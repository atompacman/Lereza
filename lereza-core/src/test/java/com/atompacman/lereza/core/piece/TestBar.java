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
        builder.add("C4").pos(16).add("E5").add("G5").pos(24).add("B4");
        buildAndPerformBasicAssertions(4,1);
    }


    //--------------------------------------- HELPERS --------------------------------------------\\

    private Bar buildAndPerformBasicAssertions(int numStartingUntiedNotes, 
                                                                int numStartingTiedNotes) {
        
        // Build bar
        Bar bar = builder.build();

        // State
        assertEquals(numStartingUntiedNotes == 0, bar.isEmpty());
        assertEquals(numStartingUntiedNotes, bar.getNumStartingUntiedNotes());
        assertEquals(numStartingUntiedNotes + numStartingTiedNotes, bar.getNumStartingNotes());
        assertEquals(64, bar.numTimeunits());

        return bar;
    }
}
