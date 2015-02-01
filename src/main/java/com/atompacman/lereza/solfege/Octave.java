package com.atompacman.lereza.solfege;

public enum Octave {

	ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE;


	//------------ STATIC CONSTRUCTORS ------------\\

	public static Octave fromInt(int octaveNumber) {
		if (octaveNumber < 0 || octaveNumber >= values().length) {
			throw new IllegalArgumentException("No octave number below 0 and above " + 
					values().length + " (got \"" + octaveNumber + "\").");
		}
		return values()[octaveNumber];
	}

	public static Octave fromHex(int hexValue) {
		int octaveNumber = (int) (hexValue / Semitones.IN_OCTAVE) - 1;
		
		if (octaveNumber < 0) {
			throw new IllegalArgumentException("Pitch hex value \"" + hexValue + "\" is too low to "
					+ "generate an octave number greater or equal to 0.");
		}
		if (octaveNumber >= values().length) {
			throw new IllegalArgumentException("Pitch hex value \"" + hexValue + "\" is too high "
					+ "to generate an octave number below " + values().length + ".");
		}
		return values()[octaveNumber];
	}


	//------------ VALUE ------------\\

	public int semitoneValue() {
		return ordinal() * Semitones.IN_OCTAVE;
	}

	public int diatonicToneValue() {
		return ordinal() * DiatonicTones.IN_OCTAVE;
	}


	//------------ STRING ------------\\

	public String toString() {
		return Integer.toString(ordinal());
	}
}
