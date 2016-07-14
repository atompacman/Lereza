package com.fxguild.lereza.common.theory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public final class TestIntervalRange {

    //
    //  ~  INIT  ~  //
    //

    @Test
    public void Init_ValidInstState() {
        assertEquals(AdvancedQuality.class, IntervalRange.FOURTH    .getQualityType());
        assertEquals(Quality.class,         IntervalRange.THIRTEENTH.getQualityType());
    }

    @Test
    public void Init_closestRangesFrom_ValidArg_ValidInstState() {
        runSingleClosestRangesFromTest(5,  IntervalRange.FOURTH);
        runSingleClosestRangesFromTest(6,  IntervalRange.FOURTH,     IntervalRange.FIFTH);
        runSingleClosestRangesFromTest(23, IntervalRange.FOURTEENTH, IntervalRange.DOUBLE_OCTAVE);
    }

    private static void runSingleClosestRangesFromTest(int semitoneValue, IntervalRange...ranges) {
        List<IntervalRange> closestRanges = IntervalRange.closestRangesFrom(semitoneValue);
        assertEquals(ranges.length, closestRanges.size());
        for (int i = 0; i < ranges.length; ++i) {
            assertEquals(ranges[i], closestRanges.get(i));
        }
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void Init_closestRangesFrom_InvalidArg_NegVal_Throw() {
        IntervalRange.closestRangesFrom(-4);
    }

    @Test (expected = IllegalArgumentException.class)
    public void Init_closestRangesFrom_InvalidArg_ValTooHigh_Throw() {
        IntervalRange.closestRangesFrom(44);
    }


    //
    //  ~  VALUE  ~  //
    //

    @Test
    public void SingleMethod_isWithinSemitoneRangeOf_ValidArgs_ValidReturnedValue() {
        assertTrue( IntervalRange.SIXTH   .isWithinSemitoneRangeOf( 8));
        assertTrue(!IntervalRange.SIXTH   .isWithinSemitoneRangeOf(11));
        assertTrue(!IntervalRange.ELEVENTH.isWithinSemitoneRangeOf(15));
        assertTrue( IntervalRange.ELEVENTH.isWithinSemitoneRangeOf(18));
    }
}
