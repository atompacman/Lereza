package com.atompacman.lereza.core.profile.drum.punctuation;

public class SilencePunctuation implements Punctuation {

	//======================================= FIELDS =============================================\\

	private final int begTU;
	private final int endTU;
	
	
	
	//======================================= METHODS ============================================\\

	//---------------------------------- PUBLIC CONSTRUCTOR --------------------------------------\\

	public SilencePunctuation(int begTU, int endTU) {
		super();
		this.begTU = begTU;
		this.endTU = endTU;
	}
	
	
	//--------------------------------------- GETTERS --------------------------------------------\\

	public int getBegTU() {
		return begTU;
	}
	
	public int getEndTU() {
		return endTU;
	}
}
