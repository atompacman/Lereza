package atompacman.leraza.midi.device;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import atompacman.atomLog.Log;
import atompacman.leraza.midi.utilities.Formatting;

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
		Log.infos(Formatting.lineSeparation("MIDI Player Initialization", 0));
		try {
			synthetizer = getSynthesizer();
			synthetizer = MidiSystem.getSynthesizer();
			Log.infos("Opening synthesizer.");
			synthetizer.open();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
		channels = synthetizer.getChannels();
		instruments = synthetizer.getDefaultSoundbank().getInstruments();
		Log.infos("Loading instruments.");
		synthetizer.loadInstrument(instruments[6]);
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
	//     PLAY SIMPLE NOTE     //
	//////////////////////////////
	
	public void playNote(int note) {
		if (!initialized) {
			Log.error("MIDI Simple Note Player: Cannot play note; MIDIFilePlayer not initialized.");
			return;
		}
		channels[5].noteOn(note, 600);
	}
}
