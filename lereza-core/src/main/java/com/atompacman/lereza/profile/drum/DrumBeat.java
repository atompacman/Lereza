package com.atompacman.lereza.profile.drum;

import java.util.HashMap;
import java.util.Map;

import com.atompacman.lereza.solfege.RythmicSignature;

public class DrumBeat {

	//======================================= FIELDS =============================================\\

	private RythmicSignature rythSign;
	private int				 nbBars;
	private int				 bpm;
	
	private Map<PercussionElement, PercussionPattern> patterns;

	
	
	//======================================= METHODS ============================================\\

	//---------------------------------- PUBLIC CONSTRUCTOR --------------------------------------\\

	public DrumBeat(RythmicSignature rythSign, int nbBars, int bpm) {
		this.rythSign 	= rythSign;
		this.nbBars 	= nbBars;
		this.bpm 		= bpm;
		this.patterns	= new HashMap<>();
	}
	
	
	//--------------------------------------- SETTERS --------------------------------------------\\

	public void addPattern(PercussionElement elem, PercussionPattern pattern) {
		if (patterns.put(elem, pattern) != null) {
			throw new IllegalArgumentException("A " + elem + " pattern "
					+ "was already added to this pattern.");
		}
	}
	
	
	//--------------------------------------- GETTERS --------------------------------------------\\

	public RythmicSignature getRythSign() {
		return rythSign;
	}

	public int getNbBars() {
		return nbBars;
	}

	public int getBpm() {
		return bpm;
	}

	
	public Map<PercussionElement, PercussionPattern> getPatterns() {
		return patterns;
	}
}
