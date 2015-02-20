package com.atompacman.lereza.midi;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;
import javax.sound.midi.Track;

import com.atompacman.atomlog.Log;
import com.atompacman.atomlog.Log.Verbose;
import com.atompacman.lereza.api.Module;
import com.atompacman.lereza.exception.MIDIFileReaderException;
import com.atompacman.lereza.piece.container.Piece;
import com.atompacman.lereza.report.Anomaly;
import com.atompacman.lereza.report.Anomaly.Info.Impact;
import com.atompacman.lereza.report.Anomaly.Info.Recoverability;
import com.atompacman.lereza.report.Checkpoint;
import com.atompacman.lereza.solfege.CircleOfFifths;
import com.atompacman.lereza.solfege.Key;
import com.atompacman.lereza.solfege.Meter;
import com.atompacman.lereza.solfege.Tone;
import com.atompacman.lereza.solfege.quality.Quality;
import com.atompacman.toolkat.exception.Throw;

public class MIDIFileReader extends Module {

	//====================================== SINGLETON ===========================================\\

	private static class InstanceHolder {
		private static final MIDIFileReader instance = new MIDIFileReader();
	}

	public static MIDIFileReader getInstance() {
		return InstanceHolder.instance;
	}



	//===================================== INNER TYPES ==========================================\\

	private enum MetaMessageType {

		SEQUENCE_NUMBER 	(0x00),
		TEXT				(0x01),
		COPYRIGHT_NOTICE	(0x02),
		TRACK_NAME			(0x03),
		INSTRUMENT_NAME		(0x04),
		LYRICS				(0x05),
		MARKER				(0x06),
		CUE_POINT			(0x07),
		CHANNEL_NUMBER		(0x20),
		MIDI_PORT			(0x21),
		END_OF_TRACK		(0x2F),
		SET_TEMPO			(0x51),
		SMPTE_OFFSET		(0x54),
		TIME_SIGNATURE		(0x58),
		KEY_SIGNATURE		(0x59),
		SEQUENCER_SPECIFIC	(0x7F),
		UNKNOWN				(-1);


		//===================================== FIELDS ===========================================\\

		private final int typeByte;



		//===================================== METHODS ==========================================\\

		//-------------------------------- PRIVATE CONSTRUCTOR -----------------------------------\\

		private MetaMessageType(int typeByte) {
			this.typeByte = typeByte;
		}



		//================================== STATIC METHODS ======================================\\

		public static MetaMessageType of(MetaMessage msg) {
			int metaType = msg.getType();

			for (MetaMessageType type : MetaMessageType.values()) {
				if (metaType == type.typeByte) {
					return type;
				}
			}
			if (Log.error() && Log.print("Unknown meta message type "
					+ "\"" + metaType + "\""));
			return UNKNOWN;
		}
	}

	private enum ChannelMessageCmd {

		NOTE_OFF 			(ShortMessage.NOTE_OFF),
		NOTE_ON				(ShortMessage.NOTE_ON),
		POLY_PRESSURE		(ShortMessage.POLY_PRESSURE),
		CONTROL_CHANGE		(ShortMessage.CONTROL_CHANGE),
		PROGRAM_CHANGE		(ShortMessage.PROGRAM_CHANGE),
		CHANNEL_PRESSURE	(ShortMessage.CHANNEL_PRESSURE),
		PITCH_BEND			(ShortMessage.PITCH_BEND),
		SYSTEM_MESSAGE		(0xF0);


		//===================================== FIELDS ===========================================\\

		private final int signifByte;



		//===================================== METHODS ==========================================\\

		//-------------------------------- PRIVATE CONSTRUCTOR -----------------------------------\\

		private ChannelMessageCmd(int signifByte) {
			this.signifByte = signifByte;
		}



		//================================== STATIC METHODS ======================================\\

		public static ChannelMessageCmd of(ShortMessage msg) {
			int msgCmd = msg.getCommand();

			for (ChannelMessageCmd cmd : ChannelMessageCmd.values()) {
				if (msgCmd == cmd.signifByte) {
					return cmd;
				}
			}
			if (Log.error() && Log.print("Unknown channel message command \"" + msgCmd + "\""));
			return null;
		}
	}

	private enum SystemCommonMessageType {

