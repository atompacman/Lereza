package atompacman.lereza.core.menu;

import java.util.ArrayList;
import java.util.List;

import atompacman.atomLog.Log;
import atompacman.lereza.core.container.Set;

public class CompositionBrowser {

	private List<Set> compositionSets;

	
	//////////////////////////////
	//       CONSTRUCTOR        //
	//////////////////////////////
	
	public CompositionBrowser() {
		this.compositionSets = new ArrayList<Set>();
	}
	
	
	//////////////////////////////
	//    ADD COMPOSITION SET   //
	//////////////////////////////
	
	public void addCompositionSet(Set set) {
		this.compositionSets.add(set);
		Log.infos("Composition set \"" + set.getName() + "\" (" + set.getNbComposition() 
				+ " composition, " + set.getNbPieces() + " pieces) added.");
	}
}
