package atompacman.leraza.midi;

import atompacman.leraza.midi.device.MidiFilePlayer;
import atompacman.leraza.midi.device.MidiFileReader;
import atompacman.lereza.common.architecture.ManagerAPI;

public class MidiFileManager implements ManagerAPI {

	public static MidiFilePlayer player = new MidiFilePlayer();
	public static MidiFileReader reader = new MidiFileReader();
}
