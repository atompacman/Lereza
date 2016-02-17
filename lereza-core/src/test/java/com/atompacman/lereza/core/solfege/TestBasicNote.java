package com.atompacman.lereza.core.solfege;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.atompacman.lereza.core.theory.Note;
import com.atompacman.lereza.core.theory.Pitch;
import com.atompacman.lereza.core.theory.Value;
import com.atompacman.toolkat.test.AbstractTest;

public class TestBasicNote extends AbstractTest {

    //====================================== UNIT TESTS ==========================================\\

    @Test
    public void staticConstructorsAreEquivalent() {
        // Note valueOf(Pitch pitch, Value value)
        Note a = Note.of(Pitch.valueOf("B3"), Value.QUARTER);
        
        // Note valueOf(byte hexNote, Value value)
        Note b = Note.of((byte) 59, Value.QUARTER);

        // Note valueOf(String pitch, Value value)
        Note c = Note.of("B3", Value.QUARTER);
        
        assertEquals(a, b);
        assertEquals(b, c);
    }
    
    @Test
    public void testSerializations() {
        Note note = Note.of(Pitch.valueOf("Eb5"), Value.EIGHTH);
        
        assertEquals("Eb5",     note.toString());
        assertEquals("Eb5i",    note.toStaccato());
        assertEquals("Eb5ia76", note.toStaccato((byte) 76));
    }
}
