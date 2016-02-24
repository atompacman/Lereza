package com.atompacman.lereza.core.theory;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.atompacman.lereza.core.theory.DiatonicTones;
import com.atompacman.lereza.core.theory.Direction;
import com.atompacman.lereza.core.theory.Pitch;
import com.atompacman.lereza.core.theory.Tone;

public final class TestDiatonicTones {

    //
    //  ~  BETWEEN  ~  //
    //

    @Test
    public void SingleMethod_between_$tones$_ValidArgs_ValidReturnedValue() {
        assertEquals(2, DiatonicTones.between(Tone.of("Cb"), Direction.ASCENDING,  Tone.of("E#")));
        assertEquals(2, DiatonicTones.between(Tone.of("E#"), Direction.DESCENDING, Tone.of("Cb")));
        
        assertEquals(6, DiatonicTones.between(Tone.of("F"),  Direction.ASCENDING,  Tone.of("E#")));
        assertEquals(1, DiatonicTones.between(Tone.of("F"),  Direction.DESCENDING, Tone.of("E#")));
    }

    @Test
    public void SingleMethod_between_$pitches$_ValidArgs_ValidReturnedValue() {
        assertEquals(  4, DiatonicTones.between(Pitch.of("A2"), Pitch.of("Eb3")));
        assertEquals(-20, DiatonicTones.between(Pitch.of("B6"), Pitch.of("C4" )));
        assertEquals(  0, DiatonicTones.between(Pitch.of("A2"), Pitch.of("A2" )));
    }


    //
    //  ~  NORMALIZE  ~  //
    //

    @Test
    public void SingleMethod_normalize_ValidArgs_ValidReturnedValue() {
        assertEquals(DiatonicTones.normalize(  4), 4);
        assertEquals(DiatonicTones.normalize( 23), 2);
        assertEquals(DiatonicTones.normalize(- 1), 6);
        assertEquals(DiatonicTones.normalize( 12), 5);
        assertEquals(DiatonicTones.normalize(-34), 1);
    }
}
