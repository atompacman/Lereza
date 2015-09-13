package com.atompacman.lereza.core.piece;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.atompacman.lereza.core.solfege.Value;
import com.atompacman.toolkat.test.AbstractTest;

public class TestNote extends AbstractTest {

    //================================== FUNCTIONNAL TESTS =======================================\\

    @Test
    public void testTieTo() {
        Note a = Note.valueOf("E7", Value.QUARTER);
        Note b = Note.valueOf("E7", Value.SIXTYFORTH);
        
        assertEquals(null, a.getPrevTiedNote());
        assertEquals(null, a.getNextTiedNote());
        assertEquals(null, b.getPrevTiedNote());
        assertEquals(null, b.getNextTiedNote());
        assertFalse(a.isTied());
        assertFalse(b.isTied());

        a.tieTo(b);
        
        assertEquals(null, a.getPrevTiedNote());
        assertEquals(b,    a.getNextTiedNote());
        assertEquals(a,    b.getPrevTiedNote());
        assertEquals(null, b.getNextTiedNote());
        assertFalse(a.isTied());
        assertTrue(b.isTied());
    }
    
    @Test
    public void testSerialization() {
        Note a = Note.valueOf("E7", Value.WHOLE);
        Note b = Note.valueOf("E7", Value.SIXTYFORTH);
        Note c = Note.valueOf("E7", Value.QUARTER);
        
        a.tieTo(b).tieTo(c);
        
        assertEquals("E7-x-a5", b.toStaccato((byte)5));
    }
    
    
    
    //================================== INCORRECT USE CASES =====================================\\

    @Test
    public void tieNoteToItself() {
        expect("Cannot tie a note to itself");
        Note a = Note.valueOf("E7", Value.QUARTER);
        a.tieTo(a);
    }
    
    @Test
    public void tieNotesOfDifferentPitch() {
        expect("Cannot tie notes of different pitch");
        Note a = Note.valueOf("E7",  Value.QUARTER);
        Note b = Note.valueOf("Gb3", Value.HALF);
        a.tieTo(b);
    }
}
