package com.atompacman.lereza.common.solfege;

public class DiatonicTones {

	public static final int IN_OCTAVE = 7;
	

	//------------ BETWEEN ------------\\

	public static int between(Tone a, Tone b) {
		return b.getNote().ordinal() - a.getNote().ordinal();
	}
	
	public static int between(Pitch a, Pitch b) {
		int octaveDelta = b.getOctave().ordinal() - a.getOctave().ordinal();
		int octaveTones = octaveDelta * DiatonicTones.IN_OCTAVE;
		int tonesWithinOctave = between(a.getTone(), b.getTone());
		return octaveTones + tonesWithinOctave; 
	}

	
	//------------ NORMALIZE ------------\\

	public static int normalize(int diatonicToneValue) {
		diatonicToneValue %= DiatonicTones.IN_OCTAVE;
		if (diatonicToneValue < 0) {
			diatonicToneValue = DiatonicTones.IN_OCTAVE - diatonicToneValue;
		}
		return diatonicToneValue;
	}
}
