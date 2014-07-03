package atompacman.lereza.midi.test;

import atompacman.leraza.midi.MidiFileManager;

public class TestPlayer extends TestSetup {
	
	private static final int NB_ARGS = 2;

	public static void main(String[] args) throws InterruptedException {
		if (!validArgs(args, NB_ARGS)) {
			return;
		}

		//for (String path : args) {
			MidiFileManager.reader.read(args[1]);
			MidiFileManager.player.playMIDIFile(args[1]);
		//}
	}
}
