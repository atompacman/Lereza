package com.atompacman.lereza.core.piece;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.atompacman.lereza.core.solfege.Pitch;
import com.atompacman.lereza.core.solfege.Value;

public class TestNote {

    //====================================== UNIT TESTS ==========================================\\

    @Test
    public void staticConstructorsAreEquivalent() {
        // Note valueOf(Pitch pitch, Value value)
        Note a = Note.valueOf(Pitch.valueOf("B3"), Value.QUARTER);

        // Note valueOf(byte hexNote, Value value, boolean isTied)
        Note b = Note.valueOf((byte) 59, Value.QUARTER, false);

        // Note valueOf(Pitch pitch, Value value, boolean isTied)
        Note c = Note.valueOf(Pitch.valueOf("B3"), Value.QUARTER, false);

        assertEquals(a, b);
        assertEquals(b, c);
    }
}
