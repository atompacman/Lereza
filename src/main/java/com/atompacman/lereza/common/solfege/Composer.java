package com.atompacman.lereza.common.solfege;

import java.util.HashMap;
import java.util.Map;

public class Composer {

	private static Map<String, Composer> composers;
	
	static {
		composers = new HashMap<String, Composer>();
	}
	
	
	private String name;
	
	
	//------------ STATIC CONSTRUCTORS ------------\\
	
	public static Composer get(String name) {
		Composer composer = composers.get(name);
		if (composer == null) {
			composer = new Composer(name);
			composers.put(name, composer);
		}
		return composer;
	}
	
	
	//------------ CONSTRUCTORS ------------\\

	private Composer(String name) {
		this.name = name;
	}
	
	
	//------------ GETTERS ------------\\

	public String getName() {
		return name;
	}
}
