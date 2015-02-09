package com.atompacman.lereza.midi;

import java.io.IOException;

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
import com.atompacman.lereza.api.Device;
import com.atompacman.lereza.api.Wizard;
import com.atompacman.lereza.exception.MIDIFileReaderException;
import com.atompacman.lereza.report.ActivityReports;
import com.atompacman.lereza.report.ReportManager;
import com.atompacman.lereza.report.anomaly.Anomaly;
import com.atompacman.lereza.report.anomaly.AnomalyInfo;
import com.atompacman.lereza.report.anomaly.AnomalyInfo.Impact;
import com.atompacman.lereza.report.anomaly.AnomalyInfo.Recoverability;
import com.atompacman.lereza.report.anomaly.AnomalyReport;
import com.atompacman.lereza.solfege.CircleOfFifths;
import com.atompacman.lereza.solfege.Key;
import com.atompacman.lereza.solfege.Meter;
import com.atompacman.lereza.solfege.Tone;
import com.atompacman.lereza.solfege.quality.Quality;
import com.atompacman.toolkat.exception.Throw;
import com.atompacman.toolkat.io.IO;

public class MIDIFileReader implements Device {

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

	private enum MIDIFileAnomaly implements Anomaly {

		IGNORED_MIDI_EVENT { public AnomalyInfo getInfo() { return new AnomalyInfo(
				"Ignored MIDI event", 
				"Processing for some MIDI event is not implemented", 
				"May contain useful information", 
				Impact.MINIMAL, 
				Recoverability.NORMAL,
				MIDIFileReader.class);
		}
		}
	}



	//======================================= FIELDS =============================================\\

	// Temporary fields
	private MIDISequence 	seq;
	private MIDITrack		track;
	private MidiEvent		event;
	private AnomalyReport	anomalies;



	//======================================= METHODS ============================================\\

	//---------------------------------- PRIVATE CONSTRUCTOR -------------------------------------\\

	private MIDIFileReader() {
		if (Log.infos() && Log.title("MIDI File Reader", 1));
		ActivityReports ar = Wizard.getDevice(ReportManager.class).getActivityReports(getClass());
		this.anomalies = ar.getReport(AnomalyReport.class);
	}


	//---------------------------------------- READ ----------------------------------------------\\

	public MIDISequence read(String midiFilePath) throws MIDIFileReaderException {
		if (Log.infos() && Log.print("Loading MIDI file \"" + midiFilePath + "\""));

		seq = new MIDISequence(midiFilePath);
		Sequence midiSequence = null;

		try {
			midiSequence = MidiSystem.getSequence(IO.getFile(midiFilePath));
		} catch (InvalidMidiDataException | IOException e) {
			Throw.a(MIDIFileReaderException.class, "Could not load "
					+ "MIDI sequence at \"" + midiFilePath + "\"", e);
		}

		if (Log.extra()) {
			printSequenceInfo(midiSequence);
		}


		for (int i = 0; i < midiSequence.getTracks().length; ++i) {
			if (Log.infos() && Log.title("Reading track " + (i + 1), 2));
			readTrack(midiSequence.getTracks()[i]);
			seq.addTrack(track);
		}

		return seq;
	}

	private void readTrack(Track midiTrack) {
		track = new MIDITrack();
		for (int i = 0; i < midiTrack.size(); ++i) {
			event = midiTrack.get(i);
			processEvent();
		}
	}


	//------------------------------------ PROCESS EVENT -----------------------------------------\\

	private void processEvent() {
		MidiMessage msg = event.getMessage();
		if (msg instanceof ShortMessage) {
			processMessage((ShortMessage) msg);
		} else if (msg instanceof MetaMessage) {
			processMessage((MetaMessage) msg);
		} else {
			processMessage((SysexMessage) msg);
		}
	}

	private void processMessage(MetaMessage msg) {
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
			seq.addTempoChange(event.getTick(), 60000000.0 / microSec);		break;
		case TEXT:					track.addText(strData);					break;
		case TIME_SIGNATURE:
			seq.setSignature(new Meter(data[0], (int) Math.pow(2, data[1])));
			seq.setNum32thNotesPerBeat(data[3]);							break;
		case TRACK_NAME:			track.setName(strData);					break;
		default:		
			ignored = true;
			break;
		}
		
