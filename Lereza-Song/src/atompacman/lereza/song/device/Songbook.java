package atompacman.lereza.song.device;

import java.util.ArrayList;
import java.util.List;

import atompacman.atomLog.Log;
import atompacman.leraza.midi.MidiFileManager;
import atompacman.lereza.common.formatting.Formatting;
import atompacman.lereza.common.solfege.Genre.Subgenre;
import atompacman.lereza.song.SongManager;
import atompacman.lereza.song.api.SongbookAPI;
import atompacman.lereza.song.container.Composition;
import atompacman.lereza.song.container.Set;

public class Songbook implements SongbookAPI {

	private List<Set> compositionSets;

	private static final String DEFAULT_SET_NAME = "Default set";
	private static final String DEFAULT_COMPOSER_NAME = "Defaulf composer";
	
	
	//////////////////////////////
	//       CONSTRUCTOR        //
	//////////////////////////////

	public Songbook() {
		this.compositionSets = new ArrayList<Set>();
		newCompositionSet(DEFAULT_SET_NAME, null);
	}


	//////////////////////////////
	//         ADD SONG         //
	//////////////////////////////

	public void addSongToSet(String songName, String composer, String setName, String filePath) {
		Set set = getCompositionSet(setName);
		if (set == null) {
			Log.error("The songbook does not contain a set named \"" + setName + "\".");
			return;
		}
		set.add(new Composition(songName, composer, SongManager.pieceBuilder.getPiece(filePath), MidiFileManager.reader.getMidiFile(filePath)));
	}

	public void addSong(String songName, String composer, String filePath) {
		addSongToSet(songName, composer, DEFAULT_SET_NAME, filePath);
	}

	public void addSong(String songName, String filePath) {
		addSongToSet(songName, DEFAULT_COMPOSER_NAME, DEFAULT_SET_NAME, filePath);
	}

	//////////////////////////////
	//    ADD COMPOSITION SET   //
	//////////////////////////////

	public void newCompositionSet(String setName, Subgenre subgenre) {
		Log.infos(Formatting.lineSeparation("Songbook", 0));
		if (containsSetNamed(setName)) {
			Log.error("The songbook already contains a set named \"" + setName + "\".");
			return;
		}
		Set set = new Set(setName, subgenre);
		this.compositionSets.add(set);
		Log.infos("Composition set \"" + set.getName() + "\" added.");
	}
	

	//////////////////////////////
	//      PRIVATE METHODS     //
	//////////////////////////////

	private Set getCompositionSet(String setName) {
		for (Set set : compositionSets) {
			if (set.getName().equalsIgnoreCase(setName)) {
				return set;
			}
		}
		return null;
	}

	private boolean containsSetNamed(String setName) {
		if (getCompositionSet(setName) == null) {
			return false;
		}
		return true;
	}
}
