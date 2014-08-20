package atompacman.lereza.midi.test;

import atompacman.leraza.midi.api.MidiFileReaderAPI;
import atompacman.leraza.midi.io.MidiFilePlayer;
import atompacman.leraza.midi.io.MidiFileReader;

public class TestPlayer {
	
	private static final int NB_ARGS = 2;

	public static void main(String[] args) throws InterruptedException {
		if (args.length != NB_ARGS) {
			throw new IllegalArgumentException("Received " + args.length + " arguments while " + NB_ARGS + " were needed.");
		}
		
		MidiFileReaderAPI reader = new MidiFileReader();
		
		for (String path : args) {
			reader.read(path);
			MidiFilePlayer.getPlayer().playFile(path);
		}
	}
}
