package atompacman.leraza.midi.device;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import atompacman.atomLog.Log;
import atompacman.leraza.midi.Parameters;
import atompacman.leraza.midi.container.MIDIFile;
import atompacman.leraza.midi.container.MIDINote;
import atompacman.leraza.midi.utilities.Formatting;
import atompacman.leraza.midi.utilities.HexToNote;
import atompacman.leraza.midi.utilities.Instrument;

public class MIDIFilePlayer {

	private MidiChannel[] channels;
	private Synthesizer synthesizer;
	private boolean initialized = false;
	

	private class TrackPlayer implements Runnable {
	
		private Map<Integer, Stack<MIDINote>> midiTrack;
		private int channelNo;
		private int finalTimestamp;
		private double tempo;
		private Instrument instrument;
		
		public TrackPlayer(Map<Integer, Stack<MIDINote>> midiTrack, int channelNo, int finalTimestamp, double tempo, Instrument instrument) {
			this.midiTrack = midiTrack;
			this.channelNo = channelNo;
			this.finalTimestamp = finalTimestamp;
			this.tempo = tempo;
			this.instrument = instrument;
		}

		public void run() {
			playMIDItrack(midiTrack, finalTimestamp, channelNo, tempo, instrument);
		}
	}
	
	private class NotePlayer implements Runnable {
		private MIDINote note;
		private int channelNo;
		private double tempo;
		
		public NotePlayer(MIDINote note, int channelNo, double tempo) {
			this.note = note;
			this.channelNo = channelNo;
			this.tempo = tempo;
		}

		public void run() {
			playFor(note.getNote(), note.getLength(), channelNo, tempo);
		}
	}
	
	
	//////////////////////////////
	//       INITIALIZE         //
	//////////////////////////////

	public void initialize() {
		if (initialized) {
			return;
		}
		Log.infos(Formatting.lineSeparation("MIDI Player Initialization", 0));
		try {
			synthesizer = getSynthesizer();
			synthesizer = MidiSystem.getSynthesizer();
			Log.infos("Opening synthesizer.");
			synthesizer.open();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
		channels = synthesizer.getChannels();
		initialized = true;

		Log.infos(Formatting.lineSeparation(0));
	}

	private Synthesizer getSynthesizer() throws MidiUnavailableException {
		Log.extra(Formatting.lineSeparation("Synthesizer selection", 1));
		Log.extra("Nb of installed MIDI devices in total: " + MidiSystem.getMidiDeviceInfo().length);
		int nbSynthesizer = 0;
		Synthesizer synth = null;

		for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
			MidiDevice midiDevice = MidiSystem.getMidiDevice(info);
			if (midiDevice instanceof Synthesizer) {
				synth = (Synthesizer) midiDevice;
				Log.extra(Formatting.lineSeparation("", 3));
				Log.extra("Synth name    : " + info.getName());
				Log.extra("Version       : " + info.getVersion());
				Log.extra("Vendor        : " + info.getVendor());
				Log.extra("Description   : " + info.getDescription());
				Log.extra("Latency       : " + (double)synth.getLatency() / 1000000 + " sec");
				Log.extra("Max polyphony : " + synth.getMaxPolyphony());
				++nbSynthesizer;
			}
		}
		Log.extra(Formatting.lineSeparation("", 2));
		Log.extra("Nb of installed MIDI synths: " + nbSynthesizer);
		if (nbSynthesizer == 1) {
			Log.infos("Only one synthetizer installed on MIDI system. Choosing it.");
		} else {
			Log.warng("The selection of the best synthetizer has not been implemented. Choosing the first one.");
		}
		Log.extra(Formatting.lineSeparation(1));
		return synth;
	}


	//////////////////////////////
	//  PLAY/STOP SIMPLE NOTE   //
	//////////////////////////////

	public void startNote(int note) {
		startNote(note, 0);
	}
	
	public void startNote(int note, int channel) {
		if (!initialized) {
			Log.error("MIDI Simple Note Player: Cannot play note; MIDIFilePlayer not initialized.");
			return;
		}
		Log.extra("Starting note " + HexToNote.toString(note));
		channels[channel].noteOn(note, 600);
	}

