package atompacman.lereza.midi.test;

import atompacman.atomLog.Log;
import atompacman.atomLog.Log.Verbose;
import atompacman.leraza.midi.MiDiO;
import atompacman.leraza.midi.container.MIDIFile;

public class TestPlayer {
	
	public static void main(String[] args) {
		Log.setVerbose(Verbose.EXTRA);
		MiDiO.initialize();
//		MIDIFile midiFile = null;
//		
//		if (args.length != 0) {
//			midiFile = MiDiO.reader.readFile(args[0]);
//		} else {
//			Log.error("No argument provided.");
//			return;
//		}
//		midiFile.addInfo("salut");
	}
}
