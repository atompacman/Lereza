package com.fxguild.lereza.common.theory;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public final class TestPitch {

    //
    //  ~  INIT  ~  //
    //

    @Test
    public void Init_thatIsMoreCommonForHexValue_ValidArg_ValidInstState() {
        assertEquals(Pitch.of("A0"),  Pitch.thatIsMoreCommonForHexValue((byte)  21));
        assertEquals(Pitch.of("G2"),  Pitch.thatIsMoreCommonForHexValue((byte)  43));
        assertEquals(Pitch.of("F#8"), Pitch.thatIsMoreCommonForHexValue((byte) 114));
    }

    @Test
    public void Init_Equivalence() {
        Pitch a = Pitch.of(Tone.of("Bb"), Octave.FIVE);
        Pitch b = Pitch.of(NoteLetter.B, Accidental.FLAT, Octave.FIVE);
        Pitch c = Pitch.of("Bb5");
        Pitch d = Pitch.thatIsMoreCommonForHexValue((byte) 82);
        assertEquals(a, b);
        assertEquals(b, c);
        assertEquals(c, d);
    }


    //
    //  ~  VALUE  ~  //
    //
    
    @Test
    public void SingleMethod_semitoneValue_ValidInst_ValidReturnedValue() {
        assertEquals(25, Pitch.of("C#2").semitoneValue());
    }
}
