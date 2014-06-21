package atompacman.leraza.midi.device;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import atompacman.atomLog.Log;
import atompacman.leraza.midi.MiDiO;
import atompacman.leraza.midi.Parameters;
import atompacman.leraza.midi.container.MIDIFile;
import atompacman.leraza.midi.container.MIDINote;
import atompacman.leraza.midi.utilities.Formatting;

public class MIDIFilePlayer {

	private MidiChannel[] channels;
	private Synthesizer synthesizer;
	private boolean initialized = false;

	public enum Instrument {
		Acoustic_Grand_Piano, Bright_Acoustic_Piano, Electric_Grand_Piano, Honky_tonk_Piano, Electric_Piano_1, Electric_Piano_2,
		Harpsichord, Clavinet, Celesta, Glockenspiel, Music_Box, Vibraphone, Marimba, Xylophone, Tubular_Bells, Dulcimer, Drawbar_Organ,
		Percussive_Organ, Rock_Organ, Church_Organ, Reed_Organ, Accordion, Harmonica, Tango_Accordion, Acoustic_Guitar_nylon, 
		Acoustic_Guitar_steel, Electric_Guitar_jazz, Electric_Guitar_clean, Electric_Guitar_muted, Overdriven_Guitar, Distortion_Guitar, 
		Guitar_harmonics, Acoustic_Bass, Electric_Bass_finger, Electric_Bass_pick, Fretless_Bass, Slap_Bass_1, Slap_Bass_2, Synth_Bass_1,
		Synth_Bass_2, Violin, Viola, Cello, Contrabass, Tremolo_Strings, Pizzicato_Strings, Orchestral_Harp, Timpani, String_Ensemble_1,
		String_Ensemble_2, Synth_Strings_1, Synth_Strings_2, Choir_Aahs, Voice_Oohs, Synth_Voice, Orchestra_Hit, Trumpet, Trombone, Tuba,
		Muted_Trumpet, French_Horn, Brass_Section, Synth_Brass_1, Synth_Brass_2, Soprano_Sax, Alto_Sax, Tenor_Sax, Baritone_Sax, Oboe,
		English_Horn, Bassoon, Clarinet, Piccolo, Flute, Recorder, Pan_Flute, Blown_Bottle, Shakuhachi, Whistle, Ocarina, Lead_1_square,
		Lead_2_sawtooth, Lead_3_calliope, Lead_4_chiff, Lead_5_charang, Lead_6_voice, Lead_7_fifths, Lead_8_bass_lead, Pad_1_new_age, 
		Pad_2_warm, Pad_3_polysynth, Pad_4_choir, Pad_5_bowed, Pad_6_metallic, Pad_7_halo, Pad_8_sweep, FX_1_rain, FX_2_soundtrack,
		FX_3_crystal, FX_4_atmosphere, FX_5_brightness, FX_6_goblins, FX_7_echoes, FX_8_sci_fi, Sitar, Banjo, Shamisen, Koto,Kalimba,
		Bag_pipe, Fiddle, Shanai, Tinkle_Bell, Agogo, Steel_Drums, Woodblock, Taiko_Drum, Melodic_Tom, Synth_Drum, Reverse_Cymbal,
	 	Guitar_Fret_Noise, Breath_Noise, Seashore, Bird_Tweet, Telephone_Ring, Helicopter, Applause, Gunshot;
	}

	private class TrackPlayer implements Runnable {
	
		private List<MIDINote> midiTrack;
		private int channelNo;
		private Instrument instrument;
		
		public TrackPlayer(List<MIDINote> midiTrack, int channelNo, Instrument instrument) {
			this.midiTrack = midiTrack;
			this.channelNo = channelNo;
			this.instrument = instrument;
		}

		public void run() {
			playMIDItrack(midiTrack, channelNo, instrument);
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
		if (!initialized) {
			Log.error("MIDI Simple Note Player: Cannot play note; MIDIFilePlayer not initialized.");
			return;
		}
		channels[0].noteOn(note, 600);
	}

	public void stopNote(int note) {
		if (!initialized) {
			Log.error("MIDI Simple Note Player: Cannot stop note; MIDIFilePlayer not initialized.");
			return;
		}
		channels[0].noteOff(note);
	}

	public void playFor(int note, int length, int channel) {
		if (!initialized) {
			Log.error("MIDI Simple Note Player: Cannot play note; MIDIFilePlayer not initialized.");
			return;
		}
		channels[channel].noteOn(note, 600);
		try {
			Thread.sleep(length);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		channels[channel].noteOff(note);
	}
	
	public void playFor(int note, int length) {
		playFor(note, length, 0);
	}

	
	//////////////////////////////
	//     PLAY MIDI TRACK      //
	//////////////////////////////
	
	public void playMIDItrack(List<MIDINote> notes, int channelNo, Instrument instrument) {
		try {
			setInstrument(channelNo, instrument);
			
			for (int j = 0; j < notes.size() - 1; ++j) {
				MIDINote note = notes.get(j);
				MIDINote nextNote = notes.get(j + 1);
				if (note.getLength() < 16 && nextNote.isRest()) {
					MiDiO.player.playFor(note.getNote(), (note.getLength() + nextNote.getLength() / 2) * Parameters.PLAYBACK_SPEED_CORRECTION, channelNo);
					Thread.sleep(nextNote.getLength() * Parameters.PLAYBACK_SPEED_CORRECTION / 2);
					++j;
				} else if (note.isRest()) {
					Thread.sleep(note.getLength() * Parameters.PLAYBACK_SPEED_CORRECTION);
				} else {
					MiDiO.player.playFor(note.getNote(), note.getLength() * Parameters.PLAYBACK_SPEED_CORRECTION, channelNo);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void playMIDItrack(List<MIDINote> notes, Instrument instrument) {
		playMIDItrack(notes, 0, instrument);
	}
	
	public void playMIDItrack(List<MIDINote> notes) {
		playMIDItrack(notes, 0, Instrument.Acoustic_Grand_Piano);
	}
	
	
	//////////////////////////////
	//     PLAY MIDI FILE       //
	//////////////////////////////
	
	public void playMIDIFile(MIDIFile midiFile) {
		playMIDIFile(midiFile, null);
	}
	
	public void playMIDIFile(MIDIFile midiFile, List<Instrument> instruments) {	
		if (instruments == null) {
			instruments = new ArrayList<Instrument>();
			for (int i = 0; i < midiFile.getNotes().size(); ++i) {
				instruments.add(Instrument.Acoustic_Grand_Piano);
			}
		}
		try {
			List<Thread> threads = new ArrayList<Thread>();
			List<List<MIDINote>> notes = midiFile.getNotes();
			
			for (int i = 0; i < notes.size(); ++i) {
				if (!notes.get(i).isEmpty()) {
					notes.get(i).add(0, new MIDINote(0, midiFile.getTimeBeforeFirstNote(i)));
				}
			}
			
			for (int i = 0; i < notes.size(); ++i) {
				List<MIDINote> midiTrack = notes.get(i);
				if (!midiTrack.isEmpty()) {
					Thread thread = new Thread(new TrackPlayer(midiTrack, i, instruments.get(i)));
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

	public void setInstrument(int channel, Instrument instr) {
		channels[channel].programChange(instr.ordinal());
	}
	
	public void setInstrument(Instrument instr) {
		channels[0].programChange(instr.ordinal());
	}
}
