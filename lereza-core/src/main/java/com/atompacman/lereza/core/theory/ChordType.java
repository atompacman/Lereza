package com.atompacman.lereza.core.theory;

import static com.atompacman.lereza.core.theory.AdvancedQuality.AUGMENTED;
import static com.atompacman.lereza.core.theory.AdvancedQuality.DIMINISHED;
import static com.atompacman.lereza.core.theory.AdvancedQuality.PERFECT;
import static com.atompacman.lereza.core.theory.IntervalRange.ELEVENTH;
import static com.atompacman.lereza.core.theory.IntervalRange.FIFTH;
import static com.atompacman.lereza.core.theory.IntervalRange.FOURTH;
import static com.atompacman.lereza.core.theory.IntervalRange.NINTH;
import static com.atompacman.lereza.core.theory.IntervalRange.SECOND;
import static com.atompacman.lereza.core.theory.IntervalRange.SEVENTH;
import static com.atompacman.lereza.core.theory.IntervalRange.SIXTH;
import static com.atompacman.lereza.core.theory.IntervalRange.THIRD;
import static com.atompacman.lereza.core.theory.IntervalRange.THIRTEENTH;
import static com.atompacman.lereza.core.theory.IntervalRange.UNISON;
import static com.atompacman.lereza.core.theory.Quality.MAJOR;
import static com.atompacman.lereza.core.theory.Quality.MINOR;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

@AutoValue
public abstract class ChordType {

    //
    //  ~  CONSTANTS  ~  //
    //

    private static final int MAX_INTERVAL_NUMBER = 13;

    
    //
    //  ~  FIELDS  ~  //
    //

    protected abstract ImmutableMap<IntervalRange, Interval> intervals();
    

    //
    //  ~  INIT  ~  //
    //

    public static ChordType of(String str) {
        String chordQuality    = extractChordQuality(str);
        String intervalQuality = extractIntervalQuality(str);
        String suspension      = extractSuspension(str);
        String addedTone       = extractAddedTones(str);

        verifyExtractedFeatures(str, chordQuality, intervalQuality, suspension, addedTone);

        Map<IntervalRange, Interval> intervals = new HashMap<>();

        intervals.put(UNISON, Interval.of(PERFECT, UNISON));
        
        if (chordQuality.equals(MAJOR.toString())) {
            intervals.put(THIRD, Interval.of(MAJOR,      THIRD));
            intervals.put(FIFTH, Interval.of(PERFECT,    FIFTH));

        } else if (chordQuality.equals(MINOR.toString())) {
            intervals.put(THIRD, Interval.of(MINOR,      THIRD));
            intervals.put(FIFTH, Interval.of(PERFECT,    FIFTH));
        } else if (chordQuality.equals(AUGMENTED.toString())) {
            intervals.put(THIRD, Interval.of(MAJOR,      THIRD));
            intervals.put(FIFTH, Interval.of(AUGMENTED,  FIFTH));
        } else if (chordQuality.equals(DIMINISHED.toString())) {
            intervals.put(THIRD, Interval.of(MINOR,      THIRD));
            intervals.put(FIFTH, Interval.of(DIMINISHED, FIFTH));
        } 

        if (intervalQuality.equals("6")) {
            intervals.put(SIXTH,   Interval.of(MAJOR, SIXTH));
        } else if (intervalQuality.equals("7")) {
            intervals.put(SEVENTH, Interval.of(MINOR, SEVENTH));
        } else if (intervalQuality.equals("M7")) {
            intervals.put(SEVENTH, Interval.of(MAJOR, SEVENTH));
        }

        if (suspension.equals("sus2")) {
            intervals.put   (SECOND, Interval.of(MAJOR,   SECOND));
            intervals.remove(THIRD);
        } else if (suspension.equals("sus4")) {
            intervals.put   (FOURTH, Interval.of(PERFECT, FOURTH));
            intervals.remove(THIRD);
        }

        if (addedTone.contains("add")) {
            switch(Integer.parseInt(addedTone.substring(3, addedTone.length()))) {
            case 13: intervals.put(THIRTEENTH, Interval.of(MAJOR,   THIRTEENTH));
            case 11: intervals.put(ELEVENTH,   Interval.of(PERFECT, ELEVENTH));
            case  9: intervals.put(NINTH,      Interval.of(MAJOR,   NINTH));
            }
        }

        return new AutoValue_ChordType(ImmutableMap.copyOf(intervals));
    }

    public static ChordType of(List<Interval> intervals) {
        Map<IntervalRange, Interval> chordIntervals = new HashMap<IntervalRange, Interval>();
        for (Interval interval : intervals) {
            chordIntervals.put(interval.getRange(), interval);
        }
        return new AutoValue_ChordType(ImmutableMap.copyOf(chordIntervals));
    }

    private static String extractChordQuality(String str) {
        for (Quality quality : Quality.values()) {
            if (str.indexOf(quality.toString()) == 0 && !quality.toString().isEmpty()) {
                return quality.toString();
            }
        }
        for (AdvancedQuality quality : AdvancedQuality.values()) {
            if (str.indexOf(quality.toString()) == 0 && !quality.toString().isEmpty()) {
                return quality.toString();
            }
        }
        return "";
    }

    private static String extractIntervalQuality(String str) {
        if (str.contains("6")) {
            return "6";
        } else if (str.contains("M7")) {
            return "M7";
        } else if (str.contains("7")) {
            return "7";
        } else {
            return "";
        }
    }

    private static String extractSuspension(String str) {
        if (str.contains("sus2")) {
            return "sus2";
        } else if (str.contains("sus4")) {
            return "sus4";
        } else {
            return "";
        }
    }

    private static String extractAddedTones(String str) {
        for (int i = 9; i <= MAX_INTERVAL_NUMBER; i +=2) {
            String addedTone = "add" + Integer.toString(i);
            if (str.indexOf(addedTone) != -1) {
                return addedTone;
            }
        }
        return "";
    }	

    private static void verifyExtractedFeatures(String str, String... features) {
        int lastGoodIndex = 0;
        int[] indexes = new int[features.length];
        
        for (int i = 0; i < features.length; ++i) {
            if (features[i].isEmpty()) {
                indexes[i] = indexes[lastGoodIndex] + features[lastGoodIndex].length();
            } else {
                indexes[i] = str.indexOf(features[i]);
                lastGoodIndex = i;
            }
        }
        
        String errMsg = "\"" + str + "\" is either invalid or not yet implemented.";
        
        if (indexes[0] != 0) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < features.length - 1; ++i) {
            if (indexes[i] + features[i].length() != indexes[i + 1]) {
                throw new IllegalArgumentException(errMsg);
            }
            if (indexes[i] > indexes[i + 1]) {
                throw new IllegalArgumentException(errMsg);
            }
        }
        if (indexes[lastGoodIndex] + features[lastGoodIndex].length() != str.length()) {
            throw new IllegalArgumentException(errMsg);
        }
    }


    //
    //  ~  GETTERS  ~  //
    //

    public ImmutableList<Interval> getIntervals() {
        return ImmutableList.copyOf(intervals().values());
    }
    
    public Optional<Interval> getIntervalForRange(IntervalRange range) {
        return Optional.ofNullable(intervals().get(range));
    }
}
