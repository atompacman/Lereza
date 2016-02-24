package com.atompacman.lereza.core.theory;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.atompacman.lereza.core.theory.Direction;
import com.atompacman.lereza.core.theory.Pitch;
import com.atompacman.lereza.core.theory.Semitones;
import com.atompacman.lereza.core.theory.Tone;

public final class TestSemitones {

    //
    //  ~  BETWEEN  ~  //
    //
    @Test
    public void SingleMethod_between_$pitches$_ValidInst_ValidReturnedValue() {
        assertEquals(  5, Semitones.between(Pitch.of("G4"),  Pitch.of("C5")));
        assertEquals(-26, Semitones.between(Pitch.of("Bb4"), Pitch.of("G#2")));
    }

    @Test
    public void SingleMethod_between_$tones$_ValidInst_ValidReturnedValue() {
        assertEquals( 0, Semitones.between(Tone.of("E"),  Direction.ASCENDING,  Tone.of("Fb")));
        assertEquals( 0, Semitones.between(Tone.of("E"),  Direction.STRAIGHT,   Tone.of("Fb")));

        assertEquals( 5, Semitones.between(Tone.of("Ab"), Direction.ASCENDING,  Tone.of("C#")));
        assertEquals( 7, Semitones.between(Tone.of("Ab"), Direction.DESCENDING, Tone.of("C#")));

        assertEquals( 2, Semitones.between(Tone.of("D"),  Direction.ASCENDING,  Tone.of("E")));
        assertEquals(10, Semitones.between(Tone.of("D"),  Direction.DESCENDING, Tone.of("E")));
    }


    //
    //  ~  NORMALIZE  ~  //
    //

    @Test
    public void SingleMethod_normalize_ValidInst_ValidReturnedValue() {
        assertEquals( 4, Semitones.normalize(  4));
        assertEquals(11, Semitones.normalize( 23));
        assertEquals(11, Semitones.normalize(- 1));
        assertEquals( 0, Semitones.normalize( 12));
        assertEquals( 2, Semitones.normalize(-34));
    }
}
