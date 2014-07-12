package atompacman.lereza.common.solfege;

public enum Octave {
	
	ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE;
	
	
	public static Octave getOctaveFormHex(int hexValue) {
		return Octave.values()[(int) hexValue / 12 - 1];
	}
}
