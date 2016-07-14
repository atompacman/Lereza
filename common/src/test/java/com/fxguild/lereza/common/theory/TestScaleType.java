package com.fxguild.lereza.common.theory;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public final class TestScaleType {

    //
    //  ~  INIT  ~  //
    //

    @Test
    public void Init_ValidArgs_ValidInst() {
        assertEquals(ScaleType.NATURAL_MINOR, ScaleType.of(Quality.MINOR));
    }


    //
    //  ~  INTERVAL  ~  //
    //

    @Test
    public void SingleMethod_intervalFromRootTo_ValidInst_ValidReturnedValue() {
        assertEquals(Interval.of(Quality.MAJOR, IntervalRange.THIRD), 
                     ScaleType.MAJOR.intervalFromRootTo(Degree.III));

        assertEquals(Interval.of(Quality.MINOR, IntervalRange.SIXTH), 
                     ScaleType.NATURAL_MINOR.intervalFromRootTo(Degree.VI));
        
        assertEquals(Interval.of(Quality.MAJOR, IntervalRange.SEVENTH), 
                     ScaleType.HARMONIC_MINOR.intervalFromRootTo(Degree.VII));
    }


    //
    //  ~  GETTERS  ~  //
    //

    @Test
    public void SingleMethod_getDegree_ValidInst_ValidReturnedValue() {
        assertEquals(ScaleDegree.of(Degree.III, ChordType.of("m")), 
                     ScaleType.MAJOR.getDegree(Degree.III));

        assertEquals(ScaleDegree.of(Degree.II, ChordType.of("dim")), 
                     ScaleType.NATURAL_MINOR.getDegree(Degree.II));

        assertEquals(ScaleDegree.of(Degree.I, ChordType.of("")), 
                     ScaleType.MAJOR.getDegree(Degree.I));

        assertEquals(ScaleDegree.of(Degree.IV, ChordType.of("m")), 
                     ScaleType.HARMONIC_MINOR.getDegree(Degree.IV));
    }
}
