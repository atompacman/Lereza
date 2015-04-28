package com.atompacman.lereza.solfege;

public enum Articulation {
	
	NORMAL(""), STACCATO("·");
	
	
	private String representation;
	
	
	//------------ PRIVATE CONSTRUCTOR ------------\\

	private Articulation(String representation) {
		this.representation = representation;
	}
	
	
	//------------ STATIC CONSTRUCTORS ------------\\

	public static Articulation parseArticulation(String string) {
		for (Articulation articulation : Articulation.values()) {
			if (string.equalsIgnoreCase(articulation.toString())) {
				return articulation;
			}
		}
		throw new IllegalArgumentException("\"" + string + "\" is not a valid string "
				+ "representation of an articulation.");
	}
	
	
	//------------ STRING ------------\\

	public String toString() {
		return representation;
	}
}
