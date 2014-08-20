package com.atompacman.lereza.common.solfege;

public enum Octave {
	
	ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE;
	
	
	//------------ STATIC CONSTRUCTORS ------------\\

	public static Octave fromInt(int integer) {
		return Octave.values()[integer];
	}
	
	public static Octave fromHex(int hexValue) {
		return Octave.values()[(int) hexValue / 12 - 1];
	}


	//------------ SEMITONE VALUE ------------\\

	public int semitoneValue() {
		return ordinal() * Tone.NB_SEMITONES_IN_OCTAVE;
	}
	
	
	//------------ STRING ------------\\

	public String toString() {
		return Integer.toString(ordinal());
	}
}
