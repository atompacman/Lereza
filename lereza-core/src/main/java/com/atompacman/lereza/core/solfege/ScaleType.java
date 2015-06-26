package com.atompacman.lereza.core.solfege;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ScaleType {	

	MAJOR 			(Arrays.asList(0, 2, 4, 5, 7, 9, 11)),

	MELODIC_MINOR 	(Arrays.asList(0, 2, 3, 5, 7, 9, 11)),

	HARMONIC_MINOR 	(Arrays.asList(0, 2, 3, 5, 7, 8, 11)),

	NATURAL_MINOR 	(Arrays.asList(0, 2, 3, 5, 7, 8, 10));

	//TODO put that back at 4 one everything in the package works + double flats and sharps implem.
	private static final int NB_INTERVAL_IN_DEGREES = 3;
	
	private List<Degree> degrees;
	private List<Integer> degreeSemitones;
	

	//------------ PRIVATE CONSTRUCTOR ------------\\

	private ScaleType(List<Integer> semitones) {
		this.degreeSemitones = semitones;
		
		if (degreeSemitones.size() != DiatonicTones.IN_OCTAVE) {
			throw new IllegalArgumentException("A scaletype must be built with " 
					+ DiatonicTones.IN_OCTAVE + " semitone values");
		}
		degrees = new ArrayList<Degree>();
		
		for (int i = 0; i < DiatonicTones.IN_OCTAVE; ++i) {
			List<Interval> intervals = new ArrayList<Interval>();

			for (int j = 0; j < NB_INTERVAL_IN_DEGREES; ++j) {
				int rootNoteSemitones = degreeSemitones.get(i);
				int intervalSemitones = degreeSemitones.get(DiatonicTones.normalize(i + 2*j));
				int semitoneDelta = intervalSemitones - rootNoteSemitones;
				int diatonicToneDelta = 2*j;
				if (semitoneDelta < 0) {
					semitoneDelta += Semitones.IN_OCTAVE;
				}
				intervals.add(Interval.valueOf(semitoneDelta, diatonicToneDelta));
			}
			ScaleDegree degree = ScaleDegree.values()[i];
			
			degrees.add(Degree.valueOf(degree, ChordType.valueOf(intervals)));
		}
	}


	//------------ STATIC CONSTRUCTORS ------------\\

	public static ScaleType valueOf(Quality quality) {
		if (quality == Quality.MAJOR) {
			return MAJOR;
		} else if (quality == Quality.MINOR) {
			return NATURAL_MINOR;
		}
		throw new IllegalArgumentException("\"" + quality.toString() + "\" is not a valid quality for a scaletype.");
	}
	
	
	//------------ INTERVALS ------------\\

	public Interval intervalFromRootTo(ScaleDegree degree) {
		int semitoneDelta = degreeSemitones.get(degree.ordinal());
		int diatonicToneDelta = degree.ordinal();
		return Interval.valueOf(semitoneDelta, diatonicToneDelta);
	}
	
	
	//------------ GETTERS ------------\\

	public Degree getDegree(ScaleDegree degree) {
		return degrees.get(degree.ordinal());
	}
}
