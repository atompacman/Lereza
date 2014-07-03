package atompacman.lereza.song.container;

import java.util.ArrayList;
import java.util.List;

import atompacman.atomLog.Log;
import atompacman.lereza.common.solfege.Genre;
import atompacman.lereza.common.solfege.Genre.BroadGenre;
import atompacman.lereza.common.solfege.Genre.Subgenre;

public class Set {
	
	private String setName;
	private Genre genre;
	private List<Composition> compositions;
	
	private static final String VARIOUS_COMPOSERS = "Various composers";
	
	
	//////////////////////////////
	//       CONSTRUCTORS       //
	//////////////////////////////
	
	public Set(String setName, Subgenre subgenre) {
		this.setName = setName;
		this.genre = new Genre(subgenre);
		this.compositions = new ArrayList<Composition>();
	}


	//////////////////////////////
	//     ADD COMPOSITION      //
	//////////////////////////////
	
	public void add(Composition composition) {
		this.compositions.add(composition);
		Log.infos("Composition \"" + composition.getName() + "\" add to set " + setName + ".");
	}
	
	
	//////////////////////////////
	//         GETTERS          //
	//////////////////////////////
	
	public String getName() {
		return setName;
	}

	public String getComposer() {
		if (compositions.isEmpty()) {
			Log.error("Cannot retrieve the composer of the set + " + setName + ": There are no composition in this set.");
		}
		String composer = compositions.get(0).getComposer();
		
		for (Composition composition : compositions) {
			if (!composer.equalsIgnoreCase(composition.getComposer())) {
				return VARIOUS_COMPOSERS;
			}
		}
		return composer;
	}
	
	public BroadGenre getBroadGenre() {
		return genre.getBroadGenre();
	}
	
	public Subgenre getSubgenre() {
		return genre.getSubGenre();
	}
	
	public int getNbComposition() {
		return compositions.size();
	}
}
