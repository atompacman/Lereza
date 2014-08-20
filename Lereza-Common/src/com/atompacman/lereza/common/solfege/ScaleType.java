package com.atompacman.lereza.common.solfege;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ScaleType {	

	MAJOR 			(Arrays.asList(0, 2, 4, 5, 7, 9, 11)),

	MELODIC_MINOR 	(Arrays.asList(0, 2, 3, 5, 7, 9, 11)),

	HARMONIC_MINOR 	(Arrays.asList(0, 2, 3, 5, 7, 8, 11)),

	NATURAL_MINOR 	(Arrays.asList(0, 2, 3, 5, 7, 8, 10));


	private static final int NB_SEMITONES_NEEDED = Tone.NB_TONES_IN_OCTAVE - 1;
	private static final int NB_INTERVAL_IN_DEGREES = 4;
	
	private List<Degree> degrees;


	//------------ STATIC CONSTRUCTORS ------------\\

	private ScaleType(List<Integer> semitones) {
		degrees = new ArrayList<Degree>();
		if (semitones.size() != NB_SEMITONES_NEEDED) {
			throw new IllegalArgumentException("A scaletype must be built with " 
					+ NB_SEMITONES_NEEDED + " semitone values");
		}
		for (int i = 0; i < NB_SEMITONES_NEEDED; ++i) {
			List<Interval> intervals = new ArrayList<Interval>();

			for (int j = 0; j < NB_INTERVAL_IN_DEGREES; ++j) {
				int rootNoteSemitones = semitones.get(i);
				int intervalSemitones = semitones.get((i + 2*j) % NB_SEMITONES_NEEDED);
				int nbSemitonesInInterval = intervalSemitones - rootNoteSemitones;
				if (nbSemitonesInInterval < 0) {
					nbSemitonesInInterval += Tone.NB_SEMITONES_IN_OCTAVE;
				}
				intervals.add(Interval.fromSemitoneValue(nbSemitonesInInterval));
			}
			ScaleDegree degree = ScaleDegree.values()[i];
			
			degrees.add(new Degree(degree, ChordType.fromUnorderedIntervals(intervals)));
		}
	}


	//------------ GETTERS ------------\\

	public List<Degree> getDegrees() {
		return degrees;
	}


	//------------ CHORD TYPE OF ------------\\

	public ChordType chordTypeOf(ScaleDegree scaleDegree) {
		return degrees.get(scaleDegree.ordinal()).getChord();
	}
}
