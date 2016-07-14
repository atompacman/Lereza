package com.fxguild.lereza.common.theory;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fxguild.common.test.AbstractTest;

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
