package com.atompacman.lereza.piece.container;

import com.atompacman.lereza.solfege.Articulation;
import com.atompacman.lereza.solfege.Pitch;
import com.atompacman.lereza.solfege.Value;

public class Note {
	
	private Pitch pitch;
	private Value value;
	
	private Articulation articulation;
	private boolean isTied;
	
	private boolean pitchConfirmed;

	
	//------------ PRIVATE CONSTRUCTOR ------------\\

	private Note(Pitch pitch, Value value, boolean isTied, Articulation articulation) {
		this.pitch = pitch;
		this.value = value;
		this.articulation = articulation;
		this.isTied = isTied;
		this.pitchConfirmed = false;
	}
	
	
	//------------ STATIC CONSTRUCTORS ------------\\
	
	public static Note valueOf(int hexValue, Value value) {
		Pitch pitch = Pitch.thatIsMoreCommonForHexValue(hexValue);
		return new Note(pitch, value, false, Articulation.NORMAL);
	}
	
	public static Note valueOf(int hexValue, Value value, boolean isTied) {
		Pitch pitch = Pitch.thatIsMoreCommonForHexValue(hexValue);
		return new Note(pitch, value, isTied, Articulation.NORMAL);
	}
	
	public static Note valueOf(Pitch pitch, Value value) {
		return new Note(pitch, value, false, Articulation.NORMAL);
	}
	
	public static Note valueOf(Pitch pitch, Value value, boolean isTied) {
		return new Note(pitch, value, isTied, Articulation.NORMAL);
	}
	
	public static Note valueOf(Pitch pitch, Value value, boolean isTied, Articulation articul) {
		return new Note(pitch, value, isTied, articul);
	}
	
	
	//------------ GETTERS ------------\\

	public Pitch getPitch() {
		return pitch;
	}
	
	public Value getValue() {
		return value;
	}
	
	public Articulation getArticulation() {
		return articulation;
	}
	
	
	//------------ OBSERVERS ------------\\

	public boolean isTied() {
		return isTied;
	}
	
	public boolean pitchConfirmed() {
		return pitchConfirmed;
	}
	
	
	//------------ SETTERS ------------\\

	public void setArticulation(Articulation articulation) {
		this.articulation = articulation;
	}
	
	public void confirmPitch(Pitch pitch) {
		if (pitch.semitoneValue() != this.pitch.semitoneValue()) {
			throw new IllegalArgumentException("Cannot confirm pitch " + toString() + " with pitch "
					+ pitch.toString() + " : Not the same semitone value.");
		}
		this.pitch = pitch;
		this.pitchConfirmed = true;
	}
	
	
	//------------ STRING ------------\\

	public String toString() {
		String output = pitch.toString();
		if (articulation == Articulation.STACCATO) {
			output += "·";
		}
		if (isTied) {
			output = "(" + output + ")";
		}
		return output;
	}


	//------------ EQUALITITES ------------\\

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((articulation == null) ? 0 : articulation.hashCode());
		result = prime * result + (isTied ? 1231 : 1237);
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
		if (articulation != other.articulation)
			return false;
		if (isTied != other.isTied)
			return false;
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
