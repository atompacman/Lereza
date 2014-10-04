package com.atompacman.lereza.common.solfege.quality;

public enum AdvancedQuality implements IntervalQuality {
	
	DIMINISHED ("dim", -1.0), PERFECT ("", 0.0), AUGMENTED ("aug", 1.0);
	
	
	private double semitoneModifier;
	private String representation;
	
	
	//------------ CONSTRUCTORS ------------\\
	
	private AdvancedQuality(String representation, double semiToneModifier) {
		this.semitoneModifier = semiToneModifier;
		this.representation = representation;
	}
	
	
	//------------ STATIC CONSTRUCTORS ------------\\

	public static AdvancedQuality parseQuality(String string) {
		for (AdvancedQuality advancedQuality : AdvancedQuality.values()) {
			if (string.equalsIgnoreCase(advancedQuality.toString())) {
				return advancedQuality;
			}
		}
		throw new IllegalArgumentException("\"" + string + "\" is not a valid string "
				+ "representation of an advanced quality.");
	}
	
	
	//------------ GETTERS ------------\\

	public double semitoneModifier() {
		return semitoneModifier;
	}
	
	public static double semitoneRadius() {
		return 1.0;
	}
	
	
	//------------ STRING ------------\\

	public String fullName() {
		return name().toLowerCase();
	}
	
	public String toString() {
		return representation;
	}
}