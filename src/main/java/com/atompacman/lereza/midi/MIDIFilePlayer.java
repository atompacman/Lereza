package com.atompacman.lereza.midi;

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
import com.atompacman.lereza.Parameters;
import com.atompacman.lereza.api.MIDIFilePlayerAPI;
import com.atompacman.lereza.common.database.Database;
import com.atompacman.lereza.exception.DatabaseException;
import com.atompacman.lereza.midi.container.MIDIFile;
import com.atompacman.lereza.midi.container.MIDIInstrument;
import com.atompacman.lereza.midi.container.MIDINote;

public class MIDIFilePlayer implements MIDIFilePlayerAPI {

	private static MIDIFilePlayer player;

	private MidiChannel[] channels;
	private Synthesizer synthesizer;


	private class TrackPlayer implements Runnable {

		private MIDIFile file;
		private int channelNo;
		private double tempo;
		private MIDIInstrument instrument;

		public TrackPlayer(MIDIFile file, int channelNo, double tempo, MIDIInstrument instrument) {
			this.file = file;
			this.channelNo = channelNo;
			this.tempo = tempo;
			this.instrument = instrument;
		}

		public void run() {
			playTrack(file, channelNo, tempo, instrument);
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
			playNote(note.getNote(), note.getLength(), tempo, channelNo);
		}
	}


	//------------ STATIC ACCESS ------------\\

	public static MIDIFilePlayer getPlayer() {
		if (player == null) {
			player = new MIDIFilePlayer();
		}
		return player;
	}

	private MIDIFilePlayer() {
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
		if (Log.extra() && Log.print("Nb of installed MIDI devices in total: " 
				+ MidiSystem.getMidiDeviceInfo().length));
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
				if (Log.extra() && Log.print("Max polyphony : " + synth.getMaxPolyphony()));
				if (Log.extra() && Log.print("Latency       : " 
						+ (double)synth.getLatency() / 1000000 + " sec"));
				++nbSynthesizer;
			}
		}
		if (Log.extra() && Log.title("", 2));
		if (Log.extra() && Log.print("Nb of installed MIDI synths: " + nbSynthesizer));
		if (nbSynthesizer != 1) {
			Log.print("Automatic selection of the best synthetizer has "
					+ "not been implemented. Choosing the first one.");
		}
		if (Log.extra() && Log.title(1));
		return synth;
	}


	//------------ PLAY/STOP SIMPLE NOTE ------------\\

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

	public void playNote(int note, int length, double tempo) {
		playNote(note, length, tempo, 0);
	}
	
	private void playNote(int note, int length, double tempo, int channel) {
		if (Log.extra() && Log.print("Playing the note " + HexToNote.toString(note) 
				+ " (length: " + length + ") (channel: " + channel + ")"));
		channels[channel].noteOn(note, 600);
		try {
			Thread.sleep((int)(((double) length - Parameters.CONSECUTIVE_NOTE_CORRECTION) * 
					Parameters.NOTE_PLAYBACK_SPEED_CORRECTION * tempo));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		channels[channel].noteOff(note);
	}


	//------------ REST ------------\\

	public void rest(int length, double tempo) {
		if (Log.extra() && Log.print("Resting for " + length + "."));
		try {
			Thread.sleep((int)(((double) length - Parameters.CONSECUTIVE_NOTE_CORRECTION) * 
					Parameters.NOTE_PLAYBACK_SPEED_CORRECTION * tempo));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	//------------ PLAY NOTE STACK ------------\\

	public void playNoteStack(Stack<MIDINote> noteStack, int channel, double tempo) {
		if (noteStack != null) {
			for (MIDINote note : noteStack) {
				Thread notePlayer = new Thread(new NotePlayer(note, channel, tempo));
				notePlayer.start();
			}
		}
	}


	//------------ PLAY MIDI TRACK ------------\\

	public void playTrack(int caseID, int track, double tempo) throws DatabaseException {
		MIDIFile midiFile = Database.getMIDIFile(caseID);
		playTrack(Database.getMIDIFile(caseID), track, tempo, midiFile.getInstruments().get(track));
	}

	public void playTrack(int caseID, int track, double tempo, MIDIInstrument instrument) 
			throws DatabaseException {
		playTrack(Database.getMIDIFile(caseID), track, tempo, instrument);
	}

	protected void playTrack(MIDIFile midiFile, int track, double tempo, MIDIInstrument instrument) {
		if (Log.infos() && Log.title("MIDI Player", 0));
		if (Log.infos() && Log.title("Playing a track on channel " + track + ".", 1));
		setInstrument(instrument, track);
		try {
			Map<Integer,Stack<MIDINote>> notes = midiFile.getNotes().get(track);

			for (int j = 0; j <= midiFile.getFinalTimestamp(); ++j) {
				Stack<MIDINote> noteStack = notes.get(j);
				playNoteStack(noteStack, track, tempo);
				Thread.sleep((int)(Parameters.NOTE_PLAYBACK_SPEED_CORRECTION * tempo));
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	//------------ PLAY MIDI FILE ------------\\

	public void playFile(int caseID) throws DatabaseException {
		playFile(Database.getMIDIFile(caseID), false);
	}

	public void playFileAndWait(int caseID) throws DatabaseException {
		playFile(Database.getMIDIFile(caseID), true);
	}

	protected void playFile(MIDIFile midiFile, boolean waitForSongCompletion) {
		if (Log.infos() && Log.title("MIDI Player", 0));
		if (Log.infos() && Log.title("Now playing: " + midiFile.getFileInfos().getTitle(), 1));
		try {
			List<MIDIInstrument> instruments = midiFile.getInstruments();
			List<Thread> threads = new ArrayList<Thread>();

			for (int i = 0; i < midiFile.getNbTracks(); ++i) {
				if (!midiFile.getNotes().get(i).isEmpty()) {
					MIDIInstrument instr = (instruments == null) ? MIDIInstrument.Acoustic_Grand_Piano : instruments.isEmpty() ? MIDIInstrument.Acoustic_Grand_Piano : instruments.get(i);
					Thread thread = new Thread(new TrackPlayer(midiFile, i, (double) midiFile.getTempo() / Parameters.TRACK_PLAYBACK_SPEED_CORRECTION, instr));
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


	//------------ CHANGE INSTRUMENT ------------\\

	public void setInstrument(MIDIInstrument instr, int channel) {
		if (Log.infos() && Log.title("MIDI Player", 0));
		if (Log.infos() && Log.print("Setting channel no." + channel + " to the instrument \"" + instr.name() + "\"."));
		channels[channel].programChange(instr.ordinal());
	}

	public void setInstrumentToAllChannels(MIDIInstrument instr) {
		if (Log.infos() && Log.title("MIDI Player", 0));
		if (Log.infos() && Log.print("Setting all channels to the instrument \"" + instr.name() + "\"."));
		for (MidiChannel channel : channels) {
			channel.programChange(instr.ordinal());
		}
	}
}
