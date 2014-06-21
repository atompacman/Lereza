package atompacman.lereza.core.menu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import atompacman.atomLog.Log;
import atompacman.lereza.core.container.Set;

public class Session {

	private String sessionName;
	private List<Set> compositionSets;
	private Date timestamp;
	
	//////////////////////////////
	//       CONSTRUCTORS       //
	//////////////////////////////
	
	public Session(String sessionName) {
		this.sessionName = sessionName;
		this.compositionSets = new ArrayList<Set>();
		this.timestamp = new Date();
		Log.infos("Lereza Session \"" + sessionName + "\" started at " + getTimestamp() + ".");
	}
	
	
	//////////////////////////////
	//    ADD COMPOSITION SET   //
	//////////////////////////////
	
	public void addCompositionSet(Set set) {
		this.compositionSets.add(set);
		Log.infos("Composition set \"" + set.getName() + "\" (" + set.getNbComposition() 
				+ " composition, " + set.getNbPieces() + " pieces) added to session \"" + sessionName + "\".");
	}


	//////////////////////////////
	//         GETTERS          //
	//////////////////////////////
	
	public String getSessionName() {
		return sessionName;
	}
	
	public String getTimestamp() {
		SimpleDateFormat format = new SimpleDateFormat();
		return format.format(timestamp);
	}
}
