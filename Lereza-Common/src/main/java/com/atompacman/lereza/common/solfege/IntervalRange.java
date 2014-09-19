package com.atompacman.lereza.common.solfege;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.lereza.common.solfege.quality.AdvancedQuality;
import com.atompacman.lereza.common.solfege.quality.IntervalQuality;
import com.atompacman.lereza.common.solfege.quality.Quality;

public enum IntervalRange {
	
	UNISON 			(0.0),
	SECOND 			(1.5),
	THIRD 			(3.5),
	FOURTH 			(5.0),
	FIFTH 			(7.0),
	SIXTH 			(8.5),
	SEVENTH 		(10.5),

	OCTAVE 			(12.0),
	NINTH 			(13.5), 
	TENTH 			(15.5), 
	ELEVENTH 		(17.0), 
	TWELVTH 		(19.0), 
	THIRTEENTH 		(20.5), 
	FOURTEENTH 		(22.5), 
	
	DOUBLE_OCTAVE 	(24.0);
	
	
	private double semitoneValue;
	private Class<? extends IntervalQuality> qualityType;
	
	
	//------------ CONSTRUCTORS ------------\\

	private IntervalRange(double semitoneValue) {
		this.semitoneValue = semitoneValue;
		boolean semitoneIsInt = (semitoneValue == (int) semitoneValue);
		this.qualityType = semitoneIsInt ? AdvancedQuality.class : Quality.class;
	}

	
	//------------ STATIC CONSTRUCTORS ------------\\

	public static List<IntervalRange> closestRangesFrom(int semitoneValue) {
		if (semitoneValue < 0) {
			throw new IllegalArgumentException("Semitone value must be positive.");
		}
		List<IntervalRange> closestRanges = new ArrayList<IntervalRange>();
		
		for (IntervalRange range : values()) {
			if (range.isWithinSemitoneRangeOf(semitoneValue)) {
				closestRanges.add(range);
			}
		}
		if (closestRanges.isEmpty()) {
			IntervalRange highest = IntervalRange.values()[IntervalRange.values().length - 1];
			throw new IllegalArgumentException("Semitone value must equal to or smaller than " + highest.semitoneValue() 
					+ " (" + highest.name() + ").");
		}
		
		return closestRanges;
	}
	
	
	//------------ GETTERS ------------\\

	public Class<? extends IntervalQuality> getQualityType() {
		return qualityType;
	}
	
	
	//------------ TONE / SEMITONE ------------\\
	
	public double semitoneValue() {
		return semitoneValue;
	}
	
	public int diatonicTonesValue() {
		return ordinal();
	}
	
	public boolean isWithinSemitoneRangeOf(int semitoneValue) {
		double semitoneDelta = Math.abs(this.semitoneValue - (double) semitoneValue);
		if (qualityType.equals(Quality.class)) {
			return semitoneDelta <= Quality.semitoneRadius();
		} else {
			return semitoneDelta <= AdvancedQuality.semitoneRadius();
		}
	}
	
	
	//------------ STRING ------------\\

	public String toString() {
		return name().toLowerCase();
	}
}