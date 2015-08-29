package com.atompacman.lereza.core.piece;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class TestBar {

    //======================================= FIELDS =============================================\\

    private BarBuilder builder;



    //======================================= METHODS ============================================\\

    //===================================== BEFORE TESTS =========================================\\

    @Before
    public void beforeTest() {
        builder = new BarBuilder();
    }


    //====================================== UNIT TESTS ==========================================\\

    @Test
    public void addSimpleUntiedNotes() {
        builder.pos(0).add("C2").pos(16).add("E3").add("B3").pos(24).add("G2");
        
        buildAndPerformBasicAssertions(4);
    }


    //--------------------------------------- HELPERS --------------------------------------------\\

    private Bar<Stack<Note>> buildAndPerformBasicAssertions(int numStartingNotes) {
        // Build bar
        Bar<Stack<Note>> stack = builder.build();

        // State
        assertEquals(numStartingNotes == 0, stack.isEmpty());
        assertEquals(numStartingNotes,      stack.getNumStartingNotes());
        assertEquals(64,                    stack.numTimeunits());

        return stack;
    }
}
