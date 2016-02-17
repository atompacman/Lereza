package com.atompacman.lereza.core.theory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ChordType {

    //
    //  ~  CONSTANTS  ~  //
    //

    private static final int MAX_INTERVAL_NUMBER = 13;

    
    //
    //  ~  FIELDS  ~  //
    //

    public abstract Map<IntervalRange, Interval> getIntervals();
    

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

        add(IntervalRange.UNISON, intervals);

        if (chordQuality.equals(Quality.MAJOR.toString())) {
            add(IntervalRange.THIRD, Quality.MAJOR, intervals);
            add(IntervalRange.FIFTH, intervals);
        } else if (chordQuality.equals(Quality.MINOR.toString())) {
            add(IntervalRange.THIRD, Quality.MINOR, intervals);
            add(IntervalRange.FIFTH, intervals);
        } else if (chordQuality.equals(AdvancedQuality.AUGMENTED.toString())) {
            add(IntervalRange.THIRD, Quality.MAJOR, intervals);
            add(IntervalRange.FIFTH, AdvancedQuality.AUGMENTED, intervals);
        } else if (chordQuality.equals(AdvancedQuality.DIMINISHED.toString())) {
            add(IntervalRange.THIRD, Quality.MINOR, intervals);
            add(IntervalRange.FIFTH, AdvancedQuality.DIMINISHED, intervals);
        } 

        if (intervalQuality.equals("6")) {
            add(IntervalRange.SIXTH,   Quality.MAJOR, intervals);
        } else if (intervalQuality.equals("7")) {
            add(IntervalRange.SEVENTH, Quality.MINOR, intervals);
        } else if (intervalQuality.equals("M7")) {
            add(IntervalRange.SEVENTH, Quality.MAJOR, intervals);
        }

        if (suspension.equals("sus2")) {
            add   (IntervalRange.SECOND, Quality.MAJOR, intervals);
            remove(IntervalRange.THIRD,  intervals);
        } else if (suspension.equals("sus4")) {
            add   (IntervalRange.FOURTH, intervals);
            remove(IntervalRange.THIRD,  intervals);
        }

        if (addedTone.contains("add")) {
            switch(Integer.parseInt(addedTone.substring(3, addedTone.length()))) {
            case 13: add(IntervalRange.THIRTEENTH, Quality.MAJOR, intervals);
            case 11: add(IntervalRange.ELEVENTH,                  intervals);
            case  9: add(IntervalRange.NINTH,      Quality.MAJOR, intervals);
            }
        }

        return new AutoValue_ChordType(intervals);
    }

    public static ChordType of(List<Interval> intervals) {
        Map<IntervalRange, Interval> chordIntervals = new HashMap<IntervalRange, Interval>();
        for (Interval interval : intervals) {
            chordIntervals.put(interval.getRange(), interval);
        }
        return new AutoValue_ChordType(chordIntervals);
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

    private static void add(IntervalRange range, Map<IntervalRange, Interval> intervals) {
        intervals.put(range, Interval.of(AdvancedQuality.PERFECT, range));
    }

    private static void add(IntervalRange                range, 
                            IntervalQuality              quality, 
                            Map<IntervalRange, Interval> intervals) {
        
        intervals.put(range, Interval.of(quality, range));
    }

    private static void remove(IntervalRange range, Map<IntervalRange, Interval> intervals) {
        intervals.remove(range);
    }


    //
    //  ~  GETTERS  ~  //
    //

    public Interval getIntervalForRange(IntervalRange range) {
        return getIntervals().get(range);
    }
}
