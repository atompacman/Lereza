package com.atompacman.lereza.core.piece;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.atompacman.lereza.core.solfege.Pitch;
import com.atompacman.lereza.core.solfege.Value;
import com.atompacman.toolkat.test.AbstractTest;

public class TestStack extends AbstractTest {

    //======================================= FIELDS =============================================\\

    private NoteStackBuilder builder;



    //===================================== BEFORE TESTS =========================================\\

    @Before
    public void beforeTest() {
        builder = new NoteStackBuilder();
    }


    //====================================== UNIT TESTS ==========================================\\

    @Test
    public void addSimpleStartingUntiedNotes() {
        builder.add("C5").add("E5").add("G5");

        NoteStack stack = buildAndPerformBasicAssertions(3,0,0,0);

        Set<Note> set = stack.getStartingUntiedNotes();
        assertTrue(set.contains(Note.valueOf(Pitch.valueOf("E5"), Value.QUARTER)));
        assertTrue(stack.containsNoteOfPitch(Pitch.valueOf("E5")));
        Note note = stack.getNote(Pitch.valueOf("E5"));
        assertEquals(NoteStackBuilder.DEFAULT_VALUE, note.getValue());
        assertEquals(NoteStackBuilder.DEFAULT_VELOCITY, stack.getDynamic().getVelocity());
    }

    @Test
    public void addSimpleStartingTiedNotes() {
        Note a   = Note.valueOf("C5", Value.EIGHTH);
        Note b4a = Note.valueOf("C5", Value.EIGHTH);
        b4a.tieTo(a);
        
        Note b   = Note.valueOf("E5", Value.EIGHTH);
        Note b4b = Note.valueOf("E5", Value.EIGHTH);
        b4b.tieTo(b);
        
        builder.add(a).add(b);

        NoteStack stack = buildAndPerformBasicAssertions(0,2,0,0);

        Set<Note> set = stack.getStartingNotes();
        assertTrue(set.contains(Note.valueOf(Pitch.valueOf("E5"), Value.EIGHTH)));
        assertTrue(stack.containsNoteOfPitch(Pitch.valueOf("E5")));
        Note note = stack.getNote(Pitch.valueOf("E5"));
        assertEquals(Value.EIGHTH, note.getValue());
        assertEquals(NoteStackBuilder.DEFAULT_VELOCITY, stack.getDynamic().getVelocity());
    }
    
    @Test
    public void addSimpleStartedUntiedNotes() {
        builder.isStarting(false).add("F3").add("Ab8").add("G#2");

        NoteStack stack = buildAndPerformBasicAssertions(0,0,3,0);

        Set<Note> set = stack.getStartedNotes();
        assertTrue(set.contains(Note.valueOf(Pitch.valueOf("F3"), Value.QUARTER)));
        assertTrue(stack.containsNoteOfPitch(Pitch.valueOf("Ab8")));
        Note note = stack.getNote(Pitch.valueOf("G#2"));
        assertEquals(NoteStackBuilder.DEFAULT_VALUE, note.getValue());
    }

    @Test
    public void addStartingUntiedNotes() {
        builder.velocity(50) .value(Value.EIGHTH)    .add(Pitch.valueOf("B1"))
                             .value(Value.SIXTYFORTH).add(Pitch.valueOf("D1"))
               .velocity(100)                        .add(Pitch.valueOf("F1"))
                                                     .add(Pitch.valueOf("A1"));

        NoteStack stack = buildAndPerformBasicAssertions(4,0,0,0);

        assertEquals(75, stack.getDynamic().getVelocity());
        assertEquals(Value.EIGHTH,     stack.getNote(Pitch.valueOf("B1")).getValue());
        assertEquals(Value.SIXTYFORTH, stack.getNote(Pitch.valueOf("F1")).getValue());
    }

    @Test
    public void addUntiedNotes() {
        builder.isStarting(true) .velocity(50) .value(Value.EIGHTH)    .add(Pitch.valueOf("B1"))
                                               .value(Value.SIXTYFORTH).add(Pitch.valueOf("D1"))
               .isStarting(false).velocity(100)                        .add(Pitch.valueOf("F1"))
                                               .value(Value.HALF)      .add(Pitch.valueOf("A1"));

        NoteStack stack = buildAndPerformBasicAssertions(2,0,2,0);

        assertEquals(50, stack.getDynamic().getVelocity());
        assertEquals(Value.EIGHTH,     stack.getNote(Pitch.valueOf("B1")).getValue());
        assertEquals(Value.SIXTYFORTH, stack.getNote(Pitch.valueOf("F1")).getValue());
    }

    @Test
    public void addNotesOfEveryStatus() {
        builder.isStarting(true) .velocity(50) .value(Value.EIGHTH)    .add(Pitch.valueOf("B1"))
                                               .value(Value.SIXTYFORTH).add(Pitch.valueOf("D1"))
               .isStarting(false).velocity(100)                        .add(Pitch.valueOf("F1"))
                                               .value(Value.HALF)      .add(Pitch.valueOf("A1"));

        Note b4a = Note.valueOf("G4", Value.WHOLE);
        Note a   = Note.valueOf("G4", Value.WHOLE);
        b4a.tieTo(a);
        
        Note b4b = Note.valueOf("Eb3", Value.SIXTEENTH);
        Note b   = Note.valueOf("Eb3", Value.SIXTEENTH);
        b4b.tieTo(b);
        
        builder.velocity(50).add(a).isStarting(true).add(b);
        
        NoteStack stack = buildAndPerformBasicAssertions(2,1,2,1);

        assertEquals(50, stack.getDynamic().getVelocity());
        assertEquals(Value.EIGHTH,     stack.getNote(Pitch.valueOf("B1")).getValue());
        assertEquals(Value.SIXTYFORTH, stack.getNote(Pitch.valueOf("F1")).getValue());
    }
    
