package com.atompacman.lereza.common.solfege;

public class Semitones {

	public static final int IN_OCTAVE = 12;

	
	//------------ BETWEEN ------------\\

	public static int between(Tone a, Tone b) {
		return b.semitoneValue() - a.semitoneValue();
	}
	
	public static int between(Pitch a, Pitch b) {
		return b.semitoneValue() - a.semitoneValue();
	}


	//------------ NORMALIZE ------------\\

	public static int normalize(int semitoneValue) {
		semitoneValue %= Semitones.IN_OCTAVE;
		if (semitoneValue < 0) {
			semitoneValue = Semitones.IN_OCTAVE - semitoneValue;
		}
		return semitoneValue;
	}
}
