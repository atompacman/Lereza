package com.atompacman.lereza.common.solfege;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.atompacman.lereza.common.solfege.quality.Quality;

public enum ScaleType {	

	MAJOR 			(Arrays.asList(0, 2, 4, 5, 7, 9, 11)),

	MELODIC_MINOR 	(Arrays.asList(0, 2, 3, 5, 7, 9, 11)),

	HARMONIC_MINOR 	(Arrays.asList(0, 2, 3, 5, 7, 8, 11)),

	NATURAL_MINOR 	(Arrays.asList(0, 2, 3, 5, 7, 8, 10));


	private static final int NB_SEMITONES_NEEDED = DiatonicTones.IN_OCTAVE - 1;
	private static final int NB_INTERVAL_IN_DEGREES = 4;
	
	private List<Degree> degrees;
	private List<Integer> degreeSemitones;
	

	//------------ CONSTRUCTORS ------------\\

	private ScaleType(List<Integer> semitones) {
		this.degreeSemitones = semitones;
		
		if (degreeSemitones.size() != NB_SEMITONES_NEEDED) {
			throw new IllegalArgumentException("A scaletype must be built with " 
					+ NB_SEMITONES_NEEDED + " semitone values");
		}
		degrees = new ArrayList<Degree>();
		
		for (int i = 0; i < NB_SEMITONES_NEEDED; ++i) {
			List<Interval> intervals = new ArrayList<Interval>();

			for (int j = 0; j < NB_INTERVAL_IN_DEGREES; ++j) {
				int rootNoteSemitones = degreeSemitones.get(i);
				int intervalSemitones = degreeSemitones.get((i + 2*j) % NB_SEMITONES_NEEDED);
				int semitoneDelta = intervalSemitones - rootNoteSemitones;
				int diatonicToneDelta = 2*j;
				if (semitoneDelta < 0) {
					semitoneDelta += Semitones.IN_OCTAVE;
				}
				intervals.add(Interval.between(semitoneDelta, diatonicToneDelta));
			}
			ScaleDegree degree = ScaleDegree.values()[i];
			
			degrees.add(new Degree(degree, ChordType.fromIntervals(intervals)));
		}
	}


	//------------ STATIC CONSTRUCTORS ------------\\

	public static ScaleType fromQuality(Quality quality) {
		if (quality == Quality.MAJOR) {
			return MAJOR;
		} else if (quality == Quality.MINOR) {
			return NATURAL_MINOR;
		}
		throw new IllegalArgumentException("\"" + quality.toString() + "\" is not a valid quality for a scaletype.");
	}
	
	
	//------------ INTERVAL OF DEGREE ------------\\

	public Interval intervalFromRootTo(ScaleDegree degree) {
		int semitoneDelta = degreeSemitones.get(degree.ordinal());
		int diatonicToneDelta = degree.ordinal();
		return Interval.between(semitoneDelta, diatonicToneDelta);
	}
	
	
	//------------ GETTERS ------------\\

	public Degree getDegree(ScaleDegree degree) {
		return degrees.get(degree.ordinal());
	}
}