    @Test
    public void anomalyWhenAddingMultipleTimeTheSamePitchI() {
        builder.add(Pitch.valueOf("C5")).add(Pitch.valueOf("C5"));
        buildAndPerformBasicAssertions(1,0,0,0);
    }

    @Test
    public void anomalyWhenAddingMultipleTimeTheSamePitchII() {
        builder.add(Pitch.valueOf("C5")).isStarting(false).add(Pitch.valueOf("C5"));
        buildAndPerformBasicAssertions(0,0,0,0);
    }

    @Test
    public void anomalyVelocityIsNegative() {
        builder.velocity(-32).add(Pitch.valueOf("C5"));
        buildAndPerformBasicAssertions(1,0,0,0);
    }

    @Test
    public void anomalyVelocityIsGreaterThan255() {
        builder.velocity(325).add(Pitch.valueOf("C5"));
        buildAndPerformBasicAssertions(1,0,0,0);
    }

    @Test
    public void cannotGetStackDynamicWhenThereAreNoStartingNotes() {
        expect("Cannot get dynamic of a stack with no starting notes");
        builder.isStarting(false).add(Pitch.valueOf("F3"));
        buildAndPerformBasicAssertions(0,0,1,0).getDynamic();
    }

    @Test
    public void emptyStack() {
        buildAndPerformBasicAssertions(0,0,0,0);
    }

    @Test
    public void missingNote() {
        expect("Stack does not contain a note of pitch \"C3\".");
        NoteStack stack = buildAndPerformBasicAssertions(0,0,0,0);
        assertTrue(!stack.containsNoteOfPitch(Pitch.valueOf("C3")));
        stack.getNote(Pitch.valueOf("C3"));
    }

    @Test
    public void staccatoSerialization() {
        builder.add(Pitch.valueOf("Ab4"), Value.QUARTER,   70, true)
               .add(Pitch.valueOf("C3"),  Value.SIXTEENTH, 80, true)
               .add(Pitch.valueOf("F#7"), Value.HALF,      30, true)
               .add(Pitch.valueOf("Bb1"), Value.HALF,      32, false);

        Note b4a = Note.valueOf("Fb4", Value.QUARTER);
        Note a   = Note.valueOf("Fb4", Value.QUARTER);
        b4a.tieTo(a);
        builder.velocity(60).add(a);
        
        Note bb4b = Note.valueOf("B4", Value.THIRTYSECONTH);
        Note b4b  = Note.valueOf("B4", Value.THIRTYSECONTH);
        Note b    = Note.valueOf("B4", Value.THIRTYSECONTH);
        bb4b.tieTo(b4b).tieTo(b);
        builder.add(b4b);
        
        NoteStack stack = buildAndPerformBasicAssertions(3,2,1,0);

        assertEquals("Ab4qa60+C3sa60+F#7ha60+Fb4-qa60+B4-t-a60", stack.toStaccato());
    }


    //--------------------------------------- HELPERS --------------------------------------------\\

    private NoteStack buildAndPerformBasicAssertions(int numUntiedStartingNotes,
                                                           int numTiedStartingNotes,
                                                           int numUntiedStartedNotes,
                                                           int numTiedStartedNotes) {
        
        // Build note stack
        NoteStack stack = builder.build();

        // Get sets
        int numStartingNotes = numTiedStartingNotes + numUntiedStartingNotes;
        int numStartedNotes  = numTiedStartedNotes  + numUntiedStartedNotes;
        int numPlayingNotes  = numStartingNotes     + numStartedNotes;
        assertEquals(numUntiedStartingNotes, stack.getStartingUntiedNotes().size());
        assertEquals(numStartingNotes,       stack.getStartingNotes().size());
        assertEquals(numStartedNotes,        stack.getStartedNotes().size());
        assertEquals(numPlayingNotes,        stack.getPlayingNotes().size());

        // Count
        assertEquals(numUntiedStartingNotes, stack.countStartingUntiedNotes());
        assertEquals(numStartingNotes,       stack.countStartingNotes());
        assertEquals(numStartedNotes,        stack.countStartedNotes());
        assertEquals(numPlayingNotes,        stack.countPlayingNotes());

        // Get dynamic
        if (numStartingNotes != 0) {
            assertNotNull(stack.getDynamic());
        }

        // State
        assertEquals(numUntiedStartingNotes != 0, stack.hasStartingUntiedNotes());
        assertEquals(numStartingNotes       != 0, stack.hasStartingNotes());
        assertEquals(numStartedNotes        != 0, stack.hasStartedNotes());
        assertEquals(numPlayingNotes        != 0, stack.hasPlayingNotes());

        return stack;
    }
}
