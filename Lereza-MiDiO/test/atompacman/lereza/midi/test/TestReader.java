package atompacman.lereza.midi.test;

import atompacman.atomLog.Log;
import atompacman.atomLog.Log.Verbose;
import atompacman.leraza.midi.container.MIDIFile;
import atompacman.leraza.midi.device.MIDIFileReader;

public class TestReader {

	public static void main(String[] args) {
		Log.setVerbose(Verbose.EXTRA);
		MIDIFileReader midiFileReader = new MIDIFileReader();
		MIDIFile midiFile = null;
		
		if (args.length != 0) {
			midiFile = midiFileReader.readFile(args[0]);
		} else {
			Log.error("No argument provided.");
			return;
		}
		midiFile.addInfo("Salut");
	}
}
