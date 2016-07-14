package com.fxguild.lereza.common.theory;

import static com.fxguild.lereza.common.theory.AdvancedQuality.AUGMENTED;
import static com.fxguild.lereza.common.theory.AdvancedQuality.DIMINISHED;
import static com.fxguild.lereza.common.theory.AdvancedQuality.PERFECT;
import static com.fxguild.lereza.common.theory.IntervalRange.ELEVENTH;
import static com.fxguild.lereza.common.theory.IntervalRange.FIFTH;
import static com.fxguild.lereza.common.theory.IntervalRange.FOURTH;
import static com.fxguild.lereza.common.theory.IntervalRange.NINTH;
import static com.fxguild.lereza.common.theory.IntervalRange.SECOND;
import static com.fxguild.lereza.common.theory.IntervalRange.SEVENTH;
import static com.fxguild.lereza.common.theory.IntervalRange.SIXTH;
import static com.fxguild.lereza.common.theory.IntervalRange.THIRD;
import static com.fxguild.lereza.common.theory.IntervalRange.THIRTEENTH;
import static com.fxguild.lereza.common.theory.IntervalRange.UNISON;
import static com.fxguild.lereza.common.theory.Quality.MAJOR;
import static com.fxguild.lereza.common.theory.Quality.MINOR;
import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fxguild.common.annotations.SubMethodOf;
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

        // Root
        intervals.put(UNISON, Interval.of(PERFECT, UNISON));
        
        // Third and fifth
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

        // Sixth or seventh
        if (intervalQuality.equals("6")) {
            intervals.put(SIXTH,   Interval.of(MAJOR, SIXTH));
        } else if (intervalQuality.equals("7")) {
            intervals.put(SEVENTH, Interval.of(MINOR, SEVENTH));
        } else if (intervalQuality.equals("M7")) {
            intervals.put(SEVENTH, Interval.of(MAJOR, SEVENTH));
        }

        // Suspension
        if (suspension.equals("sus2")) {
            intervals.put   (SECOND, Interval.of(MAJOR,   SECOND));
            intervals.remove(THIRD);
        } else if (suspension.equals("sus4")) {
            intervals.put   (FOURTH, Interval.of(PERFECT, FOURTH));
            intervals.remove(THIRD);
        }

        // Added tones (notice use of fall-through switch/case)
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

    @SubMethodOf("of")
    private static String extractChordQuality(String str) {
        for (Quality quality : Quality.values()) {
            if (!quality.toString().isEmpty() && str.startsWith(quality.toString())) {
                return quality.toString();
            }
        }
        for (AdvancedQuality quality : AdvancedQuality.values()) {
            if (!quality.toString().isEmpty() && str.startsWith(quality.toString())) {
                return quality.toString();
            }
        }
        return "";
    }

    @SubMethodOf("of")
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

    @SubMethodOf("of")
    private static String extractSuspension(String str) {
        if (str.contains("sus2")) {
            return "sus2";
        } else if (str.contains("sus4")) {
            return "sus4";
        } else {
            return "";
        }
    }

    @SubMethodOf("of")
    private static String extractAddedTones(String str) {
        for (int i = 9; i <= MAX_INTERVAL_NUMBER; i += 2) {
            String addedTone = "add" + Integer.toString(i);
            if (str.contains(addedTone)) {
                return addedTone;
            }
        }
        return "";
    }	

    @SubMethodOf("of")
    private static void verifyExtractedFeatures(String str, String...features) {
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
        
        checkArgument(indexes[0] == 0, errMsg);
        for (int i = 0; i < features.length - 1; ++i) {
            checkArgument(indexes[i] + features[i].length() == indexes[i + 1], errMsg);
            checkArgument(indexes[i] <= indexes[i + 1], errMsg);
        }
        checkArgument(indexes[lastGoodIndex]+features[lastGoodIndex].length()==str.length(),errMsg);
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
