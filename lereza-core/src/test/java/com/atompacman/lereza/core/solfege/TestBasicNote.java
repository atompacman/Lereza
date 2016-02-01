package com.atompacman.lereza.core.solfege;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.atompacman.toolkat.test.AbstractTest;

public class TestBasicNote extends AbstractTest {

    //====================================== UNIT TESTS ==========================================\\

    @Test
    public void staticConstructorsAreEquivalent() {
        // Note valueOf(Pitch pitch, Value value)
        BasicNote a = BasicNote.valueOf(Pitch.valueOf("B3"), Value.QUARTER);
        
        // Note valueOf(byte hexNote, Value value)
        BasicNote b = BasicNote.valueOf((byte) 59, Value.QUARTER);

        // Note valueOf(String pitch, Value value)
        BasicNote c = BasicNote.valueOf("B3", Value.QUARTER);
        
        assertEquals(a, b);
        assertEquals(b, c);
    }
    
    @Test
    public void testSerializations() {
        BasicNote note = BasicNote.valueOf(Pitch.valueOf("Eb5"), Value.EIGHTH);
        
        assertEquals("Eb5",     note.toString());
        assertEquals("Eb5i",    note.toStaccato());
        assertEquals("Eb5ia76", note.toStaccato((byte) 76));
    }
}
