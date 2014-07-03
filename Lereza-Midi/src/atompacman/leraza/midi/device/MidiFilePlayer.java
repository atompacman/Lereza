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
import atompacman.leraza.midi.MidiFileManager;
import atompacman.leraza.midi.Parameters;
import atompacman.leraza.midi.api.MidiFilePlayerAPI;
import atompacman.leraza.midi.container.MidiInstrument;
import atompacman.leraza.midi.container.MidiFile;
import atompacman.leraza.midi.container.MidiNote;
import atompacman.leraza.midi.utils.HexToNote;
import atompacman.lereza.common.architecture.Device;
import atompacman.lereza.common.formatting.Formatting;

public class MidiFilePlayer implements MidiFilePlayerAPI, Device {

	private MidiChannel[] channels;
	private Synthesizer synthesizer;
	
	
	private class TrackPlayer implements Runnable {
	
		private String filePath;
		private int channelNo;
		private int finalTimestamp;
		private double tempo;
		private MidiInstrument instrument;
		
		public TrackPlayer(String filePath, int channelNo, int finalTimestamp, double tempo, MidiInstrument instrument) {
			this.filePath = filePath;
			this.channelNo = channelNo;
			this.finalTimestamp = finalTimestamp;
			this.tempo = tempo;
			this.instrument = instrument;
		}

		public void run() {
			playMIDItrack(filePath, channelNo, finalTimestamp, tempo, instrument);
		}
	}
	
	private class NotePlayer implements Runnable {
		private MidiNote note;
		private int channelNo;
		private double tempo;
		
		public NotePlayer(MidiNote note, int channelNo, double tempo) {
			this.note = note;
			this.channelNo = channelNo;
			this.tempo = tempo;
		}

		public void run() {
			playNoteFor(note.getNote(), note.getLength(), tempo, channelNo);
		}
	}
	
	
	//////////////////////////////
	//       INITIALIZE         //
	//////////////////////////////

	public MidiFilePlayer() {
		Log.infos(Formatting.lineSeparation("MIDI Player", 0));
		Log.infos("Initialization.");
		try {
			synthesizer = getSynthesizer();
			synthesizer = MidiSystem.getSynthesizer();
			Log.infos("Opening synthesizer.");
			synthesizer.open();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
		channels = synthesizer.getChannels();
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
			Log.infos("Only one synthetizer installed on MIDI system: Choosing it.");
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
		Log.extra("Starting note " + HexToNote.toString(note));
		channels[channel].noteOn(note, 600);
	}

	public void stopNote(int note, int channel) {
		Log.extra("Stopping note " + HexToNote.toString(note));
		channels[channel].noteOff(note);
	}
	
	public void stopNote(int note) {
		stopNote(note, 0);
	}

	public void playNoteFor(int note, int length, double tempo, int channel) {
		Log.extra("Playing the note " + HexToNote.toString(note) + " (length: " + length + ") (channel: " + channel + ")");
		channels[channel].noteOn(note, 600);
		try {
			Thread.sleep((int)((length - Parameters.CONSECUTIVE_NOTE_CORRECTION) * Parameters.PLAYBACK_SPEED_CORRECTION * tempo));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		channels[channel].noteOff(note);
	}
	
	public void playNoteFor(int note, int length, double tempo) {
		playNoteFor(note, length, tempo, 0);
	}

	
	//////////////////////////////
	//     PLAY MIDI TRACK      //
	//////////////////////////////
	
	public void playMIDItrack(String filePath, int track, int finalTimestamp, double tempo, MidiInstrument instrument) {
		Log.infos(Formatting.lineSeparation("MIDI Player", 0));
		Log.infos(Formatting.lineSeparation("Playing a track on channel " + track + ".", 1));
		setInstrument(instrument, track);
		try {
			Map<Integer,Stack<MidiNote>> notes = MidiFileManager.reader.getMidiFile(filePath).getNotes().get(track);
			
			for (int j = 0; j <= finalTimestamp; ++j) {
				Stack<MidiNote> noteStack = notes.get(j);
				if (noteStack != null) {
					for (MidiNote note : noteStack) {
						Thread notePlayer = new Thread(new NotePlayer(note, track, tempo));
						notePlayer.start();
					}
				}
				Thread.sleep((int)(Parameters.PLAYBACK_SPEED_CORRECTION * tempo));
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void playMIDItrack(String filePath, int track, int finalTimestamp, double tempo) {
		playMIDItrack(filePath, track, finalTimestamp, tempo, MidiInstrument.Acoustic_Grand_Piano);
	}
	
	
	//////////////////////////////
	//     PLAY MIDI FILE       //
	//////////////////////////////
	
	public void playMIDIFile(String filePath) {
		playMIDIFile(filePath, (MidiFileManager.reader.getMidiFile(filePath)).getInstruments());
	}
	
	public void playMIDIFile(String filePath, List<MidiInstrument> instruments) {
		Log.infos(Formatting.lineSeparation("MIDI Player", 0));
		Log.infos(Formatting.lineSeparation("Now playing: " + filePath, 1));
		try {
			MidiFile midiFile = MidiFileManager.reader.getMidiFile(filePath);
			List<Thread> threads = new ArrayList<Thread>();
			
			for (int i = 0; i < midiFile.getNbTracks(); ++i) {
				if (!midiFile.getNotes().get(i).isEmpty()) {
					MidiInstrument instr = (instruments == null) ? MidiInstrument.Acoustic_Grand_Piano : instruments.isEmpty() ? MidiInstrument.Acoustic_Grand_Piano : instruments.get(i);
					Thread thread = new Thread(new TrackPlayer(filePath, i, midiFile.getFinalTimestamp(), 1.0/** / midiFile.getTempo()**/, instr));
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

	public void setInstrument(MidiInstrument instr, int channel) {
		Log.infos("Setting channel no." + channel + " to the instrument \"" + instr.name() + "\".");
		channels[channel].programChange(instr.ordinal());
	}
}
