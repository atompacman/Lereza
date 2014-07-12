package atompacman.lereza.midi.test;

import atompacman.leraza.midi.MidiFileManager;
import atompacman.lereza.common.test.CommonTestSetup;

public class TestPlayer extends CommonTestSetup {
	
	private static final int NB_ARGS = 2;

	public static void main(String[] args) throws InterruptedException {
		if (!validArgs(args, NB_ARGS)) {
			return;
		}

		//for (String path : args) {
			MidiFileManager.reader.read(args[1]);
			MidiFileManager.player.playFile(args[1]);
		//}
	}
}