		if (ignored || Log.extra()) {
			String message = String.format("%s %-17s |  MetaMessage   | %s (%s)", ignored ? 
					"[ Ignored ]" : "[Processed]", type.name(), strData, toString(data));
			if (ignored) {
				anomalies.record(MIDIFileAnomaly.IGNORED_MIDI_EVENT, message, true);
			} else {
				if (Log.extra() && Log.print(message));
			}
		}
	}

	private void processMessage(SysexMessage msg) {
		byte[] data = msg.getData();
		String strData = new String(data);
		
		String message = String.format("[ Ignored ] %-17s |  SysexMessage  | %s (%s)", 
				msg.getStatus() == SysexMessage.SPECIAL_SYSTEM_EXCLUSIVE ? 
						"SPECIAL SYST. EXCL." : "SYSTEM EXCLUSIVE", strData, toString(data));
		anomalies.record(MIDIFileAnomaly.IGNORED_MIDI_EVENT, message, true);
	}

	private void processMessage(ShortMessage msg) {
		ChannelMessageCmd cmd = ChannelMessageCmd.of(msg);
		boolean ignored = false;
		boolean showMsg = true;

		switch(cmd) {
		case NOTE_OFF: case NOTE_ON: 	
			track.addNote(msg.getData1(), msg.getData2(), event.getTick()); 
			showMsg = false;															
			break;

		case PROGRAM_CHANGE:			
			track.addInstrumentChange(msg.getData1(), event.getTick());					
			break;

		case SYSTEM_MESSAGE:			
			processSystemMessage(msg); 						
			break;

		default: 
			ignored = true;
		}

		if (ignored || Log.extra()) {
			String message = String.format("%s %-17s | ChannelMessage | %3d %3d", ignored ? 
					"[ Ignored ]" : "[Processed]", cmd.name(), msg.getData1(), msg.getData2());
			if (ignored) {
				anomalies.record(MIDIFileAnomaly.IGNORED_MIDI_EVENT, message, true);
			} else {
				if (showMsg && Log.extra() && Log.print(message));
			}
		}
	}

	private void processSystemMessage(ShortMessage msg) {
		SystemCommonMessageType scmt = SystemCommonMessageType.of(msg);
		String message = String.format("[ Ignored ] %-17s | System Message |", scmt.name());
		if (Log.extra() && Log.print(message));
		anomalies.record(MIDIFileAnomaly.IGNORED_MIDI_EVENT, message, true);
	}


	//--------------------------------------- SHUTDOWN -------------------------------------------\\

	public void shutdown() {

	}


	//==================================== STATIC METHODS ========================================\\

	//---------------------------------------- PRINT ---------------------------------------------\\

	private static void printSequenceInfo(Sequence seq) {
		Log.print("MIDI sequence infos");
		Log.print("%4s%-18s : %d", "", "Num of tracks", seq.getTracks().length);
		Log.print("%4s%-18s : %.2f%s", "", "Duration", 
				(double) seq.getMicrosecondLength() / 1000000.0, 
				" sec (" + seq.getTickLength() + " ticks)");
		Log.print("%4s%-18s : %s", "", "Timing resolution", seq.getResolution() + " ticks per " +
				(seq.getDivisionType() != Sequence.PPQ ? " " + "frame (" + seq.getDivisionType() + 
						" frames per second) (SMPTE-based)" : "quarter note (tempo-based)"));
	}

	private static String toString(byte[] bytes) {
		if (bytes.length == 0) {
			return "";
		}
		if (bytes.length > 8) {
			return "...";
		}
		StringBuilder sb = new StringBuilder();
		sb.append(bytes[0]);
		for (int i = 1; i < bytes.length; ++i) {
			sb.append(' ');
			sb.append(Byte.toString(bytes[i]));
		}
		return sb.toString();
	}
}