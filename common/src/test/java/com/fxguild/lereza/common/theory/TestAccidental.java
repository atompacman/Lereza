package com.fxguild.lereza.common.theory;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public final class TestAccidental {

    //
    //  ~  INIT  ~  //
    //
    
    @Test
    public void Init_ValidArg_ValidInstState() {
        assertEquals(Accidental.of(1), Accidental.SHARP);
    }

    @Test (expected = IllegalArgumentException.class)
    public void Init_InvalidArg_Throws() {
        Accidental.of(11251);
    }
}