		MIDI_TIME_CODE 			(ShortMessage.MIDI_TIME_CODE),
		SONG_POSITION_POINTER 	(ShortMessage.SONG_POSITION_POINTER),
		SONG_SELECT 			(ShortMessage.SONG_SELECT),
		TUNE_REQUEST 			(ShortMessage.TUNE_REQUEST),
		END_OF_EXCLUSIVE 		(ShortMessage.END_OF_EXCLUSIVE),
		TIMING_CLOCK 			(ShortMessage.TIMING_CLOCK),
		START 					(ShortMessage.START),
		CONTINUE 				(ShortMessage.CONTINUE),
		STOP 					(ShortMessage.STOP),
		ACTIVE_SENSING 			(ShortMessage.ACTIVE_SENSING),
		SYSTEM_RESET 			(ShortMessage.SYSTEM_RESET);


		//===================================== FIELDS ===========================================\\

		private final int statusByte;



		//===================================== METHODS ==========================================\\

		//-------------------------------- PRIVATE CONSTRUCTOR -----------------------------------\\

		private SystemCommonMessageType(int statusByte) {
			this.statusByte = statusByte;
		}



		//================================== STATIC METHODS ======================================\\

		public static SystemCommonMessageType of(ShortMessage msg) {
			int status = msg.getStatus();

			for (SystemCommonMessageType scmt : SystemCommonMessageType.values()) {
				if (status == scmt.statusByte) {
					return scmt;
				}
			}
			if (Log.error() && Log.print("Unknown system common message \"" + status + "\""));
			return null;
		}
	}

	private enum AN implements Anomaly {

		IGNORED_MIDI_EVENT { public Info info() { return new Info(
				"Ignored MIDI event", 
				"Processing for some MIDI event is not implemented", 
				"May contain useful information", 
				Impact.MINIMAL, 
				Recoverability.NORMAL,
				MIDIFileReader.class);
		}},

		ROUNDED_MIDI_TICK { public Info info() { return new Info(
				"Rounded MIDI tick",
				"A MIDI tick (timstamp) had to be rounded to become a timeunit",
				"Possible deviations in rythm",
				Impact.MINIMAL,
				Recoverability.NORMAL,
				MIDIFileReader.class);
		}}
	}

	private enum CP implements Checkpoint {

		LOAD_MIDI_FILE_FROM_DISK { public Info info() { return new Info(
				"Loading MIDI file from disk", true);
		}},
		
		PROCESS_MIDI_EVENTS { public Info info() { return new Info(
				"Processing MIDI events", true);
		}},

		ADJUST_NOTES_TIMESTAMPS { public Info info() { return new Info(
				"Adjusting note timestamps", true);
		}},
		
		BUILD_PIECE { public Info info() { return new Info(
				"Build piece", true);
		}},
	}



	//======================================= FIELDS =============================================\\

	// Temporary fields
	private MIDISequence seq;
	private MIDITrack	 track;



	//======================================= METHODS ============================================\\

	//---------------------------------------- READ ----------------------------------------------\\

	public Piece read(File midiFile) throws MIDIFileReaderException {
		initTempFields(midiFile);

		report.start(CP.LOAD_MIDI_FILE_FROM_DISK);		
		Sequence midiSeq = loadSequenceFromDisk();

		report.stop().start(CP.PROCESS_MIDI_EVENTS);
		processMIDIEvents(midiSeq);

		report.stop().start(CP.ADJUST_NOTES_TIMESTAMPS);
		int finalTU = adjustNotesTimestamps(midiSeq.getDivisionType(), midiSeq.getResolution());

		report.stop().start(CP.BUILD_PIECE);
		return buildPiece(finalTU);
	}

	private void initTempFields(File midiFile) {
		seq = new MIDISequence(midiFile);
		track = null;
		report.setVerbose(Verbose.EXTRA);
	}

	// - - - - - - - - - - - - - - - - - LOAD FILE FROM DISK - - - - - - - - - - - - - - - - - - -\\

	private Sequence loadSequenceFromDisk() throws MIDIFileReaderException {
		report.log(Verbose.INFOS, "URL: \"" + seq.getFile().getAbsolutePath() + "\"");

		Sequence seq = null;

		try {
			seq = MidiSystem.getSequence(this.seq.getFile());
		} catch (InvalidMidiDataException | IOException e) {
			Throw.a(MIDIFileReaderException.class, "Could not load MIDI sequence "
					+ "at \"" + this.seq.getFile().getAbsolutePath() + "\"", e);
		}

		report.log("MIDI sequence infos");
		report.log("%4s%-18s : %d", "", "Num of tracks", seq.getTracks().length);
		report.log("%4s%-18s : %.2f%s", "", "Duration", (double) seq.getMicrosecondLength() 
				/ 1000000.0, " sec (" + seq.getTickLength() + " ticks)");
		report.log("%4s%-18s : %s", "", "Timing resolution", seq.getResolution() + " ticks per " +
				(seq.getDivisionType() != Sequence.PPQ ? " " + "frame (" + seq.getDivisionType() + 
						" frames per second) (SMPTE-based)" : "quarter note (tempo-based)"));
		return seq;
	}

