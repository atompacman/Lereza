package com.atompacman.lereza.core.theory;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.List;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Interval {

    //
    //  ~  FIELDS  ~  //
    //

    public abstract Direction       getDirection();
    public abstract IntervalQuality getQuality();
    public abstract IntervalRange   getRange();


    //
    //  ~  INIT  ~  //
    //

    public static Interval of(IntervalRange range) {
        return of(AdvancedQuality.PERFECT, range);
    }

    public static Interval of(IntervalQuality quality, IntervalRange range) {
        Direction dir = range == IntervalRange.UNISON ? Direction.STRAIGHT : Direction.ASCENDING;
        return of(dir, quality, range);
    }

    public static Interval of(int semitoneDelta, int diatonicToneDelta) {
        List<Interval> possibleIntervals = withSemitoneValue(semitoneDelta);

        for (Interval possibleInterval : possibleIntervals) {
            if (possibleInterval.diatonicToneValue() == diatonicToneDelta) {
                return possibleInterval;
            }
        }
        throw new IllegalArgumentException("No valid interval for semitone delta \"" 
                + semitoneDelta + "\" and diatonic tone delta \"" + diatonicToneDelta + "\".");
    }

    public static Interval of(Direction direction, IntervalQuality quality, IntervalRange range) {
        checkArgument(quality.getClass().equals(range.getQualityType()), 
                String.format("Wrong quality type \"%s\" + for interval range \"%s\".", 
                quality.getClass(), range.name()));
        
        checkArgument(range != IntervalRange.UNISON || quality != AdvancedQuality.PERFECT 
                || direction == Direction.STRAIGHT, 
                "Direction must be \"STRAIGHT\" when interval range is perfect unison.");
        
        return new AutoValue_Interval(direction, quality, range);
    }
    
    public static List<Interval> withSemitoneValue(int semitoneValue) {
        List<Interval> possibleIntervals = new ArrayList<>();

        if (semitoneValue == 0) {
            possibleIntervals.add(of(IntervalRange.UNISON));
            return possibleIntervals;
        }
        
        Direction direction = semitoneValue > 0 ? Direction.ASCENDING : Direction.DESCENDING;
        semitoneValue = Math.abs(semitoneValue);

        List<IntervalRange> possibleRanges = IntervalRange.closestRangesFrom(semitoneValue);

        for (IntervalRange range : possibleRanges) {
            double semitoneDelta = semitoneValue - range.semitoneValue();
            IntervalQuality quality = getQualityFromSemitoneDelta(semitoneDelta);
            possibleIntervals.add(new AutoValue_Interval(direction, quality, range));
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
        throw new IllegalArgumentException("No quality semitone "
                + "modifier is equal to \"" + semitoneDelta + "\".");
    }


    //
    //  ~  ASCENDING  ~  //
    //

    public Interval ascending() {
        if (getDirection() == Direction.DESCENDING) {
            return new AutoValue_Interval(Direction.ASCENDING, getQuality(), getRange());
        }
        return this;
    }


    //
    //  ~  VALUE  ~  //
    //

    public int semitoneValue() {
        return (int) (getDirection().semitoneMultiplier() * 
                (getRange().semitoneValue() + getQuality().semitoneModifier()));
    }

    public int diatonicToneValue() {
        return (int) (getDirection().semitoneMultiplier() * getRange().diatonicTonesValue());
    }


    //
    //  ~  BETWEEN  ~  //
    //

    public static Interval between(Pitch a, Pitch b) {
        try {
            return of(Semitones.between(a, b), DiatonicTones.between(a, b));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Could not find the interval between \"" 
                    + a.toString() + "\" and \"" + b.toString() + "\": ", e);
        }
    }

    public static Interval between(Tone a, Direction direction, Tone b) {
        try {
            int semitoneDelta = Semitones.between(a, direction, b);
            int diatonicToneDelta = DiatonicTones.between(a, direction, b);
            if (direction == Direction.DESCENDING) {
                semitoneDelta     *= -1;
                diatonicToneDelta *= -1;
            }
            return of(semitoneDelta, diatonicToneDelta);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Could not find the interval between \"" 
                    + a.toString() + "\" and \"" + b.toString() + "\": ", e);
        }
    }


    //
    //  ~  SERIALIZATION  ~  //
    //

    @Override
    public String toString() {
        String repres = getQuality().fullName() + " " + getRange().toString();
        if (getDirection() != Direction.STRAIGHT) {
            repres = getDirection().toString() + " " + repres;
        }
        return repres;
    }
}
