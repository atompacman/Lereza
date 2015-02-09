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
}
