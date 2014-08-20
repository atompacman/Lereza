package atompacman.leraza.midi.io;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import com.atompacman.atomLog.Log;

import atompacman.leraza.midi.Parameters;
import atompacman.leraza.midi.api.MidiFilePlayerAPI;
import atompacman.leraza.midi.container.MidiInstrument;
import atompacman.leraza.midi.container.MidiFile;
import atompacman.leraza.midi.container.MidiNote;
import atompacman.leraza.midi.utils.HexToNote;

public class MidiFilePlayer implements MidiFilePlayerAPI {

	private static MidiFilePlayer player;
	
	private MidiChannel[] channels;
	private Synthesizer synthesizer;
	
	
	private class TrackPlayer implements Runnable {
	
		private String filePath;
		private int channelNo;
		private double tempo;
		private MidiInstrument instrument;
		
		public TrackPlayer(String filePath, int channelNo, double tempo, MidiInstrument instrument) {
			this.filePath = filePath;
			this.channelNo = channelNo;
			this.tempo = tempo;
			this.instrument = instrument;
		}

		public void run() {
			playTrack(filePath, channelNo, tempo, instrument);
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
			playNote(note.getNote(), note.getLength(), tempo, channelNo);
		}
	}
	
	static {
		player = new MidiFilePlayer();
	}
	
	
	//////////////////////////////
	//      STATIC ACCESS       //
	//////////////////////////////

	public static MidiFilePlayer getPlayer() {
		return player;
	}
	
	private MidiFilePlayer() {
		if (Log.infos() && Log.title("MIDI Player", 0));
		if (Log.infos() && Log.print("Initialization."));
		try {
			synthesizer = getSynthesizer();
			synthesizer = MidiSystem.getSynthesizer();
			if (Log.infos() && Log.print("Opening synthesizer."));
			synthesizer.open();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
		channels = synthesizer.getChannels();
	}

	private Synthesizer getSynthesizer() throws MidiUnavailableException {
		if (Log.extra() && Log.title("Synthesizer selection", 1));
		if (Log.extra() && Log.print("Nb of installed MIDI devices in total: " + MidiSystem.getMidiDeviceInfo().length));
		int nbSynthesizer = 0;
		Synthesizer synth = null;

		for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
			MidiDevice midiDevice = MidiSystem.getMidiDevice(info);
			if (midiDevice instanceof Synthesizer) {
				synth = (Synthesizer) midiDevice;
				if (Log.extra() && Log.title("", 3));
				if (Log.extra() && Log.print("Synth name    : " + info.getName()));
				if (Log.extra() && Log.print("Version       : " + info.getVersion()));
				if (Log.extra() && Log.print("Vendor        : " + info.getVendor()));
				if (Log.extra() && Log.print("Description   : " + info.getDescription()));
				if (Log.extra() && Log.print("Latency       : " + (double)synth.getLatency() / 1000000 + " sec"));
				if (Log.extra() && Log.print("Max polyphony : " + synth.getMaxPolyphony()));
				++nbSynthesizer;
			}
		}
		if (Log.extra() && Log.title("", 2));
		if (Log.extra() && Log.print("Nb of installed MIDI synths: " + nbSynthesizer));
		if (nbSynthesizer != 1) {
			Log.print("Automatic selection of the best synthetizer has not been implemented. Choosing the first one.");
		}
		if (Log.extra() && Log.title(1));
		return synth;
	}


	//////////////////////////////
	//  PLAY/STOP SIMPLE NOTE   //
	//////////////////////////////

	public void startNote(int note) {
		startNote(note, 0);
	}
	
	public void startNote(int note, int channel) {
		if (Log.extra() && Log.print("Starting note " + HexToNote.toString(note)));
		channels[channel].noteOn(note, 600);
	}

	public void stopNote(int note, int channel) {
		if (Log.extra() && Log.print("Stopping note " + HexToNote.toString(note)));
		channels[channel].noteOff(note);
	}
	
	public void stopNote(int note) {
		stopNote(note, 0);
	}

