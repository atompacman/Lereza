package com.fxguild.lereza.common.theory;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

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
