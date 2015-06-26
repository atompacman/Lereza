package com.atompacman.lereza.core.profile.drum;

import java.util.ArrayList;
import java.util.List;

public class DrumTrack {

	//======================================= FIELDS =============================================\\

	private List<DrumPattern> patterns;
	private int				  firstBar;
	
	
		
	//======================================= METHODS ============================================\\

	//---------------------------------- PUBLIC CONSTRUCTOR --------------------------------------\\

	public DrumTrack(int firstBar) {
		this.firstBar = firstBar;
		this.patterns = new ArrayList<>();
	}


	//--------------------------------------- SETTERS --------------------------------------------\\

	public void addPattern(DrumPattern pattern) {
		this.patterns.add(pattern);
	}
	
	
	//--------------------------------------- GETTERS --------------------------------------------\\

	public List<DrumPattern> getPatterns() {
		return patterns;
	}

	public int getFirstBar() {
		return firstBar;
	}
}
