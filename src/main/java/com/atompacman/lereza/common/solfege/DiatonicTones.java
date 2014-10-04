package com.atompacman.lereza.common.solfege;

public class DiatonicTones {

	public static final int IN_OCTAVE = 7;
	

	//------------ BETWEEN ------------\\

	public static int between(Tone a, Direction direction, Tone b) {
		int diatonicToneValueA = a.diatonicToneValue();
		int diatonicToneValueB = b.diatonicToneValue();

		switch (direction) {
		case ASCENDING:
			if (diatonicToneValueB < diatonicToneValueA) {
				diatonicToneValueB += IN_OCTAVE;
			}
			return diatonicToneValueB - diatonicToneValueA;
		case DESCENDING:
			if (diatonicToneValueB > diatonicToneValueA) {
				diatonicToneValueA += IN_OCTAVE;
			}
			return diatonicToneValueB - diatonicToneValueA;
		default:
			if (diatonicToneValueA != diatonicToneValueB) {
				throw new IllegalArgumentException("The STRAIGHT direction cannot be used "
						+ "between " + a + " and " + b + ".");
			}
			return 0;
		}
	}
	
	public static int between(Pitch a, Pitch b) {
		return b.diatonicToneValue() - a.diatonicToneValue(); 
	}

	
	//------------ NORMALIZE ------------\\

	public static int normalize(int diatonicToneValue) {
		diatonicToneValue %= DiatonicTones.IN_OCTAVE;
		if (diatonicToneValue < 0) {
			diatonicToneValue = DiatonicTones.IN_OCTAVE + diatonicToneValue;
		}
		return diatonicToneValue;
	}
}
