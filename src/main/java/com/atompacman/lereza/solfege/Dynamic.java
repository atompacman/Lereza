package com.atompacman.lereza.solfege;

public enum Dynamic {

	PIANISSISSIMO 	("ppp", 0),
	PIANISSIMO		("pp",	10),
	PIANO			("p",   30),
	MEZZO_PIANO		("mp",  50),
	MEZZO_FORTE		("mf",  64),
	FORTE			("f",   78),
	FORTISSIMO		("ff",  98),
	FORTISSISSIMO	("fff", 118);
	
	
	//======================================= FIELDS =============================================\\
	
	private String 	repres;
	private int 	minVelocity;
	
	
	
	//======================================= METHODS ============================================\\

	//------------------------------- PUBLIC STATIC CONSTRUCTOR ----------------------------------\\

	public static Dynamic valueOf(int hexVelocity) {
		if (hexVelocity < 0 || hexVelocity >= 128) {
			throw new IllegalArgumentException("Velocity must be between 0 and 128.");
		}
		for (Dynamic dyn : values()) {
			if (dyn.minVelocity >= hexVelocity) {
				return dyn;
			}
		}
		return null;
	}
	
	private Dynamic(String repres, int minVelocity) {
		this.repres = repres;
		this.minVelocity = minVelocity;
	}
	
	
	//-------------------------------------- TO STRING -------------------------------------------\\

	public String toString() {
		return repres;
	}
}
