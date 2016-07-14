package com.fxguild.lereza.common.theory;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public final class TestCircleOfFifth {

    //
    //  ~  KEY ARMOR  ~  //
    //

    @Test
    public void SingleMethod_accidentalOfKey_ValidArg_ValidReturnValue() {
        assertEquals(CircleOfFifths.accidentalOfKey(Key.of("Bb")), Accidental.FLAT);
        assertEquals(CircleOfFifths.accidentalOfKey(Key.of("Em")), Accidental.SHARP);
        assertEquals(CircleOfFifths.accidentalOfKey(Key.of("Am")), Accidental.NONE);
    }

    @Test
    public void SingleMethod_numAccidentalsOfKey_ValidArg_ValidReturnValue() {
        assertEquals(CircleOfFifths.numAccidentalsOfKey(Key.of("Bb")), 2);
        assertEquals(CircleOfFifths.numAccidentalsOfKey(Key.of("Em")), 1);
        assertEquals(CircleOfFifths.numAccidentalsOfKey(Key.of("Am")), 0);
    }


    //
    //  ~  POSITION IN ORDERS  ~  //
    //
    
    @Test
    public void SingleMethod_positionInOrderOfX_ValidArg_ValidReturnValue() {
        assertEquals(CircleOfFifths.positionInOrderOfSharps(Tone.of("G#")), 2);
        assertEquals(CircleOfFifths.positionInOrderOfSharps(Tone.of("B#")), 6);
        assertEquals(CircleOfFifths.positionInOrderOfFlats (Tone.of("Eb")), 1);
    }

    @Test (expected = IllegalArgumentException.class)
    public void SingleMethod_positionInOrderOfSharps_InvalidArg_Excep() {
        CircleOfFifths.positionInOrderOfSharps(Tone.of("Bb"));
    }

    @Test (expected = IllegalArgumentException.class)
    public void SingleMethod_positionInOrderOfFlats_InvalidArg_Excep() {
        CircleOfFifths.positionInOrderOfFlats(Tone.of("B"));
    }


    //
    //  ~  TONE AT POSITION  ~  //
    //
    
    @Test
    public void SingleMethod_toneAtPosition_ValidArg_ValidReturnValue() {
        assertEquals(CircleOfFifths.toneAtPosition(3),  Tone.of("A"));
        assertEquals(CircleOfFifths.toneAtPosition(-4), Tone.of("Ab"));
        assertEquals(CircleOfFifths.toneAtPosition(10), Tone.of("A#"));
    }
}