package atompacman.lereza.core.menu;

import java.util.Date;

import atompacman.leraza.midi.device.MIDIFilePlayer;
import atompacman.leraza.midi.device.MIDIFileReader;
import atompacman.leraza.midi.device.MIDIFileWriter;
import atompacman.lereza.core.builder.PieceFactory;

public class LerezaWizard {

	public static CompositionBrowser compositionBrowser = new CompositionBrowser();
	public static MIDIFileReader midiFileReader = new MIDIFileReader();
	public static MIDIFilePlayer midiFilePlayer = new MIDIFilePlayer();
	public static MIDIFileWriter midiFileWriter = new MIDIFileWriter();
	public static PieceFactory pieceFactory = new PieceFactory();
	
	private static Date wizardInitializationTime;
	
	/**
	 * Initializes every modules required for the Lereza pipeline to work properly.
	 */
	public static void initialize() {
		wizardInitializationTime = new Date();
		midiFilePlayer.initialize();
	}
}