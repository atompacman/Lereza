package atompacman.leraza.midi.device;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import atompacman.atomLog.Log;

public class MIDIFilePlayer {

	private MidiChannel[] channels;
	private Synthesizer synthetizer;
	private Instrument[] instruments;
	private boolean initialized = false;
	
	
	//////////////////////////////
	//       INITIALIZE         //
	//////////////////////////////
	
	public void initialize() {
		if (initialized) {
			return;
		}
		Log.normalMsg("================================= MIDIFilePlayer ==================================");
		Log.normalMsg("= = = = = = = = = = = = = = = = =  Initializing = = = = = = = = = = = = = = = = = =");
		
		try {
			Log.normalMsg("Getting synthesizer from Midi System.");
			synthetizer = MidiSystem.getSynthesizer();
			Log.normalMsg("Opening synthesizer.");
			synthetizer.open();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
		channels = synthetizer.getChannels();
		instruments = synthetizer.getDefaultSoundbank().getInstruments();
		Log.normalMsg("Loading instruments.");
		synthetizer.loadInstrument(instruments[6]);
		initialized = true;
		
		Log.normalMsg("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =");
		Log.normalMsg("===================================================================================");
	}
	
	
	//////////////////////////////
	//     PLAY SIMPLE NOTE     //
	//////////////////////////////
	
	public void playNote(int note) {
		if (!initialized) {
			Log.errorMsg("MIDI Simple Note Player: Cannot play note; MIDIFilePlayer not initialized.");
			return;
		}
		channels[5].noteOn(note, 600);
	}
}
