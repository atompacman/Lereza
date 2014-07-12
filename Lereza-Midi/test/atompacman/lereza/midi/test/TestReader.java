package atompacman.lereza.midi.test;

import atompacman.leraza.midi.MidiFileManager;
import atompacman.lereza.common.test.CommonTestSetup;

public class TestReader extends CommonTestSetup {

	private static final int NB_ARGS = 2;
	
	public static void main(String[] args) {
		if (!validArgs(args, NB_ARGS)) {
			return;
		}
		
		for (String path : args) {
			MidiFileManager.reader.read(path);
		}
	}
}
