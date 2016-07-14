package com.fxguild.lereza.common.theory;

import static com.fxguild.lereza.common.theory.AdvancedQuality.AUGMENTED;
import static com.fxguild.lereza.common.theory.IntervalRange.ELEVENTH;
import static com.fxguild.lereza.common.theory.IntervalRange.FIFTH;
import static com.fxguild.lereza.common.theory.IntervalRange.FOURTH;
import static com.fxguild.lereza.common.theory.IntervalRange.NINTH;
import static com.fxguild.lereza.common.theory.IntervalRange.SEVENTH;
import static com.fxguild.lereza.common.theory.IntervalRange.THIRD;
import static com.fxguild.lereza.common.theory.IntervalRange.THIRTEENTH;
import static com.fxguild.lereza.common.theory.IntervalRange.UNISON;
import static com.fxguild.lereza.common.theory.Quality.MAJOR;
import static com.fxguild.lereza.common.theory.Quality.MINOR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;

public final class TestChordType {

    //
    //  ~  INIT  ~  //
    //
    
    @Test
    public void Init_ValidArgs_ValidInstState() {
        runSingleInitialisationTest("m", 
                Interval.of(UNISON),
                Interval.of(MINOR, THIRD),
                Interval.of(FIFTH));
        
        runSingleInitialisationTest("7", 
                Interval.of(UNISON),
                Interval.of(MAJOR, THIRD),
                Interval.of(FIFTH),
                Interval.of(MINOR, SEVENTH));

        runSingleInitialisationTest("m7sus4",    
                Interval.of(UNISON),
                Interval.of(FOURTH), 
                Interval.of(FIFTH),
                Interval.of(MINOR, SEVENTH));
        
        runSingleInitialisationTest("augadd9",    
                Interval.of(UNISON),
                Interval.of(MAJOR, THIRD), 
                Interval.of(AUGMENTED, FIFTH),
                Interval.of(MAJOR, NINTH));

        runSingleInitialisationTest("mM7add13",
                Interval.of(UNISON),
                Interval.of(MINOR, THIRD), 
                Interval.of(FIFTH),
                Interval.of(MAJOR, SEVENTH),
                Interval.of(MAJOR, NINTH),
                Interval.of(ELEVENTH),
                Interval.of(MAJOR, THIRTEENTH));
    }
    
    private static void runSingleInitialisationTest(String      chordTypeStr, 
                                                    Interval... expectedIntervals) {
        
        ChordType chordType = ChordType.of(chordTypeStr);
        assertEquals(chordType.getIntervals().size(), expectedIntervals.length);
        for (Interval interval : expectedIntervals) {
            Optional<Interval> actualInterv = chordType.getIntervalForRange(interval.getRange());
            assertTrue("Chord type" + chordTypeStr + " should contain an interval "
                     + "of range " + interval.getRange(), actualInterv.isPresent());
            assertEquals(interval, actualInterv.get());
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void Init_InvalidArgs_Throw() {
        ChordType.of("sus4M7");
    }
}
