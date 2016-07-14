package com.fxguild.lereza.common.theory;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public final class TestScaleDegree {

    //
    //  ~  INIT  ~  //
    //

    @Test
    public void Init_ValidArgs_ValidInstState() {
        assertEquals(ScaleDegree.of(Degree.I,  ChordType.of("")),       ScaleDegree.of("I"));
        assertEquals(ScaleDegree.of(Degree.II, ChordType.of("m7")),     ScaleDegree.of("ii7"));
        assertEquals(ScaleDegree.of(Degree.VI, ChordType.of("mM7sus4")),ScaleDegree.of("viM7sus4"));
    }
}
