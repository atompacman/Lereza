package com.fxguild.lereza.common.theory;

import static com.fxguild.lereza.common.theory.Accidental.FLAT;
import static com.fxguild.lereza.common.theory.Accidental.NONE;
import static com.fxguild.lereza.common.theory.Accidental.SHARP;
import static com.fxguild.lereza.common.theory.AdvancedQuality.AUGMENTED;
import static com.fxguild.lereza.common.theory.AdvancedQuality.DIMINISHED;
import static com.fxguild.lereza.common.theory.Direction.ASCENDING;
import static com.fxguild.lereza.common.theory.Direction.DESCENDING;
import static com.fxguild.lereza.common.theory.IntervalRange.FIFTH;
import static com.fxguild.lereza.common.theory.IntervalRange.FOURTH;
import static com.fxguild.lereza.common.theory.IntervalRange.SIXTH;
import static com.fxguild.lereza.common.theory.IntervalRange.THIRD;
import static com.fxguild.lereza.common.theory.NoteLetter.A;
import static com.fxguild.lereza.common.theory.NoteLetter.B;
import static com.fxguild.lereza.common.theory.NoteLetter.C;
import static com.fxguild.lereza.common.theory.NoteLetter.D;
import static com.fxguild.lereza.common.theory.NoteLetter.E;
import static com.fxguild.lereza.common.theory.NoteLetter.F;
import static com.fxguild.lereza.common.theory.NoteLetter.G;
import static com.fxguild.lereza.common.theory.Quality.MINOR;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

public final class TestTone {

    //
    //  ~  INIT  ~  //
    //

    @Test
    public void Init_$str$_ValidArg_ValidInstState() {
        assertEquals(Tone.of(G, FLAT), Tone.of("Gb"));
    }

    @Test
    public void Init_$withSemitoneValueOf$_ValidArg_ValidInstances() {
        assertEquals(Arrays.asList(Tone.of("F#"), Tone.of("Gb")), Tone.withSemitoneValueOf(6));
    }

    @Test
    public void Init_$semitone_and_diatonic_tone$_ValidArg_ValidInstances() {
        assertEquals(Tone.of("G"),  Tone.of( 7,  4));
        assertEquals(Tone.of("E#"), Tone.of(17, -5));
    }

    @Test
    public void Init_$thatIsMoreCommonForSemitoneValue$_ValidArg_ValidInstances() {
        assertEquals(Tone.of("E"),  Tone.thatIsMoreCommonForSemitoneValue( 4));
        assertEquals(Tone.of("Bb"), Tone.thatIsMoreCommonForSemitoneValue(10));
        assertEquals(Tone.of("C#"), Tone.thatIsMoreCommonForSemitoneValue( 1));
        assertEquals(Tone.of("Ab"), Tone.thatIsMoreCommonForSemitoneValue( 8));
        assertEquals(Tone.of("C"),  Tone.thatIsMoreCommonForSemitoneValue( 0));
    }

    @Test
    public void Init_Equivalence() {
        Tone a = Tone.of("B");
        Tone b = Tone.of(B);
        Tone c = Tone.of(B, NONE);
        Tone d = Tone.of(11, 6);
        Tone e = Tone.thatIsMoreCommonForSemitoneValue(11);
        Tone f = Tone.fromNoteAndSemitoneValue(B, 11);

        assertEquals(a, b);
        assertEquals(b, c);
        assertEquals(c, d);
        assertEquals(d, e);
        assertEquals(e, f);
    }


    //
    //  ~  WITH SWITCHED ALTERATION  ~  //
    //

    
    @Test
    public void SingleMethod_withSwitchedAlteration_ValidInst_ValidReturnedValue() {
        assertEquals("Error switching alteration", 
                Tone.of(A, FLAT), 
                Tone.of(G, SHARP).withSwitchedAlteration());

        assertEquals("Error switching alteration", 
                Tone.of(B, SHARP), 
                Tone.of(C, FLAT).withSwitchedAlteration());
    }


    //
    //  ~  AFTER INTERVAL  ~  //
    //

    @Test
    public void SingleMethod_afterInterval_ValidInst_ValidReturnedValue() {
        assertEquals(Tone.of    (G), 
                     Tone.of    (C).afterInterval(
                     Interval.of(FIFTH)));

        assertEquals(Tone.of    (G, FLAT), 
                     Tone.of    (E, FLAT).afterInterval(
                     Interval.of(MINOR, THIRD)));

        assertEquals(Tone.of    (F, SHARP), 
                     Tone.of    (B, FLAT ).afterInterval(
                     Interval.of(AUGMENTED, FIFTH))); 
        
        assertEquals(Tone.of    (G, FLAT), 
                     Tone.of    (B, FLAT).afterInterval(
                     Interval.of(MINOR, SIXTH))); 
        
        assertEquals(Tone.of    (F), 
                     Tone.of    (F).afterInterval(
                     Interval.of(DESCENDING, DIMINISHED, FOURTH)).afterInterval(
                     Interval.of(ASCENDING,  DIMINISHED, FOURTH)));
    }


    //
    //  ~  VALUE  ~  //
    //

    @Test
    public void SingleMethod_semitoneValue_ValidInst_ValidReturnedValue() {
        assertEquals(10, Tone.of(B, FLAT).semitoneValue());
    }


    //
    //  ~  SERIALIZATION  ~  //
    //

    @Test
    public void SingleMethod_toString_ValidInst_ValidReturnedValue() {
        assertEquals("D#", Tone.of(D, SHARP).toString());
        assertEquals("Fb", Tone.of(F, FLAT ).toString());
        assertEquals("B",  Tone.of(B       ).toString());
    }
}
