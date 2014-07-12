package atompacman.lereza.api.manager;

import atompacman.leraza.midi.MidiFileManager;
import atompacman.leraza.midi.api.MidiFilePlayerAPI;
import atompacman.leraza.midi.api.MidiFileReaderAPI;
import atompacman.lereza.common.architecture.ManagerAPI;

public class MidiFileManagerAPI implements ManagerAPI {
	
	public MidiFileReaderAPI reader = MidiFileManager.reader;
	public MidiFilePlayerAPI player = MidiFileManager.player;
}
