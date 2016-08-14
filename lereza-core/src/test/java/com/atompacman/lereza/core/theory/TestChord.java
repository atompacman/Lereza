package com.atompacman.lereza.core.theory;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.atompacman.lereza.core.theory.Chord;
import com.atompacman.lereza.core.theory.ChordType;
import com.atompacman.lereza.core.theory.Tone;

public final class TestChord {

    //
    //  ~  INIT  ~  //
    //
    
    @Test
    public void Init_ValidArg_ValidInstState() {
        Chord chord = Chord.of("Bm");
        assertEquals(chord.getTone(),      Tone.of("B"));
        assertEquals(chord.getChordType(), ChordType.of("m"));

        chord = Chord.of("D#sus2");
        assertEquals(chord.getTone(),      Tone.of("D#"));
        assertEquals(chord.getChordType(), ChordType.of("sus2"));
    }
}