	private void playNote(int note, int length, double tempo, int channel) {
		if (Log.extra() && Log.print("Playing the note " + HexToNote.toString(note) + " (length: " + length + ") (channel: " + channel + ")"));
		channels[channel].noteOn(note, 600);
		try {
			Thread.sleep((int)(((double) length - Parameters.CONSECUTIVE_NOTE_CORRECTION) * Parameters.NOTE_PLAYBACK_SPEED_CORRECTION * tempo));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		channels[channel].noteOff(note);
	}
	
	public void playNote(int note, int length, double tempo) {
		playNote(note, length, tempo, 0);
	}
	
	
	//////////////////////////////
	//           REST           //
	//////////////////////////////
	
	public void rest(int length, double tempo) {
		if (Log.extra() && Log.print("Resting for " + length + "."));
		try {
			Thread.sleep((int)(((double) length - Parameters.CONSECUTIVE_NOTE_CORRECTION) * Parameters.NOTE_PLAYBACK_SPEED_CORRECTION * tempo));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	//////////////////////////////
	//     PLAY NOTE STACK      //
	//////////////////////////////
	
	public void playNoteStack(Stack<MidiNote> noteStack, int channel, double tempo) {
		if (noteStack != null) {
			for (MidiNote note : noteStack) {
				Thread notePlayer = new Thread(new NotePlayer(note, channel, tempo));
				notePlayer.start();
			}
		}
	}
	
	
	//////////////////////////////
	//     PLAY MIDI TRACK      //
	//////////////////////////////
	
	public void playTrack(String filePath, int track, double tempo, MidiInstrument instrument) {
		if (Log.infos() && Log.title("MIDI Player", 0));
		if (Log.infos() && Log.title("Playing a track on channel " + track + ".", 1));
		setInstrument(instrument, track);
		try {
			MidiFile midiFile = MidiFileReader.getMidiFile(filePath);
			Map<Integer,Stack<MidiNote>> notes = midiFile.getNotes().get(track);
			
			for (int j = 0; j <= midiFile.getFinalTimestamp(); ++j) {
				Stack<MidiNote> noteStack = notes.get(j);
				playNoteStack(noteStack, track, tempo);
				Thread.sleep((int)(Parameters.NOTE_PLAYBACK_SPEED_CORRECTION * tempo));
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void playTrack(String filePath, int track, double tempo) {
		playTrack(filePath, track, tempo, MidiInstrument.Acoustic_Grand_Piano);
	}
	
	
	//////////////////////////////
	//     PLAY MIDI FILE       //
	//////////////////////////////
	
	public void playFile(String filePath) {
		playFile(filePath, false);
	}
	
	public void playFileAndWait(String filePath) {
		playFile(filePath, true);
	}
	
	private void playFile(String filePath, boolean waitForSongCompletion) {
		if (Log.infos() && Log.title("MIDI Player", 0));
		if (Log.infos() && Log.title("Now playing: " + filePath, 1));
		try {
			MidiFile midiFile = MidiFileReader.getMidiFile(filePath);
			List<MidiInstrument> instruments = (MidiFileReader.getMidiFile(filePath)).getInstruments();
			List<Thread> threads = new ArrayList<Thread>();
			
			for (int i = 0; i < midiFile.getNbTracks(); ++i) {
				if (!midiFile.getNotes().get(i).isEmpty()) {
					MidiInstrument instr = (instruments == null) ? MidiInstrument.Acoustic_Grand_Piano : instruments.isEmpty() ? MidiInstrument.Acoustic_Grand_Piano : instruments.get(i);
					Thread thread = new Thread(new TrackPlayer(filePath, i, (double) midiFile.getTempo() / Parameters.TRACK_PLAYBACK_SPEED_CORRECTION, instr));
					thread.setName("TrackPlayer: Track " + i);
					thread.start();
					threads.add(thread);
				}
			}
			if (waitForSongCompletion) {
				for (Thread thread : threads) {
					thread.join();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	//////////////////////////////
	//    CHANGE INSTRUMENT     //
	//////////////////////////////

	public void setInstrument(MidiInstrument instr, int channel) {
		if (Log.infos() && Log.title("MIDI Player", 0));
		if (Log.infos() && Log.print("Setting channel no." + channel + " to the instrument \"" + instr.name() + "\"."));
		channels[channel].programChange(instr.ordinal());
	}
	
	public void setInstrumentToAllChannels(MidiInstrument instr) {
		if (Log.infos() && Log.title("MIDI Player", 0));
		if (Log.infos() && Log.print("Setting all channels to the instrument \"" + instr.name() + "\"."));
		for (MidiChannel channel : channels) {
			channel.programChange(instr.ordinal());
		}
	}
}
