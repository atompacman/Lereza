package com.atompacman.lereza.common.solfege;

import java.util.List;

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
		if (type.name().contains(Quality.MAJOR.name())) {
			return Quality.MAJOR;
		} else if (type.name().contains(Quality.MINOR.name())) {
			return Quality.MINOR;
		}
		throw new RuntimeException("Scaletype is neither major nor minor.");
	}

	
	//------------ CONTAINS ------------\\

	public boolean contains(Tone tone) {
		//TODO
		boolean todo;
		return false;
	}
	
	public boolean contains(List<Tone> tones) {
		for (Tone tone : tones) {
			if (!contains(tone)) {
				return false;
			}
		}
		return true;
	}
	
	
	//------------ EQUALITIES ------------\\
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tone == null) ? 0 : tone.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Scale other = (Scale) obj;
		if (tone == null) {
			if (other.tone != null)
				return false;
		} else if (!tone.equals(other.tone))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
}
