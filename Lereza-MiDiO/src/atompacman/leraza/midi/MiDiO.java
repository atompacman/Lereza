package atompacman.leraza.midi;

import atompacman.leraza.midi.device.MIDIFilePlayer;
import atompacman.leraza.midi.device.MIDIFileReader;
import atompacman.leraza.midi.device.MIDIFileWriter;

public class MiDiO {

	public static MIDIFileReader reader  = new MIDIFileReader();
	public static MIDIFilePlayer player   = new MIDIFilePlayer();
	public static MIDIFileWriter writer   = new MIDIFileWriter();
	

	public static void initialize() {
		player.initialize();
	}
}
