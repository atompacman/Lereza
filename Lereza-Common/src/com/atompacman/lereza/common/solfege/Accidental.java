package com.atompacman.lereza.common.solfege;

public enum Accidental {
	
	NONE  ("",   0), SHARP ("#",  1), FLAT  ("b", -1);
	
	
	private String representation;
	private int semitoneAlteration;
	
	
	//------------ CONSTRUCTORS ------------\\

	private Accidental(String representation, int semitoneAlteration) {
		this.representation = representation;
		this.semitoneAlteration = semitoneAlteration;
	}
	
	
	//------------ STATIC CONSTRUCTORS ------------\\

	public static Accidental parseAccidental(String string) {
		for (Accidental accidental : Accidental.values()) {
			if (string.equalsIgnoreCase(accidental.toString())) {
				return accidental;
			}
		}
		throw new IllegalArgumentException("\"" + string + "\" is not a valid string representation of an accidental.");
	}
	
	
	//------------ GETTERS ------------\\

	public int semitoneAlteration() {
		return semitoneAlteration;
	}
	
	
	//------------ STRING ------------\\

	public String toString() {
		return representation;
	}
}
