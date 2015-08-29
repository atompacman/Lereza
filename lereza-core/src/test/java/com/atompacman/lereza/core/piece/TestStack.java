package com.atompacman.lereza.core.piece;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.atompacman.lereza.core.solfege.Pitch;
import com.atompacman.lereza.core.solfege.Value;
import com.atompacman.toolkat.test.AbstractTest;

public class TestStack extends AbstractTest {

    //======================================= FIELDS =============================================\\

    private StackBuilder builder;



    //======================================= METHODS ============================================\\

    //===================================== BEFORE TESTS =========================================\\

    @Before
    public void beforeTest() {
        builder = new StackBuilder();
    }


    //====================================== UNIT TESTS ==========================================\\

    @Test
    public void addSimpleStartingNotes() {
        builder.add("C5").add("E5").add("G5");

        Stack<Note> stack = buildAndPerformBasicAssertions(3, 0);

        Set<Note> set = stack.getStartingNotes();
        assertTrue(set.contains(Note.valueOf(Pitch.valueOf("E5"), Value.QUARTER)));
        assertTrue(stack.containsNoteOfPitch(Pitch.valueOf("E5")));
        Note note = stack.getNote(Pitch.valueOf("E5"));
        assertEquals(StackBuilder.DEFAULT_VALUES, note.getValues());
        assertEquals(StackBuilder.DEFAULT_VELOCITY, stack.getDynamic().getVelocity());
    }

    @Test
    public void addSimpleStartedNotes() {
        builder.isStarting(false).add("F3").add("Ab8").add("G#2");

        Stack<Note> stack = buildAndPerformBasicAssertions(0, 3);

        Set<Note> set = stack.getStartedNotes();
        assertTrue(set.contains(Note.valueOf(Pitch.valueOf("F3"), Value.QUARTER)));
        assertTrue(stack.containsNoteOfPitch(Pitch.valueOf("Ab8")));
        Note note = stack.getNote(Pitch.valueOf("G#2"));
        assertEquals(StackBuilder.DEFAULT_VALUES, note.getValues());
    }

    @Test
    public void addStartingNotes() {
        builder.velocity(50) .value(Value.EIGHTH)    .add(Pitch.valueOf("B1"))
                             .value(Value.SIXTYFORTH).add(Pitch.valueOf("D1"))
               .velocity(100)                        .add(Pitch.valueOf("F1"))
                                                     .add(Pitch.valueOf("A1"));

        Stack<Note> stack = buildAndPerformBasicAssertions(4, 0);

        assertEquals(75, stack.getDynamic().getVelocity());
        assertEquals(Value.EIGHTH,     stack.getNote(Pitch.valueOf("B1")).getMainValue());
        assertEquals(Value.SIXTYFORTH, stack.getNote(Pitch.valueOf("F1")).getMainValue());
    }

    @Test
    public void addBothTypeOfNotes() {
        builder.isStarting(true) .velocity(50) .value(Value.EIGHTH)    .add(Pitch.valueOf("B1"))
                                               .value(Value.SIXTYFORTH).add(Pitch.valueOf("D1"))
               .isStarting(false).velocity(100)                        .add(Pitch.valueOf("F1"))
                                               .value(Value.HALF)      .add(Pitch.valueOf("A1"));

        Stack<Note> stack = buildAndPerformBasicAssertions(2, 2);

        assertEquals(50, stack.getDynamic().getVelocity());
        assertEquals(Value.EIGHTH,     stack.getNote(Pitch.valueOf("B1")).getMainValue());
        assertEquals(Value.SIXTYFORTH, stack.getNote(Pitch.valueOf("F1")).getMainValue());
    }

    @Test
    public void anomalyWhenAddingMultipleTimeTheSamePitchI() {
        builder.add(Pitch.valueOf("C5")).add(Pitch.valueOf("C5"));
        buildAndPerformBasicAssertions(1, 0);
    }

    @Test
    public void anomalyWhenAddingMultipleTimeTheSamePitchII() {
        builder.add(Pitch.valueOf("C5")).isStarting(false).add(Pitch.valueOf("C5"));
        buildAndPerformBasicAssertions(0, 1);
    }

    @Test
    public void anomalyVelocityIsNegative() {
        builder.velocity(-32).add(Pitch.valueOf("C5"));
        buildAndPerformBasicAssertions(1, 0);
    }

    @Test
    public void anomalyVelocityIsGreaterThan255() {
        builder.velocity(325).add(Pitch.valueOf("C5"));
        buildAndPerformBasicAssertions(1, 0);
    }

    @Test
    public void cannotGetStackDynamicWhenThereAreNoStartingNotes() {
        expect("Cannot get dynamic of a stack with no starting notes");
        builder.isStarting(false).add(Pitch.valueOf("F3"));
        buildAndPerformBasicAssertions(0, 1).getDynamic();
    }

    @Test
    public void emptyStack() {
        buildAndPerformBasicAssertions(0, 0);
    }

    @Test
    public void missingNote() {
        expect("Does not contain a note of pitch \"C3\".");
        Stack<Note> stack = buildAndPerformBasicAssertions(0, 0);
        assertTrue(!stack.containsNoteOfPitch(Pitch.valueOf("C3")));
        stack.getNote(Pitch.valueOf("C3"));
    }

    @Test
    public void staccatoSerialization() {
        builder.add(Pitch.valueOf("Ab4"), Arrays.asList(Value.QUARTER),            70, true)
               .add(Pitch.valueOf("C3"),  Arrays.asList(Value.SIXTEENTH),          80, true)
               .add(Pitch.valueOf("F#7"), Arrays.asList(Value.HALF, Value.EIGHTH), 30, true)
               .add(Pitch.valueOf("Bb1"), Arrays.asList(Value.HALF),               32, false);

        Stack<Note> stack = buildAndPerformBasicAssertions(3, 1);

        assertEquals("Ab4qA60+C3sA60+F#7hiA60", stack.toStaccato());
    }


    //--------------------------------------- HELPERS --------------------------------------------\\

    private Stack<Note> buildAndPerformBasicAssertions(int numStartingNotes, int numStartedNotes) {
        // Build note stack
        Stack<Note> stack = builder.build();

        // Get sets
        assertEquals(numStartingNotes,                   stack.getStartingNotes().size());
        assertEquals(numStartedNotes,                    stack.getStartedNotes().size());
        assertEquals(numStartingNotes + numStartedNotes, stack.getPlayingNotes().size());

        // Get num
        assertEquals(numStartingNotes,                   stack.getNumStartingNotes());
        assertEquals(numStartedNotes,                    stack.getNumStartedNotes());
        assertEquals(numStartingNotes + numStartedNotes, stack.getNumPlayingNotes());

        // Get dynamic
        if (numStartingNotes != 0) {
            assertNotNull(stack.getDynamic());
        }

        // State
        if (numStartingNotes == 0) {
            assertTrue(!stack.hasStartingNotes());
        } else {
            assertTrue(stack.hasStartingNotes());
        }
        if (numStartedNotes == 0) {
            assertTrue(!stack.hasStartedNotes());
        } else {
            assertTrue(stack.hasStartedNotes());
        }
        if (numStartingNotes + numStartedNotes == 0) {
            assertTrue(!stack.hasPlayingNotes());
        } else {
            assertTrue(stack.hasPlayingNotes());
        }

        return stack;
    }
}
