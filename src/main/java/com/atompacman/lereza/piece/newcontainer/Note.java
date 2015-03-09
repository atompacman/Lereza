package com.atompacman.lereza.piece.newcontainer;

import com.atompacman.lereza.solfege.Pitch;
import com.atompacman.lereza.solfege.Value;

public class Note {
	
	//======================================= FIELDS =============================================\\

	private final Pitch 	pitch;
	private final Value 	value;
	

	
	//======================================= METHODS ============================================\\
	
	//------------------------------ PUBLIC STATIC CONSTRUCTOR -----------------------------------\\
	
	public static Note valueOf(int hexPitch, Value value) {
		return new Note(Pitch.thatIsMoreCommonForHexValue(hexPitch), value);
	}
	
	public static Note valueOf(Pitch pitch, Value value) {
		return new Note(pitch, value);
	}
	

	//--------------------------------- PROTECTED CONSTRUCTOR ------------------------------------\\

	protected Note(Pitch pitch, Value value) {
		this.pitch = pitch;
		this.value = value;
	}
	
	
	//--------------------------------------- GETTERS --------------------------------------------\\

	public Pitch getPitch() {
		return pitch;
	}
	
	public Value getValue() {
		return value;
	}


	//-------------------------------------- TO STRING -------------------------------------------\\

	public String toCompleteString() {
		StringBuilder sb = new StringBuilder();
		sb.append('[').append(pitch.toString());
		sb.append('|').append(value.toString());
		sb.append(']');
		return sb.toString();
	}
	
	public String toString() {
		return pitch.toString();
	}


	//--------------------------------------- EQUALS ---------------------------------------------\\

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pitch == null) ? 0 : pitch.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Note other = (Note) obj;
		if (pitch == null) {
			if (other.pitch != null)
				return false;
		} else if (!pitch.equals(other.pitch))
			return false;
		if (value != other.value)
			return false;
		return true;
	}
}
