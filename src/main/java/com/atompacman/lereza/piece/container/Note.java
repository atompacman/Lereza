package com.atompacman.lereza.piece.container;

import com.atompacman.lereza.common.solfege.Articulation;
import com.atompacman.lereza.common.solfege.Octave;
import com.atompacman.lereza.common.solfege.Pitch;
import com.atompacman.lereza.common.solfege.Tone;
import com.atompacman.lereza.common.solfege.Value;

public class Note {
	
	private Pitch pitch;
	private Value value;
	
	private Articulation articulation;
	private boolean isTied;
	
	private boolean pitchConfirmed;

	
	//------------ CONSTRUCTORS ------------\\
	
	public Note(int hexValue, Value value) {
		this(guessPitch(hexValue), value, false);
	}
	
	public Note(int hexValue, Value value, boolean isTied) {
		this(guessPitch(hexValue), value, isTied);
	}
	
	public Note(Pitch pitch, Value value) {
		this(pitch, value, false, Articulation.NORMAL);
	}
	
	public Note(Pitch pitch, Value value, boolean isTied) {
		this(pitch, value, isTied, Articulation.NORMAL);
	}
	
	public Note(Pitch pitch, Value value, boolean isTied, Articulation articulation) {
		this.pitch = pitch;
		this.value = value;
		this.articulation = articulation;
		this.isTied = isTied;
		this.pitchConfirmed = false;
	}
	
	protected static Pitch guessPitch(int hexValue) {
		Tone tone = Tone.thatIsMoreCommonForSemitoneValue(hexValue);
		Octave octave = Octave.fromHex(hexValue);
		return new Pitch(tone, octave);
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
