package com.atompacman.lereza.core.solfege;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.atompacman.lereza.core.piece.TiedNote;
import com.atompacman.toolkat.test.AbstractTest;

public class TestNote extends AbstractTest {

    //====================================== UNIT TESTS ==========================================\\

    @Test
    public void staticConstructorsAreEquivalent() {
        // Note valueOf(Pitch pitch, Value value)
        TiedNote a = TiedNote.valueOf(Pitch.valueOf("B3"), Value.QUARTER);
        
        // Note valueOf(byte hexNote, Value value)
        TiedNote b = TiedNote.valueOf((byte) 59, Value.QUARTER);

        // Note valueOf(String pitch, Value value)
        TiedNote c = TiedNote.valueOf("B3", Value.QUARTER);
        
        assertEquals(a, b);
        assertEquals(b, c);
    }
    
    @Test
    public void testSerializations() {
        TiedNote note = TiedNote.valueOf(Pitch.valueOf("Eb5"), Value.EIGHTH);
        
        assertEquals("Eb5",     note.toString());
        assertEquals("Eb5i",    note.toStaccato());
        assertEquals("Eb5ia76", note.toStaccato((byte) 76));
    }
}
