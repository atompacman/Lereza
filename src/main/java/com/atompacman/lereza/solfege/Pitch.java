package com.atompacman.lereza.solfege;

import com.atompacman.toolkat.construction.EnumRepresConstruc;

public class Pitch {
	
	private static EnumRepresConstruc<Pitch> enumRepresConstructor = 
			new EnumRepresConstruc<Pitch>(Pitch.class);

	private Tone tone;
	private Octave octave;


	//------------ PRIVATE CONSTRUCTOR ------------\\

	private Pitch(Tone note, Octave octave) {
		this.tone = note;
		this.octave = octave;
	}
	
	
	//------------ STATIC CONSTRUCTORS ------------\\

	public static Pitch valueOf(Tone tone, Octave octave) {
		return new Pitch(tone, octave);
	}
	
	public static Pitch valueOf(NoteLetter letter, Accidental accidental, Octave octave) {
		return new Pitch(Tone.valueOf(letter, accidental), octave);
	}
	
	public static Pitch valueOf(String repres) {
		return enumRepresConstructor.newInstance(repres);
	}

	public static Pitch thatIsMoreCommonForHexValue(byte hexNote) {
		Tone tone = Tone.thatIsMoreCommonForSemitoneValue(hexNote);
		Octave octave = Octave.fromHex(hexNote);
		return new Pitch(tone, octave);
	}
	
	
	//------------ GETTERS ------------\\

	public Tone getTone(){
		return tone;
	}

	public Octave getOctave(){
		return octave;
	}

	
	//------------ TONE / SEMITONE VALUE ------------\\

	public int semitoneValue() {
		return octave.semitoneValue() + tone.semitoneValue();
	}
	
	public int diatonicToneValue() {
		return octave.diatonicToneValue() + tone.diatonicToneValue();
	}

	
	//------------ STRING ------------\\

	public String toString() {
		return tone.toString() + octave.toString();
	}

	
	//------------ EQUALITIES ------------\\

	public int hashCode() {
		return semitoneValue();
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
