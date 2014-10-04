package com.atompacman.lereza.common.solfege;

public enum Direction {
	
	ASCENDING (1.0), STRAIGHT (0), DESCENDING (-1.0);
	
	
	private double semitoneMultiplier;

	
	//------------ CONSTRUCTORS ------------\\

	private Direction(double semitoneMultiplier) {
		this.semitoneMultiplier = semitoneMultiplier;
	}


	//------------ GETTERS ------------\\

	public double semitoneMultiplier() {
		return semitoneMultiplier;
	}
	
	
	//------------ STRING ------------\\

	public String toString() {
		return name().toLowerCase();
	}
}