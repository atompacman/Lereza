package com.atompacman.lereza.solfege;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atompacman.lereza.solfege.quality.AdvancedQuality;
import com.atompacman.lereza.solfege.quality.IntervalQuality;
import com.atompacman.lereza.solfege.quality.Quality;

public class ChordType {

	private static final int MAX_INTERVAL_NUMBER = 13;
	
	private Map<IntervalRange, Interval> intervals;


	//------------ PRIVATE CONSTRUCTOR ------------\\

	private ChordType(Map<IntervalRange, Interval> intervals) {
		this.intervals = intervals;
	}
	
	
	//------------ STATIC CONSTRUCTORS ------------\\

	public static ChordType valueOf(List<Interval> intervals) {
		Map<IntervalRange, Interval> chordIntervals = new HashMap<IntervalRange, Interval>();
		for (Interval interval : intervals) {
			chordIntervals.put(interval.getIntervalRange(), interval);
		}
		return new ChordType(chordIntervals);
	}
	
	public static ChordType valueOf(String repres) {
		if (repres == null) {
			throw new NullPointerException("String representation can't be null.");
		}
		
		String chordQuality = extractChordQuality(repres);
		String intervalQuality = extractIntervalQuality(repres);
		String suspension = extractSuspension(repres);
		String addedTone = extractAddedTones(repres);
		
		verifyExtractedFeatures(repres, chordQuality, intervalQuality, suspension, addedTone);
		
		ChordType chord = new ChordType(new HashMap<IntervalRange, Interval>());

		chord.add(IntervalRange.UNISON);
		
		if (chordQuality.equals(Quality.MAJOR.toString())) {
			chord.set(IntervalRange.THIRD, Quality.MAJOR);
			chord.add(IntervalRange.FIFTH);
		} else if (chordQuality.equals(Quality.MINOR.toString())) {
			chord.set(IntervalRange.THIRD, Quality.MINOR);
			chord.add(IntervalRange.FIFTH);
		} else if (chordQuality.equals(AdvancedQuality.AUGMENTED.toString())) {
			chord.set(IntervalRange.THIRD, Quality.MAJOR);
			chord.set(IntervalRange.FIFTH, AdvancedQuality.AUGMENTED);
		} else if (chordQuality.equals(AdvancedQuality.DIMINISHED.toString())) {
			chord.set(IntervalRange.THIRD, Quality.MINOR);
			chord.set(IntervalRange.FIFTH, AdvancedQuality.DIMINISHED);
		} 
		
		if (intervalQuality.equals("6")) {
			chord.set(IntervalRange.SIXTH, Quality.MAJOR);
		} else if (intervalQuality.equals("7")) {
			chord.set(IntervalRange.SEVENTH, Quality.MINOR);
		} else if (intervalQuality.equals("M7")) {
			chord.set(IntervalRange.SEVENTH, Quality.MAJOR);
		}
		
		if (suspension.equals("sus2")) {
			chord.set(IntervalRange.SECOND, Quality.MAJOR);
			chord.remove(IntervalRange.THIRD);
		} else if (suspension.equals("sus4")) {
			chord.add(IntervalRange.FOURTH);
			chord.remove(IntervalRange.THIRD);
		}
		
		if (addedTone.indexOf("add") != -1) {
			switch(Integer.parseInt(addedTone.substring(3, addedTone.length()))) {
			case 13: chord.set(IntervalRange.THIRTEENTH, Quality.MAJOR);
			case 11: chord.add(IntervalRange.ELEVENTH);
			case  9: chord.set(IntervalRange.NINTH, Quality.MAJOR);
			}
		}
		
		return chord;
	}
	
	private static String extractChordQuality(String repres) {
		for (Quality quality : Quality.values()) {
			if (repres.indexOf(quality.toString()) == 0 && !quality.toString().isEmpty()) {
				return quality.toString();
			}
		}
		for (AdvancedQuality quality : AdvancedQuality.values()) {
			if (repres.indexOf(quality.toString()) == 0 && !quality.toString().isEmpty()) {
				return quality.toString();
			}
		}
		return "";
	}
	
	private static String extractIntervalQuality(String repres) {
		if (repres.indexOf("6") != -1) {
			return "6";
		} else if (repres.indexOf("M7") != -1) {
			return "M7";
		} else if (repres.indexOf("7") != -1) {
			return "7";
		}
		return "";
	}
	
	private static String extractSuspension(String repres) {
		if (repres.indexOf("sus2") != -1) {
			return "sus2";
		} else if (repres.indexOf("sus4") != -1) {
			return "sus4";
		}
		return "";
	}
	
	private static String extractAddedTones(String repres) {
		for (int i = 9; i <= MAX_INTERVAL_NUMBER; i +=2) {
			String addedTone = "add" + Integer.toString(i);
			if (repres.indexOf(addedTone) != -1) {
			return addedTone;
			}
		}
		return "";
	}	

	private static void verifyExtractedFeatures(String repres, String... features) {
		int lastGoodIndex = 0;
		int[] indexes = new int[features.length];
		for (int i = 0; i < features.length; ++i) {
			if (features[i] == "") {
				indexes[i] = indexes[lastGoodIndex] + features[lastGoodIndex].length();
			} else {
				indexes[i] = repres.indexOf(features[i]);
				lastGoodIndex = i;
			}
		}
		if (indexes[0] != 0) {
			throw new IllegalArgumentException("\"" + repres + "\" is either invalid or not yet implemented.");
		}
		for (int i = 0; i < features.length - 1; ++i) {
			if (indexes[i] + features[i].length() != indexes[i + 1]) {
				throw new IllegalArgumentException("\"" + repres + "\" is either invalid or not yet implemented.");
			}
			if (indexes[i] > indexes[i + 1]) {
				throw new IllegalArgumentException("\"" + repres + "\" is either invalid or not yet implemented.");
			}
		}
		if (indexes[lastGoodIndex] + features[lastGoodIndex].length() != repres.length()) {
			throw new IllegalArgumentException("\"" + repres + "\" is either invalid or not yet implemented.");
		}
	}


	//------------ SETTERS ------------\\

	protected void add(IntervalRange range) {
		intervals.put(range, Interval.valueOf(AdvancedQuality.PERFECT, range));
	}
	
	protected void set(IntervalRange range, IntervalQuality quality) {
		intervals.put(range, Interval.valueOf(quality, range));
	}
	
	protected void remove(IntervalRange range) {
		intervals.remove(range);
	}
	
	
	//------------ GETTERS ------------\\
	
	public List<Interval> getIntervals() {
		List<Interval> intervals = new ArrayList<Interval>();
		
		for (IntervalRange range : IntervalRange.values()) {
			Interval interval = this.intervals.get(range);
			if (interval != null) {
				intervals.add(interval);
			}
		}
		return intervals;
	}
	
	public Interval getIntervalForRange(IntervalRange range) {
		return intervals.get(range);
	}

	
	//------------ EQUALITIES ------------\\
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result	+ ((intervals == null) ? 0 : intervals.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChordType other = (ChordType) obj;
		if (intervals == null) {
			if (other.intervals != null)
				return false;
		} else if (!intervals.equals(other.intervals))
			return false;
		return true;
	}
}
