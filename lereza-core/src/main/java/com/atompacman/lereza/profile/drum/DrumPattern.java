package com.atompacman.lereza.profile.drum;

import com.atompacman.lereza.profile.drum.punctuation.Punctuation;

public class DrumPattern {

	//======================================= FIELDS =============================================\\

	private final DrumBeat 		beat;
	private final int		 	nbRepet;
	private final Punctuation 	punctuation;
	private final DrumBeat		fill;
	
	
	
	//======================================= METHODS ============================================\\

	//---------------------------------- PUBLIC CONSTRUCTOR --------------------------------------\\

	public DrumPattern(DrumBeat beat, int nbRepet, Punctuation punctuation, DrumBeat fill) {
		this.beat = beat;
		this.nbRepet = nbRepet;
		this.punctuation = punctuation;
		this.fill = fill;
	}

	
	//--------------------------------------- GETTERS --------------------------------------------\\

	public DrumBeat getBeat() {
		return beat;
	}
	
	public int getNbRepet() {
		return nbRepet;
	}
	
	public Punctuation getPunctuation() {
		return punctuation;
	}
	
	public DrumBeat getFill() {
		return fill;
	}
}
