package atompacman.test;

import atompacman.atomLog.Log;
import atompacman.atomLog.Log.Verbose;
import atompacman.leraza.midi.container.MIDIFile;
import atompacman.lereza.core.container.Composition;
import atompacman.lereza.core.container.Set;
import atompacman.lereza.core.container.form.fugue.Fugue;
import atompacman.lereza.core.container.piece.Piece;
import atompacman.lereza.core.menu.CompositionBrowser;
import atompacman.lereza.core.menu.LerezaWizard;
import atompacman.lereza.core.solfege.Genre;

public class Test {

	private static final String FILE_PATH = "..\\Data\\Bach\\Vol.10 - Keyboard Works II\\CD.10 - Concerto Arrangements For Harpsichord I - After Vivaldi\\Concerto In D, BWV 972 - I - Allegro.mid";
	//private static final String FILE_PATH = "..\\Data\\Fugue1.mid";
	
	
	public static void main(String args[]) {
		Log.setVerbose(Verbose.EXTRA);
		LerezaWizard.initialize();
		
		MIDIFile midiFile = LerezaWizard.midiFileReader.readFile(FILE_PATH);

		Log.setVerbose(Verbose.EXTRA);
		
		Piece piece = LerezaWizard.pieceFactory.build(midiFile, Fugue.class);
		
		Composition composition = new Composition("Test composition");
		composition.addFile(midiFile, piece);
		
		Set set = new Set("Test set", "Johann Sebastian Bach", Genre.BAROQUE.EARLY_BAROQUE);
		set.add(composition);
		
		CompositionBrowser session = new CompositionBrowser();
		session.addCompositionSet(set);
	}

	/**
	private static void listFiles() {
		File folder = new File("..\\Data");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				Log.infos("File " + listOfFiles[i].getName());
			} else if (listOfFiles[i].isDirectory()) {
				Log.infos("Directory " + listOfFiles[i].getName());
			}
		}
	}
	**/
}