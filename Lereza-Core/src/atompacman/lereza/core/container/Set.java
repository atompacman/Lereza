package atompacman.lereza.core.container;

import java.util.ArrayList;
import java.util.List;

import atompacman.atomLog.Log;
import atompacman.lereza.core.solfege.Genre;
import atompacman.lereza.core.solfege.Genre.BroadGenre;
import atompacman.lereza.core.solfege.Genre.Subgenre;

public class Set {
	
	private String setName;
	private String composer;
	private Genre genre;
	private List<Composition> compositions;
	
	
	//////////////////////////////
	//       CONSTRUCTORS       //
	//////////////////////////////
	
	public Set(String setName, String composer, Subgenre subgenre) {
		this.setName = setName;
		this.composer = composer;
		this.genre = new Genre(subgenre);
		this.compositions = new ArrayList<Composition>();
	}


	//////////////////////////////
	//     ADD COMPOSITION      //
	//////////////////////////////
	
	public void add(Composition composition) {
		this.compositions.add(composition);
		Log.infos("Composition \"" + composition.getCompositionName() + "\" add to set " + setName + ".");
	}
	
	
	//////////////////////////////
	//         GETTERS          //
	//////////////////////////////
	
	public String getName() {
		return setName;
	}

	public String getComposer() {
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
	
	public int getNbPieces() {
		int nbPieces = 0;
		for (Composition composition : compositions) {
			nbPieces += composition.getNbPieces();
		}
		return nbPieces;
	}
}
