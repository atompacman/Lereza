package com.atompacman.lereza.core.theory;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.atompacman.lereza.core.theory.Note;
import com.atompacman.lereza.core.theory.Pitch;
import com.atompacman.lereza.core.theory.RhythmValue;
import com.atompacman.toolkat.test.AbstractTest;

public final class TestNote extends AbstractTest {

    //
    //  ~  SERIALIZATION  ~  //
    //

    @Test
    public void SingleMethod_toStaccato_ValidInst_ValidReturnedValue() {
        Note note = Note.of(Pitch.of("Eb5"), RhythmValue.EIGHTH, Dynamic.of((byte)76));
        assertEquals("Eb5ia76", note.toStaccato());
    }
}
