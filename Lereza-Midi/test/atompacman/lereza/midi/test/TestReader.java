package atompacman.lereza.midi.test;

import atompacman.leraza.midi.api.MidiFileReaderAPI;
import atompacman.leraza.midi.io.MidiFileReader;

public class TestReader {

	private static final int NB_ARGS = 2;
	
	public static void main(String[] args) {
		if (args.length != NB_ARGS) {
			throw new IllegalArgumentException("Received " + args.length + " arguments while " + NB_ARGS + " were needed.");
		}
		
		MidiFileReaderAPI reader = new MidiFileReader();
		
		for (String path : args) {
			reader.read(path);
		}
	}
}
