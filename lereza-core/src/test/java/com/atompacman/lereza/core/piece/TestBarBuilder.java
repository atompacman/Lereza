package com.atompacman.lereza.core.piece;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.atompacman.lereza.core.theory.Pitch;
import com.atompacman.lereza.core.theory.RythmnValue;
import com.atompacman.lereza.core.theory.TimeSignature;

public final class TestBarBuilder {
    
    //
    //  ~  LIMIT CASES  ~  //
    //
    
    @Test
    public void Scenario_Build_NoEntries_EmptyBar() {
        BarBuilder builder = BarBuilder.of();
        
        PolyphonicBar output = builder.build();
        
        assertTrue(output instanceof MonophonicBar);
        MonophonicBar bar = (MonophonicBar) output;
        
        for (int i = 0; i < bar.timeunitLength(); ++i) {
            MonophonicBarSlice slice = bar.getSlice(i);
            
            // Slice must only contain a rest
            assertTrue(slice.isRest());
            
            // Only first slice should have the beginning whole rest node
            assertEquals(slice.getBeginningNodes().numNodes() == 0, i != 0);
        }
    }
    
    //
    //  ~  COMPLETE  ~  //
    //
    
    //@Test
    public void Complete_Monophonic_ValidArgs_ValidReturnValue() {
        TimeSignature timeSign = TimeSignature.of(3, 4);
        
        BarBuilder builder = BarBuilder.of(timeSign);
        
        builder.velocity((byte) 50)
               .pos(-RythmnValue.EIGHTH.toTimeunit())
               .length(RythmnValue.QUARTER.toTimeunit())
               .add(Pitch.of("Bb4"))
               
               .pos(-RythmnValue.EIGHTH.toTimeunit())
               .length(RythmnValue.QUARTER.toTimeunit())
               .add(Pitch.of("Bb4"))
               
               .pos(-RythmnValue.EIGHTH.toTimeunit())
               .length(RythmnValue.QUARTER.toTimeunit())
               .add(Pitch.of("Bb4"))
               
               .pos(-RythmnValue.EIGHTH.toTimeunit())
               .length(RythmnValue.QUARTER.toTimeunit())
               .add(Pitch.of("Bb4"));
    }
}
