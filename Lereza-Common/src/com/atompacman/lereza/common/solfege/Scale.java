package com.atompacman.lereza.common.solfege;

import com.atompacman.lereza.common.solfege.quality.Quality;

public class Scale {

	private Tone tone;
	private ScaleType type;
	
	
	//------------ CONSTRUCTORS ------------\\

	public Scale(Tone tone, ScaleType type) {
		this.tone = tone;
		this.type = type;
	}
	
	
	//------------ GETTERS ------------\\

	public Tone getTone() {
		return tone;
	}
	
	public ScaleType getType() {
		return type;
	}
	
	public Key getKey() {
		return new Key(tone, getQuality());
	}
	
	public Quality getQuality() {
		return Quality.valueOf(type.name());
	}
}
