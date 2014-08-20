package com.atompacman.lereza.common.solfege;

import com.atompacman.lereza.common.solfege.quality.AdvancedQuality;
import com.atompacman.lereza.common.solfege.quality.IntervalQuality;
import com.atompacman.lereza.common.solfege.quality.Quality;

public enum IntervalRange {
	
	UNISON 			(0.0,  AdvancedQuality.class),
	SECOND 			(1.5,  Quality.class),
	THIRD 			(3.5,  Quality.class),
	FOURTH 			(5.0,  AdvancedQuality.class),
	FIFTH 			(7.0,  AdvancedQuality.class),
	SIXTH 			(8.5,  Quality.class),
	SEVENTH 		(10.5, Quality.class),
	
	OCTAVE 			(12.0, AdvancedQuality.class),
	NINTH 			(13.5, Quality.class), 
	TENTH 			(15.5, Quality.class), 
	ELEVENTH 		(17.0, AdvancedQuality.class), 
	TWELVTH 		(19.0, AdvancedQuality.class), 
	THIRTEENTH 		(20.5, Quality.class), 
	FOURTEENTH 		(22.5, Quality.class), 
	
	DOUBLE_OCTAVE 	(24.0, AdvancedQuality.class);
	
	
	private double semitoneValue;
	private Class<? extends IntervalQuality> qualityType;
	
	
	//------------ CONSTRUCTORS ------------\\

	private IntervalRange(double semitoneValue, Class<? extends IntervalQuality> qualityType) {
		this.semitoneValue = semitoneValue;
		this.qualityType = qualityType;
	}

	
	//------------ GETTERS ------------\\

	public Class<? extends IntervalQuality> getQualityType() {
		return qualityType;
	}
	
	
	//------------ SEMITONE VALUE ------------\\
	
	public double semitoneValue() {
		return semitoneValue;
	}
	
	
	//------------ string representation ------------\\

	public String toString() {
		return name().toLowerCase();
	}
}