package atompacman.test;

import java.io.File;

import atompacman.atomLog.Log;
import atompacman.atomLog.Log.Verbose;
import atompacman.leraza.midi.MiDiO;
import atompacman.leraza.midi.container.MIDIFile;
import atompacman.lereza.core.builder.PieceFactory;
import atompacman.lereza.core.container.Composition;
import atompacman.lereza.core.container.Set;
import atompacman.lereza.core.container.form.fugue.Fugue;
import atompacman.lereza.core.container.piece.Piece;
import atompacman.lereza.core.menu.Session;
import atompacman.lereza.core.solfege.Genre;

public class Test {

	private static final String FILE_PATH = "..\\Data\\Fugue1.mid";
	
	public static void main(String args[]) {
		Log.setVerbose(Verbose.VITAL);
		listFiles();
		
		MiDiO.initialize();
		MIDIFile midiFile = MiDiO.reader.readFile(FILE_PATH);

		Log.setVerbose(Verbose.EXTRA);
		
		PieceFactory pieceFactory = new PieceFactory();
		pieceFactory.load(midiFile);
		Piece piece = pieceFactory.build(Fugue.class);
		
		Composition composition = new Composition("Test composition");
		composition.addFile(midiFile, piece);
		
		Set set = new Set("Test set", "Johann Sebastian Bach", Genre.BAROQUE.EARLY_BAROQUE);
		set.add(composition);
		
		Session session = new Session("Test session");
		session.addCompositionSet(set);
		
		Log.infos(session.getSessionName());
	}

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
}