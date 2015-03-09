package com.atompacman.lereza.piece.newcontainer;

import com.atompacman.lereza.solfege.Dynamic;
import com.atompacman.lereza.solfege.Pitch;
import com.atompacman.lereza.solfege.Value;

public class Note {
	
	//======================================= FIELDS =============================================\\

	private final Pitch 	pitch;
	private final Value 	value;
	private final Dynamic 	dynamic;
	

	
	//======================================= METHODS ============================================\\
	
	//------------------------------ PUBLIC STATIC CONSTRUCTOR -----------------------------------\\
	
	public static Note valueOf(int hexPitch, Value value, int hexVelocity) {
		Pitch pitch = Pitch.thatIsMoreCommonForHexValue(hexPitch);
		Dynamic dynamic = Dynamic.valueOf(hexVelocity);
		return new Note(pitch, value, dynamic);
	}
	
	public static Note valueOf(Pitch pitch, Value value, Dynamic dynamic) {
		return new Note(pitch, value, dynamic);
	}
	

	//--------------------------------- PROTECTED CONSTRUCTOR ------------------------------------\\

	protected Note(Pitch pitch, Value value, Dynamic dynamic) {
		this.pitch = pitch;
		this.value = value;
		this.dynamic = dynamic;
	}
	
	
	//--------------------------------------- GETTERS --------------------------------------------\\

	public Pitch getPitch() {
		return pitch;
	}
	
	public Value getValue() {
		return value;
	}

	public Dynamic getDynamic() {
		return dynamic;
	}
	

	//-------------------------------------- TO STRING -------------------------------------------\\

	public String toCompleteString() {
		StringBuilder sb = new StringBuilder();
		sb.append('[').append(pitch.toString());
		sb.append('|').append(value.toString());
		sb.append('|').append(dynamic.toString());
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