	// - - - - - - - - - - - - - - - - - PROCESS MIDI EVENTS - - - - - - - - - - - - - - - - - - -\\

	private void processMIDIEvents(Sequence midiSeq) {
		for (int i = 0; i < midiSeq.getTracks().length; ++i) {
			report.log(Verbose.INFOS, 2, "Reading track " + (i + 1));
			track = new MIDITrack();
			Track midiTrack = midiSeq.getTracks()[i];

			for (int j = 0; j < midiTrack.size(); ++j) {
				processEvent(midiTrack.get(j));
			}
			seq.addTrack(track);
		}
	}

	private void processEvent(MidiEvent event) {
		MidiMessage msg = event.getMessage();
		if (msg instanceof ShortMessage) {
			processMessage((ShortMessage) msg, event.getTick());
		} else if (msg instanceof MetaMessage) {
			processMessage((MetaMessage) msg, event.getTick());
		} else {
			processMessage((SysexMessage) msg);
		}
	}

	private void processMessage(MetaMessage msg, Long tick) {
		byte[] data = msg.getData();
		String strData = new String(data);
		MetaMessageType type = MetaMessageType.of(msg);
		boolean ignored = false;

		switch(type) {
		case CHANNEL_NUMBER:		track.setChannelNumber(data[0]);		break;
		case COPYRIGHT_NOTICE:		seq.setCopyrightNotice(strData);		break;
		case CUE_POINT:				track.addCuePoint(strData); 			break;
		case END_OF_TRACK:													break;
		case INSTRUMENT_NAME:		track.setInstrumentName(strData);		break;
		case KEY_SIGNATURE:													
			Tone tone = CircleOfFifths.toneAtPosition(data[0]);
			Quality quality = (data[1] == 0x00) ? Quality.MAJOR : Quality.MINOR;
			seq.setKey(Key.valueOf(tone, quality));							break;
		case LYRICS:				track.addLyrics(strData);				break;
		case MARKER:				track.addMarker(strData);				break;
		case MIDI_PORT:				track.setMidiPort(data[0]);				break;
		case SET_TEMPO:
			double microSec = (double) ((data[0] << 16) + (data[1] << 8) + data[2]);
			seq.addTempoChange(tick, 60000000.0 / microSec);				break;
		case TEXT:					track.addText(strData);					break;
		case TIME_SIGNATURE:
			seq.setSignature(new Meter(data[0], (int) Math.pow(2, data[1])));
			seq.setNum32thNotesPerBeat(data[3]);							break;
		case TRACK_NAME:			track.setName(strData);					break;
		default:		
			ignored = true;
			break;
		}

		String toLog = String.format("%s %-17s |  MetaMessage   | %s %s",
				ignored ? "[ Ignored ]" : "[Processed]", type.name(), strData, toString(data));

		if (ignored) {
			report.signal(AN.IGNORED_MIDI_EVENT, toLog);
		} else {
			report.log(toLog);
		}
	}

	private void processMessage(SysexMessage msg) {
		byte[] data = msg.getData();
		String strData = new String(data);

		report.signal(AN.IGNORED_MIDI_EVENT, String.format("[ Ignored ] %-17s |  SysexMessage  | "
				+ "%s %s", msg.getStatus() == SysexMessage.SPECIAL_SYSTEM_EXCLUSIVE ? 
						"SPECIAL SYST. EXCL." : "SYSTEM EXCLUSIVE", strData, toString(data)));
	}

	private void processMessage(ShortMessage msg, Long tick) {
		ChannelMessageCmd cmd = ChannelMessageCmd.of(msg);
		boolean ignored = false;
		boolean logMsg = true;

		switch(cmd) {
		case NOTE_OFF: case NOTE_ON: 	
			track.addNote(msg.getData1(), msg.getData2(), tick); 
			logMsg = false;
			break;

		case PROGRAM_CHANGE:			
			track.addInstrumentChange(msg.getData1(), tick);					
			break;

		case SYSTEM_MESSAGE:			
			processSystemMessage(msg); 						
			break;

		default: 
			ignored = true;
		}

		String toLog = String.format("%s %-17s | ChannelMessage | %3d %3d",	ignored ? 
				"[ Ignored ]" : "[Processed]", cmd.name(), msg.getData1(), msg.getData2());

		if (ignored) {
			report.signal(AN.IGNORED_MIDI_EVENT, toLog);
		} else {
			report.log(logMsg ? Verbose.EXTRA : Verbose.OFF, toLog);
		}
	}

