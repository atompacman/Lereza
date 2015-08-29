package com.atompacman.lereza.core.piece;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.atompacman.lereza.core.solfege.Pitch;
import com.atompacman.lereza.core.solfege.Value;
import com.atompacman.toolkat.test.AbstractTest;

public class TestNote extends AbstractTest {

    //====================================== UNIT TESTS ==========================================\\

    @Test
    public void staticConstructorsAreEquivalent() {
        // Note valueOf(Pitch pitch, Value value)
        Note a = Note.valueOf(Pitch.valueOf("B3"), Value.QUARTER);

        // Note valueOf(Pitch pitch, List<Value> values)
        Note b = Note.valueOf(Pitch.valueOf("B3"), Arrays.asList(Value.QUARTER));
        
        // Note valueOf(byte hexNote, Value value)
        Note c = Note.valueOf((byte) 59, Value.QUARTER);

        // Note valueOf(Pitch pitch, List<Value> values)
        Note d = Note.valueOf(Pitch.valueOf("B3"), Arrays.asList(Value.QUARTER));

        // Note valueOf(String pitch, Value value)
        Note e = Note.valueOf("B3", Value.QUARTER);

        // Note valueOf(String pitch, List<Value> values)
        Note f = Note.valueOf("B3", Arrays.asList(Value.QUARTER));
        
        assertEquals(a, b);
        assertEquals(b, c);
        assertEquals(c, d);
        assertEquals(d, e);
        assertEquals(e, f);
    }
    
    @Test
    public void cannotInstantiateANoteWithNoValues() {
        expect("Value list cannot be empty");
        Note.valueOf(Pitch.valueOf("C2"), new ArrayList<Value>());
    }
    
    @Test
    public void testGetTotalValue() {
        List<Value> values = Arrays.asList(Value.HALF, Value.QUARTER, Value.THIRTYSECONTH);
        assertEquals(50, Note.valueOf(Pitch.valueOf("C2"), values).getTotalValue());
    }
    
    @Test
    public void testSerializations() {
        List<Value> values = Arrays.asList(Value.QUARTER, Value.EIGHTH, Value.SIXTYFORTH);
        Note note = Note.valueOf(Pitch.valueOf("Eb5"), values);
        
        assertEquals("Eb5",               note.toString());
        assertEquals("Eb5(1/4+1/8+1/64)", note.toCompleteString());
        assertEquals("Eb5qix",            note.toStaccato());
        assertEquals("Eb5qixA76",         note.toStaccato((byte) 76));
    }
}
