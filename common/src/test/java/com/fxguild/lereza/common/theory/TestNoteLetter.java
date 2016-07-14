package com.fxguild.lereza.common.theory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public final class TestNoteLetter {

    //
    //  ~  INIT  ~  //
    //

    @Test
    public void Init_fromDiatonicToneValue_ValidArg_ValidInstState() {
        assertEquals(NoteLetter.fromDiatonicToneValue(  4), NoteLetter.G);
        assertEquals(NoteLetter.fromDiatonicToneValue( 15), NoteLetter.D);
        assertEquals(NoteLetter.fromDiatonicToneValue(-19), NoteLetter.E);
    }
    
    @Test
    public void Init_withSemitoneValue_ValidArg_ValidInstState() {
        assertEquals(Arrays.asList(NoteLetter.E, NoteLetter.F), NoteLetter.withSemitoneValue(4));
    }


    //
    //  ~  CAN BE ASSIGNED FROM  ~  //
    //

    @Test
    public void SingleMethod_canBeAssignedFrom_ValidArg_ValidReturnedValue() {
        assertTrue( NoteLetter.A.canBeAssignedFrom( 8));
        assertTrue( NoteLetter.A.canBeAssignedFrom( 9));
        assertTrue( NoteLetter.A.canBeAssignedFrom(10));
        assertTrue(!NoteLetter.A.canBeAssignedFrom(11));
        assertTrue( NoteLetter.C.canBeAssignedFrom(11));
    }


    //
    //  ~  PREVIOUS / NEXT  ~  //
    //
    
    @Test
    public void Methods_getNext_getPrevious_ValidArg_ValidReturnedValue() {
        assertEquals(NoteLetter.B.getNext().getPrevious(), NoteLetter.B);
        assertEquals(NoteLetter.B.getNext(),               NoteLetter.C);
    }
}
