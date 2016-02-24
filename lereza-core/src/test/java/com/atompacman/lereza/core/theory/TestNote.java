package com.atompacman.lereza.core.theory;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.atompacman.lereza.core.theory.Note;
import com.atompacman.lereza.core.theory.Pitch;
import com.atompacman.lereza.core.theory.RythmnValue;
import com.atompacman.toolkat.test.AbstractTest;

public final class TestNote extends AbstractTest {

    //
    //  ~  SERIALIZATION  ~  //
    //

    @Test
    public void SingleMethod_toString_ValidInst_ValidReturnedValue() {
        Note note = Note.of(Pitch.of("Eb5"), RythmnValue.EIGHTH, Dynamic.of((byte)76));
        assertEquals("Eb5",     note.toString());
        assertEquals("Eb5ia76", note.toStaccato());
    }
}
