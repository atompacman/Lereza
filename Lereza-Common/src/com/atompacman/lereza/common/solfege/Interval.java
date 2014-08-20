package com.atompacman.lereza.common.solfege;

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

	public static Interval fromSemitoneValue(int semitones) {
		Direction direction;
		if (semitones < 0) {
			direction = Direction.DESCENDING;
			semitones = -semitones;
		} else if (semitones > 0) {
			direction = Direction.ASCENDING;
		} else {
			return new Interval(IntervalRange.UNISON);
		}
		
		for (int i = 1; i < IntervalRange.values().length; ++i) {
			IntervalRange range = IntervalRange.values()[i];
			if (range.getQualityType().equals(Quality.class)) {
				Interval interval = new Interval(direction, Quality.MINOR, range);
				if (Math.abs(interval.semitoneValue()) == semitones) {
					return interval;
				}
				interval = new Interval(direction, Quality.MAJOR, range);
				if (Math.abs(interval.semitoneValue()) == semitones) {
					return interval;
				}
			} else {
				Interval interval = new Interval(direction, AdvancedQuality.PERFECT, range);
				if (Math.abs(interval.semitoneValue()) == semitones) {
					return interval;
				}
				if (range.equals(IntervalRange.FIFTH) || range.equals(IntervalRange.TWELVTH)) {
					interval = new Interval(direction, AdvancedQuality.DIMINISHED, range);
					if (Math.abs(interval.semitoneValue()) == semitones) {
						return interval;
					}
				}
			}
		}
		throw new IllegalArgumentException("Cannot obtain an interval for \"" + semitones + "\" semitones.");
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

	
	//------------ SEMITONE VALUE ------------\\

	public int semitoneValue() {
		return (int) (direction.semitoneMultiplier() * (range.semitoneValue() + quality.semitoneModifier()));
	}

	
	//------------ GET SIMPLE INTERVAL ------------\\

	public static Interval getSimpleInterval(Pitch a, Pitch b) {
		return fromSemitoneValue(b.semitoneValue() - a.semitoneValue());
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
