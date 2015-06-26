package com.atompacman.lereza.core.solfege;

public enum Quality implements IntervalQuality {
	
	MINOR ("m", -0.5), MAJOR ("", 0.5);
	
	
	private double semitoneModifier;
	private String representation;
	
	
	//------------ PRIVATE CONSTRUCTOR ------------\\

	private Quality(String representation, double semiToneModifier) {
		this.semitoneModifier = semiToneModifier;
		this.representation = representation;
	}
	
	
	//------------ STATIC CONSTRUCTORS ------------\\

	public static Quality parseQuality(String string) {
		for (Quality quality : Quality.values()) {
			if (string.equalsIgnoreCase(quality.toString())) {
				return quality;
			}
		}
		throw new IllegalArgumentException("\"" + string + "\" is not a valid string "
				+ "representation of a quality.");
	}
	
	
	//------------ GETTERS ------------\\

	public double semitoneModifier() {
		return semitoneModifier;
	}
	
	public static double semitoneRadius() {
		return 0.5;
	}
	
	
	//------------ STRING ------------\\

	public String fullName() {
		return name().toLowerCase();
	}

	public String toString() {
		return representation;
	}
}