package com.atompacman.lereza.core.theory;

import static com.atompacman.lereza.core.theory.AdvancedQuality.AUGMENTED;
import static com.atompacman.lereza.core.theory.AdvancedQuality.DIMINISHED;
import static com.atompacman.lereza.core.theory.Direction.ASCENDING;
import static com.atompacman.lereza.core.theory.Direction.DESCENDING;
import static com.atompacman.lereza.core.theory.IntervalRange.ELEVENTH;
import static com.atompacman.lereza.core.theory.IntervalRange.FIFTH;
import static com.atompacman.lereza.core.theory.IntervalRange.FOURTH;
import static com.atompacman.lereza.core.theory.IntervalRange.NINTH;
import static com.atompacman.lereza.core.theory.IntervalRange.OCTAVE;
import static com.atompacman.lereza.core.theory.IntervalRange.SECOND;
import static com.atompacman.lereza.core.theory.IntervalRange.SEVENTH;
import static com.atompacman.lereza.core.theory.IntervalRange.SIXTH;
import static com.atompacman.lereza.core.theory.IntervalRange.THIRD;
import static com.atompacman.lereza.core.theory.IntervalRange.TWELVTH;
import static com.atompacman.lereza.core.theory.IntervalRange.UNISON;
import static com.atompacman.lereza.core.theory.Quality.MAJOR;
import static com.atompacman.lereza.core.theory.Quality.MINOR;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public final class TestInterval {

    //
    //  ~  INIT  ~  //
    //

    @Test
    public void Init_withSemitoneValue_ValidArgs_ValidReturnedValue() {
        runSingleWithSemitoneValueMethodTest(10, 
                Interval.of(MINOR, SEVENTH));
        
        runSingleWithSemitoneValueMethodTest(6, 
                Interval.of(AUGMENTED, FOURTH),
                Interval.of(DIMINISHED, FIFTH));

        runSingleWithSemitoneValueMethodTest(12, 
                Interval.of(OCTAVE));

        runSingleWithSemitoneValueMethodTest(-14, 
                Interval.of(DESCENDING, MAJOR, NINTH));
    }

    private static void runSingleWithSemitoneValueMethodTest(int semitoneValue,Interval...expected){
        List<Interval> possibleIntervals = Interval.withSemitoneValue(semitoneValue);
        assertEquals(expected.length, possibleIntervals.size());
        for (int i = 0; i < expected.length; ++i) {
            assertEquals(expected[i], possibleIntervals.get(i));
        }
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void Init_InvalidArgs_$Invalid_Quality_Range_Combination$__Throw() {
        Interval.of(MAJOR, FIFTH);
    }

    @Test (expected = IllegalArgumentException.class)
    public void Init_InvalidArgs_$Invalid_Direction_Range_Combination$__Throw() {
        Interval.of(ASCENDING, MAJOR, UNISON);
    }


    //
    //  ~  VALUE  ~  //
    //

    @Test
    public void SingleMethod_semitoneValue_ValidArgs_ValidReturnedValue() {
        assertEquals( 11, Interval.of(ASCENDING,  MAJOR,     SEVENTH ).semitoneValue());
        assertEquals(-18, Interval.of(DESCENDING, AUGMENTED, ELEVENTH).semitoneValue());
    }

    @Test
    public void SingleMethod_diatonicToneValue_ValidArgs_ValidReturnedValue() {
        assertEquals(-5, Interval.of(DESCENDING, MINOR, SIXTH).diatonicToneValue());
    }


    //
    //  ~  BETWEEN  ~  //
    //
    
    @Test
    public void SingleMethod_between_$pitches$_ValidArgs_ValidReturnedValue() {
        assertEquals(Interval.of(MAJOR, SECOND), Interval.between(
                Pitch.of("A5"), 
                Pitch.of("B5")));

        assertEquals(Interval.of(AUGMENTED, FIFTH), Interval.between(
                Pitch.of("Gb3"), 
                Pitch.of("D4")));
        
        assertEquals(Interval.of(DESCENDING, AUGMENTED, TWELVTH), Interval.between(
                Pitch.of("F#6"), 
                Pitch.of("Bb4")));
    }

    @Test
    public void SingleMethod_between_$tones$_ValidArgs_ValidReturnedValue() {
        assertEquals(Interval.of(ASCENDING, MAJOR, THIRD),        Interval.between(
                Tone.of("A"), 
                ASCENDING, 
                Tone.of("C#")));
        
        assertEquals(Interval.of(DESCENDING, MINOR, SIXTH),       Interval.between(
                Tone.of("A"), 
                DESCENDING, 
                Tone.of("C#")));

        assertEquals(Interval.of(AUGMENTED, FIFTH),               Interval.between(
                Tone.of("D"), 
                ASCENDING, 
                Tone.of("A#")));
        
        assertEquals(Interval.of(DESCENDING, DIMINISHED, FOURTH), Interval.between(
                Tone.of("D"), 
                DESCENDING, 
                Tone.of("A#")));
    }
}
