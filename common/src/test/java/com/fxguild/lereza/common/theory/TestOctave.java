package com.fxguild.lereza.common.theory;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public final class TestOctave {

    //
    //  ~  INIT  ~  //
    //

    @Test
    public void Init_fromInt_ValidArg_ValidInstState() {
        assertEquals(Octave.FIVE, Octave.fromInt(5));
    }

    @Test
    public void Init_fromHex_ValidArg_ValidInstState() {
        assertEquals(Octave.ZERO, Octave.fromHex(15));
    }

    @Test (expected = IllegalArgumentException.class)
    public void Init_fromInt_InvalidArg_Throw() {
        Octave.fromInt(53);
    }

    @Test (expected = IllegalArgumentException.class)
    public void Init_fromHex_InvalidArg_$TooLow$_Throw() {
        Octave.fromHex(7);
    }

    @Test (expected = IllegalArgumentException.class)
    public void Init_fromHex_InvalidArg_$TooHigh$_Throw() {
        Octave.fromHex(132);
    }


    //
    //  ~  VALUE  ~  //
    //

    @Test
    public void SingleMethod_semitoneValue_ValidInst_ValidReturnedValue() {
        assertEquals(60, Octave.FIVE.semitoneValue());
    }

    @Test
    public void SingleMethod_diatonicToneValue_ValidInst_ValidReturnedValue() {
        assertEquals(35, Octave.FIVE.diatonicToneValue());
    }
}
