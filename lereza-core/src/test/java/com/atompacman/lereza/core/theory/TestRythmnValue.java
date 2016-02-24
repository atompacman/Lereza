package com.atompacman.lereza.core.theory;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public final class TestRythmnValue {

    //
    //  ~  INIT  ~  //
    //

    @Test
    public void Init_ValidArg_ValidInstState() {
        assertEquals(RythmnValue.THIRTYSECONTH, RythmnValue.fromTimeunit(2));
    }

    @Test  (expected = IllegalArgumentException.class)
    public void Init_InvalidArg_Throw() {
        RythmnValue.fromTimeunit(7);
    }


    //
    //  ~  TO TIMEUNIT  ~  //
    //

    @Test
    public void SingleMethod_toTimeunit_ValidInst_ValidReturnedValue() {
        assertEquals(16, RythmnValue.QUARTER.toTimeunit());
    }


    //
    //  ~  SPLIT INTO VALUES  ~  //
    //

    @Test
    public void SingleMethod_splitIntoValues_ValidArg_ValidReturnedValue() {
        assertEquals(4, RythmnValue.splitIntoValues( 77).size());
        assertEquals(2, RythmnValue.splitIntoValues(128).size());
    }
}
