package atompacman.leraza.midi;

import atompacman.atomLog.Log;
import atompacman.atomLog.Log.Verbose;
import atompacman.leraza.midi.device.MIDIFileBrowser;
import atompacman.leraza.midi.device.MIDIFilePlayer;
import atompacman.leraza.midi.device.MIDIFileWriter;
import atompacman.leraza.midi.exception.MIDIFileException;

public class MiDiO {

	public static MIDIFileBrowser browser  = new MIDIFileBrowser();
	public static MIDIFilePlayer  player   = new MIDIFilePlayer();
	public static MIDIFileWriter  writer   = new MIDIFileWriter();
	
	
	//////////////////////////////
	//           MAIN           //
	//////////////////////////////
	
	public static void main(String args[]) throws MIDIFileException {
		Log.setVerbose(Verbose.NORMAL);
		
		initialize();
		
		if (args.length == 0) {
			throw new MIDIFileException("ERROR: No path found in arguments.");
		}
		
		for (int i = 0; i < args.length; ++i) {
			browser.loadFile(args[i]);
		}
	}
	
	
	//////////////////////////////
	//        INITIALIZE        //
	//////////////////////////////
	
	public static void initialize() {
		player.initialize();
	}
}
