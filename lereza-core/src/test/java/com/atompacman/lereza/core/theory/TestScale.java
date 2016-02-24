package com.atompacman.lereza.core.theory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.atompacman.lereza.core.theory.Key;
import com.atompacman.lereza.core.theory.Quality;
import com.atompacman.lereza.core.theory.Scale;
import com.atompacman.lereza.core.theory.ScaleType;
import com.atompacman.lereza.core.theory.Tone;

public final class TestScale {

    //
    //  ~  INIT  ~  //
    //

    @Test
    public void Init_ValidArgs_ValidInstState() {
        Scale scale = Scale.of(Tone.of("G"), ScaleType.MAJOR);
        assertEquals(scale.getTone(),    Tone.of("G"));
        assertEquals(scale.getType(),    ScaleType.MAJOR);
        assertEquals(scale.getKey(),     Key.of("G"));
        assertEquals(scale.getQuality(), Quality.MAJOR);

        scale = Scale.of(Tone.of("Bb"), ScaleType.HARMONIC_MINOR);
        assertEquals(scale.getTone(),    Tone.of("Bb"));
        assertEquals(scale.getType(),    ScaleType.HARMONIC_MINOR);
        assertEquals(scale.getKey(),     Key.of("Bbm"));
        assertEquals(scale.getQuality(), Quality.MINOR);
    }


    //
    //  ~  CONTAINS  ~  //
    //

    @Test
    public void SingleMethod_contains_$singleTone$_ValidInst_ValidReturnedValue() {
        Scale scale = Scale.of(Tone.of("A"), ScaleType.MAJOR);
        assertTrue( scale.contains(Tone.of("A")));
        assertTrue( scale.contains(Tone.of("C#")));
        assertTrue(!scale.contains(Tone.of("G")));

        scale = Scale.of(Tone.of("Db"), ScaleType.HARMONIC_MINOR);
        assertTrue( scale.contains(Tone.of("C")));
        assertTrue( scale.contains(Tone.of("Fb")));
        assertTrue(!scale.contains(Tone.of("Cb")));
    }

    @Test
    public void SingleMethod_contains_$tones$_ValidInst_ValidReturnedValue() {
        Scale a = Scale.of(Tone.of("B"), ScaleType.NATURAL_MINOR);
        List<Tone> tones = new ArrayList<>();
        tones.add(Tone.of("C#"));
        tones.add(Tone.of("D"));
        tones.add(Tone.of("A"));
        assertTrue(a.contains(tones));
        tones.add(Tone.of("B#"));
        assertTrue(!a.contains(tones));
    }
}
