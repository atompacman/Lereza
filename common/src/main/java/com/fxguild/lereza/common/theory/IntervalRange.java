package com.fxguild.lereza.common.theory;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.List;

public enum IntervalRange {

    UNISON        (0.0),
    SECOND        (1.5),
    THIRD         (3.5),
    FOURTH        (5.0),
    FIFTH         (7.0),
    SIXTH         (8.5),
    SEVENTH       (10.5),
    OCTAVE        (12.0),
    NINTH         (13.5), 
    TENTH         (15.5), 
    ELEVENTH      (17.0), 
    TWELVTH       (19.0), 
    THIRTEENTH    (20.5), 
    FOURTEENTH    (22.5), 
    DOUBLE_OCTAVE (24.0);


    //
    //  ~  FIELDS  ~  //
    //

    private final double semitoneValue;


    //
    //  ~  INIT  ~  //
    //

    private IntervalRange(double semitoneValue) {
        this.semitoneValue = semitoneValue;
    }

    public static List<IntervalRange> closestRangesFrom(int semitoneValue) {
        checkArgument(semitoneValue >= 0, "Semitone value must be positive.");
        List<IntervalRange> closestRanges = new ArrayList<IntervalRange>();

        for (IntervalRange range : values()) {
            if (range.isWithinSemitoneRangeOf(semitoneValue)) {
                closestRanges.add(range);
            }
        }
        
        IntervalRange highest = IntervalRange.values()[IntervalRange.values().length - 1];
        checkArgument(!closestRanges.isEmpty(), "Semitone value must equal to or "
                + "smaller than %d (%s).", highest.semitoneValue(), highest.name());

        return closestRanges;
    }


    //
    //  ~  GETTERS  ~  //
    //
    
    public Class<? extends IntervalQuality> getQualityType() {
        return (semitoneValue == (int) semitoneValue) ? AdvancedQuality.class : Quality.class;
    }


    //
    //  ~  VALUE  ~  //
    //

    public double semitoneValue() {
        return semitoneValue;
    }

    public int diatonicTonesValue() {
        return ordinal();
    }

    public boolean isWithinSemitoneRangeOf(int semitoneValue) {
        double semitoneDelta = Math.abs(this.semitoneValue - (double) semitoneValue);
        return semitoneDelta <= (getQualityType().equals(Quality.class) ?  0.5 : 1.0);
    }


    //
    //  ~  SERIALIZATION  ~  //
    //

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}