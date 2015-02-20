package com.atompacman.lereza.midi;

public class MIDINote {
	
	//======================================= FIELDS =============================================\\

	private int hexNote;
	private int velocity;
	private int length;
	private long startTick;
	
	
	//======================================= METHODS ============================================\\

	//---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\

	MIDINote(int hexNote, int velocity, long startTick) {
		this.hexNote 	= hexNote;
		this.velocity 	= velocity;
		this.startTick 	= startTick;
	}


	//--------------------------------------- SETTERS --------------------------------------------\\

	void setEndTick(long endTick) {
		this.length = (int) (endTick - startTick);
	}
	
	
	//--------------------------------------- GETTERS --------------------------------------------\\

	public int getHexNote() {
		return hexNote;
	}

	public int getVelocity() {
		return velocity;
	}
	
	public int getLength() {
		return length;
	}

	long startTick() {
		return startTick;
	}




	//--------------------------------------- EQUALS ---------------------------------------------\\
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + hexNote;
		result = prime * result + length;
		return result;
	}



	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MIDINote other = (MIDINote) obj;
		if (hexNote != other.hexNote)
			return false;
		if (length != other.length)
			return false;
		return true;
	}
}
