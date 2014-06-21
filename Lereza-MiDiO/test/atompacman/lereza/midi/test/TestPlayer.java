package atompacman.lereza.midi.test;

import java.util.Arrays;
import java.util.List;

import atompacman.atomLog.Log;
import atompacman.atomLog.Log.Verbose;
import atompacman.leraza.midi.MiDiO;
import atompacman.leraza.midi.container.MIDIFile;
import atompacman.leraza.midi.device.MIDIFilePlayer.Instrument;

public class TestPlayer {
	
	public static void main(String[] args) throws InterruptedException {
		Log.setVerbose(Verbose.EXTRA);
		MiDiO.initialize();
		MIDIFile midiFile = null;
		
		if (args.length != 0) {
			midiFile = MiDiO.reader.readFile(args[0]);
		} else {
			Log.error("No argument provided.");
			return;
		}

		MiDiO.player.setInstrument(Instrument.Slap_Bass_1);
		MiDiO.player.startNote(63);
		Thread.sleep(500);
		MiDiO.player.stopNote(63);
		MiDiO.player.playFor(70, 200);
		MiDiO.player.playFor(72, 200);
		MiDiO.player.playFor(74, 200);
		MiDiO.player.playFor(75, 200);
		MiDiO.player.playFor(77, 200); 
		
		List<Instrument> instruments = Arrays.asList(Instrument.Alto_Sax, Instrument.Cello, Instrument.Trombone, Instrument.Synth_Brass_1, Instrument.Sitar);
		MiDiO.player.playMIDIFile(midiFile, instruments);
	}
}
