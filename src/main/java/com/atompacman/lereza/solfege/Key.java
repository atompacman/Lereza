package com.atompacman.lereza.solfege;

import java.util.List;

import com.atompacman.lereza.solfege.quality.Quality;
import com.atompacman.toolkat.construction.EnumRepresConstruc;

public class Key {
	
	private static EnumRepresConstruc<Key> enumRepresConstructor = 
			new EnumRepresConstruc<Key>(Key.class);
	
	private Tone tone;
	private Quality quality;
	
	
	//------------ PRIVATE CONSTRUCTOR ------------\\

	private Key(Tone tone, Quality quality) {
		this.tone = tone;
		this.quality = quality;
	}
	
	
	//------------ STATIC CONSTRUCTORS ------------\\
	
	public static Key valueOf(Tone tone) {
		return new Key(tone, Quality.MAJOR);
	}
	
	public static Key valueOf(Tone tone, Quality quality) {
		return new Key(tone, quality);
	}
	
	public static Key valueOf(NoteLetter letter, Accidental accidental, Quality quality) {
		return new Key(Tone.valueOf(letter, accidental), quality);
	}
	
	public static Key valueOf(String repres) {
		return enumRepresConstructor.newInstance(repres);
	}
	
	
	//------------ GETTERS ------------\\

	public Tone getTone() {
		return tone;
	}
	
	public Quality getQuality() {
		return quality;
	}

	
	//------------ CORRESPONDING SCALE ------------\\
	
	public Scale correspondingScale() {
		return Scale.valueOf(tone, ScaleType.valueOf(quality));
	}
	
	
	//------------ CONTAINS ------------\\

	public boolean contains(Tone tone) {
		return correspondingScale().contains(tone);
	}
	
	public boolean contains(List<Tone> tones) {
		for (Tone tone : tones) {
			if (!correspondingScale().contains(tone)) {
				return false;
			}
		}
		return true;
	}
	
	
	//------------ ARMOR ------------\\
	
	public Accidental accidental() {
		return CircleOfFifths.accidentalOfKey(this);
	}
	
	public int nbAccidentals() {
		return CircleOfFifths.nbAccidentalsOfKey(this);
	}

	
	//------------ PARALLEL KEY ------------\\

	public Key parallelKey() {
		Direction directionOfMinorThird = null;
		Quality quality;
		
		if (this.quality == Quality.MINOR) {
			directionOfMinorThird = Direction.ASCENDING;
			quality = Quality.MAJOR;
		} else {
			directionOfMinorThird = Direction.DESCENDING;
			quality = Quality.MINOR;
		}
		
		Interval intervalToParallel = Interval.valueOf(directionOfMinorThird, 
				Quality.MINOR, IntervalRange.THIRD);
		
		return new Key(tone.afterInterval(intervalToParallel), quality);
	}
	
	
	//------------ STRING ------------\\

	public String toString() {
		return tone.toString() + " " + quality.name().toLowerCase();
	}


	//------------ EQUALITIES ------------\\

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((quality == null) ? 0 : quality.hashCode());
		result = prime * result + ((tone == null) ? 0 : tone.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Key other = (Key) obj;
		if (quality != other.quality)
			return false;
		if (tone == null) {
			if (other.tone != null)
				return false;
		} else if (!tone.equals(other.tone))
			return false;
		return true;
	}
}
