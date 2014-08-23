package com.atompacman.lereza.common.solfege;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.lereza.common.solfege.quality.AdvancedQuality;
import com.atompacman.lereza.common.solfege.quality.IntervalQuality;
import com.atompacman.lereza.common.solfege.quality.Quality;

public class Interval {

	private Direction direction;
	private IntervalQuality quality;
	private IntervalRange range;


	//------------ CONSTRUCTORS ------------\\

	public Interval(IntervalRange range) {
		this(AdvancedQuality.PERFECT, range);
	}

	public Interval(IntervalQuality quality, IntervalRange range) {
		this(range == IntervalRange.UNISON ? Direction.STRAIGHT : Direction.ASCENDING, quality, range);
	}

	public Interval(Direction direction, IntervalQuality quality, IntervalRange range) {
		if (direction == null || quality == null || range == null) {
			throw new NullPointerException();
		}
		if (!quality.getClass().equals(range.getQualityType())) {
			throw new IllegalArgumentException("Wrong quality type \"" + quality.getClass().getName() + 
					"\" + for interval range \"" + range.name() + "\".");
		}
		if (range == IntervalRange.UNISON && direction != Direction.STRAIGHT) {
			throw new IllegalArgumentException("Direction must be \"STRAIGHT\" when interval range is unison.");
		}
		this.direction = direction;
		this.quality = quality;
		this.range = range;
	}


	//------------ STATIC CONSTRUCTORS ------------\\

	public static List<Interval> fromSemitoneValue(int semitoneValue) {
		List<Interval> possibleIntervals = new ArrayList<Interval>();

		if (semitoneValue == 0) {
			possibleIntervals.add(new Interval(IntervalRange.UNISON));
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
		throw new IllegalArgumentException("No quality semitone modifier is equal to \"" + semitoneDelta + "\".");
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


	//------------ TONE /  SEMITONE ------------\\

	public int semitoneValue() {
		return (int) (direction.semitoneMultiplier() * (range.semitoneValue() + quality.semitoneModifier()));
	}

	public int diatonicToneValue() {
		return (int) (direction.semitoneMultiplier() * range.diatonicTonesValue());
	}


	//------------ BETWEEN ------------\\

	public static Interval between(Pitch a, Pitch b) {
		try {
			return between(Semitones.between(a, b), DiatonicTones.between(a, b));
		} catch (Exception e) {
			throw new IllegalArgumentException("Could not measure the interval between \"" 
					+ a.toString() + "\" and \"" + b.toString() + "\": ", e);
		}
	}

	public static Interval between(Tone a, Tone b) {
		try {
			return between(Semitones.between(a, b), DiatonicTones.between(a, b));
		} catch (Exception e) {
			throw new IllegalArgumentException("Could not measure the interval between \"" 
					+ a.toString() + "\" and \"" + b.toString() + "\": ", e);
		}
	}

	public static Interval between(int semitoneDelta, int diatonicToneDelta) {
		List<Interval> possibleIntervals = fromSemitoneValue(semitoneDelta);

		for (Interval possibleInterval : possibleIntervals) {
			if (possibleInterval.diatonicToneValue() == diatonicToneDelta) {
				return possibleInterval;
			}
		}
		throw new IllegalArgumentException("Unkown error.");
	}


	//------------ STRING ------------\\

	public String toString() {
		if (direction == Direction.STRAIGHT) {
			return quality.fullName() + " " + range.toString();
		} else {
			return direction.toString() + " " + quality.fullName() + " " + range.toString();
		}
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
