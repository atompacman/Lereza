package com.atompacman.lereza.common.solfege;

import com.atompacman.lereza.common.helper.EnumRepresConstructor;

public class Pitch {
	
	private static EnumRepresConstructor<Pitch> enumRepresConstructor = new EnumRepresConstructor<Pitch>(Pitch.class);

	private Tone tone;
	private Octave octave;


	//------------ CONSTRUCTORS ------------\\

	public Pitch(Tone note, Octave octave) {
		this.tone = note;
		this.octave = octave;
	}
	
	public Pitch(NoteLetter letter, Accidental accidental, Octave octave) {
		this.tone = new Tone(letter, accidental);
		this.octave = octave;
	}

	
	//------------ STATIC CONSTRUCTORS ------------\\

	public static Pitch valueOf(String repres) {
		return enumRepresConstructor.newInstance(repres);
	}

	
	//------------ GETTERS ------------\\

	public Tone getTone(){
		return tone;
	}

	public Octave getOctave(){
		return octave;
	}

	
	//------------ TONE /  SEMITONE ------------\\

	public int semitoneValue() {
		return octave.semitoneValue() + tone.semitoneValue();
	}
	
	
	//------------ HEIGHT FOR PARTITION ------------\\

	public int heightForPartition() {
		return tone.getNote().ordinal() + octave.ordinal() * 7;
	}

	
	//------------ STRING ------------\\

	public String toString() {
		return tone.toString() + octave.toString();
	}

	
	//------------ EQUALITIES ------------\\

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((octave == null) ? 0 : octave.hashCode());
		result = prime * result + ((tone == null) ? 0 : tone.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pitch other = (Pitch) obj;
		if (octave != other.octave)
			return false;
		if (tone == null) {
			if (other.tone != null)
				return false;
		} else if (!tone.equals(other.tone))
			return false;
		return true;
	}
}