	public void stopNote(int note) {
		if (!initialized) {
			Log.error("MIDI Simple Note Player: Cannot stop note; MIDIFilePlayer not initialized.");
			return;
		}
		Log.extra("Stopping note " + HexToNote.toString(note));
		channels[0].noteOff(note);
	}

	public void playFor(int note, int length, int channel, double tempo) {
		if (!initialized) {
			Log.error("MIDI Simple Note Player: Cannot play note; MIDIFilePlayer not initialized.");
			return;
		}
		Log.extra("Playing the note " + HexToNote.toString(note) + " (length: " + length + ") (channel: " + channel + ")");
		channels[channel].noteOn(note, 600);
		try {
			Thread.sleep((int)((length - Parameters.CONSECUTIVE_NOTE_CORRECTION) * Parameters.PLAYBACK_SPEED_CORRECTION * tempo));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		channels[channel].noteOff(note);
	}
	
	public void playFor(int note, int length, double tempo) {
		playFor(note, length, 0, tempo);
	}

	
	//////////////////////////////
	//     PLAY MIDI TRACK      //
	//////////////////////////////
	
	public void playMIDItrack(Map<Integer,Stack<MIDINote>> notes, int finalTimestamp, int channelNo, double tempo, Instrument instrument) {
		Log.infos(Formatting.lineSeparation("MIDI Player", 0));
		Log.infos(Formatting.lineSeparation("Playing a track on channel " + channelNo + ".", 1));
		setInstrument(instrument, channelNo);
		try {
			for (int j = 0; j <= finalTimestamp; ++j) {
				Stack<MIDINote> noteStack = notes.get(j);
				if (noteStack != null) {
					for (MIDINote note : noteStack) {
						Thread notePlayer = new Thread(new NotePlayer(note, channelNo, tempo));
						notePlayer.start();
					}
				}
				Thread.sleep((int)(Parameters.PLAYBACK_SPEED_CORRECTION * tempo));
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void playMIDItrack(Map<Integer,Stack<MIDINote>> notes, int finalTimestamp, double tempo, Instrument instrument) {
		playMIDItrack(notes, finalTimestamp, 0, tempo, instrument);
	}
	
	public void playMIDItrack(Map<Integer,Stack<MIDINote>> notes, int finalTimestamp, double tempo) {
		playMIDItrack(notes, finalTimestamp, 0, tempo, Instrument.Acoustic_Grand_Piano);
	}
	
	
	//////////////////////////////
	//     PLAY MIDI FILE       //
	//////////////////////////////
	
	public void playMIDIFile(MIDIFile midiFile) {
		playMIDIFile(midiFile, midiFile.getInstruments());
	}
	
	public void playMIDIFile(MIDIFile midiFile, List<Instrument> instruments) {
		Log.infos(Formatting.lineSeparation("MIDI Player", 0));
		Log.infos(Formatting.lineSeparation("Now playing: " + midiFile.getFilePath(), 1));
		try {
			List<Thread> threads = new ArrayList<Thread>();
			List<Map<Integer, Stack<MIDINote>>> notes = midiFile.getNotes();
			
			for (int i = 0; i < notes.size(); ++i) {
				Map<Integer, Stack<MIDINote>> midiTrack = notes.get(i);
				if (!midiTrack.isEmpty()) {
					Instrument instr = (instruments == null) ? Instrument.Acoustic_Grand_Piano : instruments.isEmpty() ? Instrument.Acoustic_Grand_Piano : instruments.get(i);
					Thread thread = new Thread(new TrackPlayer(midiTrack, i, midiFile.getFinalTimestamp(), 1.0/** / midiFile.getTempo()**/, instr));
					thread.setName("MiDiO Player: Track " + i);
					thread.start();
					threads.add(thread);
				}
			}
			for (Thread thread : threads) {
				thread.join();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	//////////////////////////////
	//    CHANGE INSTRUMENT     //
	//////////////////////////////

	public void setInstrument(Instrument instr, int channel) {
		Log.infos("Setting channel no." + channel + " to the instrument \"" + instr.name() + "\".");
		channels[channel].programChange(instr.ordinal());
	}
	
	public void setInstrument(Instrument instr) {
		setInstrument(instr, 0);
	}
}
