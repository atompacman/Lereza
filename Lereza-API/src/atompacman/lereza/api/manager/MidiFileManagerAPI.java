package atompacman.lereza.api.manager;

import atompacman.leraza.midi.MidiFileManager;
import atompacman.leraza.midi.api.MidiFilePlayerAPI;
import atompacman.leraza.midi.api.MidiFileReaderAPI;

public class MidiFileManagerAPI {
	
	public MidiFileReaderAPI reader = MidiFileManager.reader;
	public MidiFilePlayerAPI player = MidiFileManager.player;
}
