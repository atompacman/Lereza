package com.atompacman.lereza.core.midi.sequence;

public class MIDINote {
	
	//======================================= FIELDS =============================================\\

	private final byte hexNote;
	private final byte velocity;
	private final long startTick;
	
	private int length;

	
	
	//======================================= METHODS ============================================\\

	//---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\

	MIDINote(byte hexNote, byte velocity, long startTick) {
		this.hexNote   = hexNote;
		this.velocity  = velocity;
		this.startTick = startTick;
		
	    this.length    = 0;
	}


	//--------------------------------------- SETTERS --------------------------------------------\\

	void setEndTick(long endTick) {
		this.length = (int) (endTick - startTick);
	}
	
	void setTimeunitLength(int timeunitLength) {
		length = timeunitLength;
	}
	
	
	//--------------------------------------- GETTERS --------------------------------------------\\

	public byte getHexNote() {
		return hexNote;
	}

	public byte getVelocity() {
		return velocity;
	}
	
	public int getLength() {
		return length;
	}

	public long startTick() {
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