	private void processSystemMessage(ShortMessage msg) {
		report.signal(AN.IGNORED_MIDI_EVENT, String.format("[ Ignored ] %-17s | System Message |", 
				SystemCommonMessageType.of(msg).name()));
	}

	// - - - - - - - - - - - - - - - - ADJUST NOTES TIMESTAMPS - - - - - - - - - - - - - - - - - -\\

	private int adjustNotesTimestamps(float divisionType, int ticksPerQuarterNote) 
			throws MIDIFileReaderException {

		checkIfAdjustable(divisionType, ticksPerQuarterNote);
		int offset = findOptimalTickOffsetForRounding(ticksPerQuarterNote / 8);
		report.log("Optimal midi tick offset: " + offset);
		return convertTicksToTimeunit(offset, ticksPerQuarterNote / 8);
	}

	private void checkIfAdjustable(float divisionType, int ticksPerQuarterNote) 
			throws MIDIFileReaderException{

		if (divisionType != Sequence.PPQ) {
			Throw.a(MIDIFileReaderException.class, "SMPTE-based division type is unimplemented");
		}
		if (seq.getNum32thNotesPerBeat() != MIDISequence.DEFAULT_NUM_32TH_NOTES_PER_BEAT) {
			Throw.a(MIDIFileReaderException.class, "Unimplemented timestamp adjustment "
					+ "for sequence with num of 32th notes per beat different than "
					+ MIDISequence.DEFAULT_NUM_32TH_NOTES_PER_BEAT);
		}
		if (ticksPerQuarterNote % 8 != 0) {
			Throw.a(MIDIFileReaderException.class, "Sequence's num of "
					+ "ticks per 32th note is not integral");
		}
	}

	private int findOptimalTickOffsetForRounding(int ticksPer32thNote) {
		int minimalError = Integer.MAX_VALUE;
		int optimalOffset = 0;

		for (int offset = 0; offset < ticksPer32thNote; ++offset) {
			int error = 0;

			for (int i = 0; i < seq.getNumTracks(); ++i) {
				for (Long tick : seq.getTrack(i).getNotes().keySet()) {
					error += tick % ticksPer32thNote;
				}
			}
			if (error < minimalError) {
				minimalError = error;
				optimalOffset = offset;
			}
		}
		return optimalOffset;
	}

	private int convertTicksToTimeunit(int offset, int ticksPer32thNote) {
		int finalTimeunit = 0;
		for (int i = 0; i < seq.getNumTracks(); ++i) {
			Map<Long, Set<MIDINote>> converted = new HashMap<>();
			Map<Long, Set<MIDINote>> notes = seq.getTrack(i).getNotes();
			
			for (Entry<Long, Set<MIDINote>> entry : notes.entrySet()) {
				double timeunit = (entry.getKey() + offset) / ticksPer32thNote;
				Long roundedTU = Math.round(timeunit);
				if (timeunit != roundedTU) {
					long roundedTick = (roundedTU * ticksPer32thNote - offset);
					report.signal(AN.ROUNDED_MIDI_TICK, "From " + entry.getKey() + " to " + 
							roundedTick + "(" + (roundedTick - entry.getKey() + ")"));
				}
				converted.put(roundedTU, entry.getValue());
			}
			notes.clear();
			notes.putAll(converted);
		}
		return finalTimeunit; // TODO
	}

	//- - - - - - - - - - - - - - - - - - - BUILD PIECE - - - - - - - - - - - - - - - - - - - - - \\

	private Piece buildPiece(int finalTimeunit) {
		Piece piece = new Piece(seq);
		return piece;
	}
	
	private getFinalTimeunit() {
		for (int i = 0; i < seq.getNumTracks(); ++i) {
			
		}
	}
	
	
	//--------------------------------------- SHUTDOWN -------------------------------------------\\

	public void shutdown() {

	}



	//==================================== STATIC METHODS ========================================\\

	//-------------------------------------- TO STRING -------------------------------------------\\

	private static String toString(byte[] bytes) {
		if (bytes.length == 0) {
			return "";
		}
		if (bytes.length > 8) {
			return "(...)";
		}
		StringBuilder sb = new StringBuilder();
		sb.append('(').append(bytes[0]);
		for (int i = 1; i < bytes.length; ++i) {
			sb.append(' ');
			sb.append(Byte.toString(bytes[i]));
		}
		return sb.append(')').toString();
	}
}