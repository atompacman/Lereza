package atompacman.lereza.midi.test;

import atompacman.atomLog.Log;
import atompacman.leraza.midi.MiDiO;
import atompacman.leraza.midi.container.MIDIFile;

public class TestReader {

	public static void main(String[] args) {
		MiDiO.initialize();
		MIDIFile midiFile = null;
		
		if (args.length != 0) {
			midiFile = MiDiO.reader.readFile(args[0]);
		} else {
			Log.error("No argument provided.");
			return;
		}
		midiFile.addInfo("Salut");
	}
}
