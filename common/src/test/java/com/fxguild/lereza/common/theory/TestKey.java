package com.fxguild.lereza.common.theory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public final class TestKey {

    //
    //  ~  INIT  ~  //
    //

    @Test
    public void Init_Equivalence() {
        assertEquals(Key.of("Bbm"), Key.of(Tone.of("Bb"), Quality.MINOR));
        assertEquals(Key.of("E"),   Key.of(Tone.of("E"),  Quality.MAJOR));
    }


    //
    //  ~  CORRESPONDING SCALE  ~  //
    //
    
    @Test
    public void SingleMethod_correspondingScale_ValidArgs_ValidReturnedValue() {
        assertEquals(Scale.of(Tone.of("C"), ScaleType.NATURAL_MINOR), 
                     Key.of("Cm").correspondingScale());
    }

    @Test
    public void SingleMethod_contains_ValidArgs_ValidReturnedValue() {
        Key a = Key.of("Cm");
        List<Tone> tones = new ArrayList<>();
        tones.add(Tone.of("D"));
        tones.add(Tone.of("Eb"));
        assertTrue(a.contains(tones));
        tones.add(Tone.of("E"));
        assertTrue(!a.contains(tones));
    }


    //
    //  ~  ARMOR  ~  //
    //
    
    @Test
    public void SingleMethod_accidental_ValidArgs_ValidReturnedValue() {
        Key key = Key.of("Dm");
        assertEquals(Accidental.FLAT, key.accidental());
        assertEquals(1, key.numAccidentals());
    }


    //
    //  ~  RELATIVE KEY  ~  //
    //
    
    @Test
    public void SingleMethod_relativeKey_ValidArgs_ValidReturnedValue() {
        assertEquals(Key.of("E"),  Key.of("C#m").relativeKey());
        assertEquals(Key.of("Gm"), Key.of("Bb" ).relativeKey());
    }


    //
    //  ~  SERIALIZATION  ~  //
    //
    
    @Test
    public void SingleMethod_toString_ValidInst_ValidReturnedValue() {
        assertEquals("Ab minor", Key.of("Abm").toString());
        assertEquals("D major",  Key.of("D"  ).toString());
    }
}
