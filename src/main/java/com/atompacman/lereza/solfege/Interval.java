package com.atompacman.lereza.solfege;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.lereza.solfege.quality.AdvancedQuality;
import com.atompacman.lereza.solfege.quality.IntervalQuality;
import com.atompacman.lereza.solfege.quality.Quality;

public class Interval {

	private Direction direction;
	private IntervalQuality quality;
	private IntervalRange range;


	//------------ PRIVATE CONSTRUCTOR ------------\\

	private Interval(Direction direction, IntervalQuality quality, IntervalRange range) {
		if (!quality.getClass().equals(range.getQualityType())) {
			throw new IllegalArgumentException("Wrong quality type \""
					+ quality.getClass().getName() + "\" + for interval range \"" 
					+ range.name() + "\".");
		}
		if (range == IntervalRange.UNISON && quality == AdvancedQuality.PERFECT 
				&& direction != Direction.STRAIGHT) {
			throw new IllegalArgumentException("Direction must be \"STRAIGHT\" when interval range "
					+ "is perfect unison.");
		}
		this.direction = direction;
		this.quality = quality;
		this.range = range;
	}


	//------------ STATIC CONSTRUCTORS ------------\\

	public static Interval valueOf(IntervalRange range) {
		return valueOf(AdvancedQuality.PERFECT, range);
	}

	public static Interval valueOf(IntervalQuality quality, IntervalRange range) {
		Direction direct = range == IntervalRange.UNISON ? Direction.STRAIGHT : Direction.ASCENDING;
		return new Interval(direct, quality, range);
	}

	public static Interval valueOf(Direction direction, 
			IntervalQuality quality, IntervalRange range) {
		return new Interval(direction, quality, range);
	}

	public static Interval valueOf(int semitoneDelta, int diatonicToneDelta) {
		List<Interval> possibleIntervals = withSemitoneValue(semitoneDelta);

		for (Interval possibleInterval : possibleIntervals) {
			if (possibleInterval.diatonicToneValue() == diatonicToneDelta) {
				return possibleInterval;
			}
		}
		throw new IllegalArgumentException("No valid interval for semitone delta \"" 
				+ semitoneDelta	+ "\" and diatonic tone delta \"" + diatonicToneDelta + "\".");
	}

	public static List<Interval> withSemitoneValue(int semitoneValue) {
		List<Interval> possibleIntervals = new ArrayList<Interval>();

		if (semitoneValue == 0) {
			possibleIntervals.add(valueOf(IntervalRange.UNISON));
			return possibleIntervals;
		}
		Direction direction = semitoneValue > 0 ? Direction.ASCENDING : Direction.DESCENDING;
		semitoneValue = Math.abs(semitoneValue);

		List<IntervalRange> possibleRanges = IntervalRange.closestRangesFrom(semitoneValue);

		for (IntervalRange range : possibleRanges) {
			double semitoneDelta = semitoneValue - range.semitoneValue();
			IntervalQuality quality = getQualityFromSemitoneDelta(semitoneDelta);
			possibleIntervals.add(new Interval(direction, quality, range));
		}

		return possibleIntervals;
	}

	private static IntervalQuality getQualityFromSemitoneDelta(double semitoneDelta) {
		for (Quality quality : Quality.values()) {
			if (quality.semitoneModifier() == semitoneDelta) {
				return quality;
			}
		}
		for (AdvancedQuality quality : AdvancedQuality.values()) {
			if (quality.semitoneModifier() == semitoneDelta) {
				return quality;
			}
		}
		throw new IllegalArgumentException("No quality semitone modifier is equal to \"" 
				+ semitoneDelta + "\".");
	}


	//------------ GETTERS ------------\\

	public Direction getDirection() {
		return direction;
	}

	public IntervalQuality getQuality() {
		return quality;
	}

	public IntervalRange getIntervalRange() {
		return range;
	}


	//------------ ASCENDING ------------\\

	public Interval ascending() {
		if (direction == Direction.DESCENDING) {
			return new Interval(Direction.ASCENDING, quality, range);
		}
		return this;
	}


	//------------ TONE / SEMITONE VALUE ------------\\

	public int semitoneValue() {
		return (int) (direction.semitoneMultiplier() * (range.semitoneValue() + quality.semitoneModifier()));
	}

	public int diatonicToneValue() {
		return (int) (direction.semitoneMultiplier() * range.diatonicTonesValue());
	}


	//------------ BETWEEN ------------\\

	public static Interval between(Pitch a, Pitch b) {
		try {
			return valueOf(Semitones.between(a, b), DiatonicTones.between(a, b));
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Could not find the interval between \"" 
					+ a.toString() + "\" and \"" + b.toString() + "\": ", e);
		}
	}

	public static Interval between(Tone a, Direction direction, Tone b) {
		try {
			int semitoneDelta = Semitones.between(a, direction, b);
			int diatonicToneDelta = DiatonicTones.between(a, direction, b);
			return valueOf(semitoneDelta, diatonicToneDelta);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Could not find the interval between \"" 
					+ a.toString() + "\" and \"" + b.toString() + "\": ", e);
		}
	}


	//------------ STRING ------------\\

	public String toString() {
		String repres = quality.fullName() + " " + range.toString();
		if (direction != Direction.STRAIGHT) {
			repres = direction.toString() + " " + repres;
		}
		return repres;
	}


	//------------ EQUALITIES ------------\\

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + ((quality == null) ? 0 : quality.hashCode());
		result = prime * result + ((range == null) ? 0 : range.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Interval other = (Interval) obj;
		if (direction != other.direction)
			return false;
		if (quality == null) {
			if (other.quality != null)
				return false;
		} else if (!quality.equals(other.quality))
			return false;
		if (range != other.range)
			return false;
		return true;
	}
}
