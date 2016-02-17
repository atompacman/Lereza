package com.atompacman.lereza.core.piece;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.lereza.core.piece.timeline.TimeunitToBarConverter;
import com.atompacman.lereza.core.theory.Pitch;
import com.atompacman.lereza.core.theory.TimeSignature;
import com.atompacman.toolkat.module.BaseModule;

public final class PartBuilder extends PieceComponentBuilder<Part> {

    //======================================= FIELDS =============================================\\

    // Sub-builders
    private final List<BarBuilder> builders;

    // Lifetime
    private final TimeunitToBarConverter tuToBar;
    
    // Builder parameters
    private int  currBegTU;
    private int  currLenTU;
    private byte currVelocity;



    //======================================= METHODS ============================================\\

    //------------------------------------- CONSTRUCTORS -----------------------------------------\\

    public PartBuilder(int pieceLengthTU) {
        this(new TimeunitToBarConverter(pieceLengthTU), null);
    }

    public PartBuilder(TimeSignature firstTimeSign, int pieceLengthTU) {
        this(new TimeunitToBarConverter(firstTimeSign, pieceLengthTU), null);
    }
    
    public PartBuilder(TimeunitToBarConverter tuToBar, BaseModule parentModule) {
        super(parentModule);
        
        // Sub-builders
        this.builders = new ArrayList<>();
        
        // Lifetime
        this.tuToBar = tuToBar;
        
        // Builder parameters
        this.currBegTU    = 0;
        this.currLenTU    = 32;
        this.currVelocity = 100;
    }


    //----------------------------------------- ADD ----------------------------------------------\\

    public PartBuilder add(Pitch pitch, byte velocity, int begTU, int lengthTU) {
        int tuInBar = tuToBar.getBarLengthTU(begTU);
        int barPosTU = begTU - tuToBar.convertBarToTu(tuToBar.convertTuToBar(begTU));
        int actualLen = lengthTU;

        // Check if note spans more than one bar
        if (barPosTU + lengthTU > tuInBar) {
            actualLen = tuInBar - barPosTU;
        }
        
        // Add first untied note
        log(begTU, actualLen);
        Note untiedNote = builderAt(begTU).add(pitch, velocity, barPosTU, actualLen, null);
        lengthTU -= actualLen;

        // Add tied notes
        while (lengthTU != 0) {
            begTU += actualLen;
            tuInBar = tuToBar.getBarLengthTU(begTU);
            if (lengthTU > tuInBar) {
                actualLen = tuInBar;
            } else {
                actualLen = lengthTU;
            }
            log(begTU, actualLen);
            untiedNote = builderAt(begTU).add(pitch, velocity, 0, actualLen, untiedNote);
            lengthTU -= actualLen;
        }
        
        return this;
    }

    public PartBuilder add(Pitch pitch, int begTU, int lengthTU) {
        return add(pitch, currVelocity, begTU, lengthTU);
    }

    public PartBuilder add(Pitch pitch, int begTU) {
        return add(pitch, currVelocity, begTU, currLenTU);
    }

    public PartBuilder add(Pitch pitch) {
        return add(pitch, currVelocity, currBegTU, currLenTU);
    }

    public PartBuilder pos(int timeunit) {
        this.currBegTU = timeunit;
        return this;
    }

    public PartBuilder length(int noteLenTU) {
        this.currLenTU = noteLenTU;
        return this;
    }

    public PartBuilder velocity(byte velocity) {
        this.currVelocity = velocity;
        return this;
    }

    private BarBuilder builderAt(int timeunit) {
        int bar = tuToBar.convertTuToBar(timeunit);
        for (int i = builders.size(); i <= bar; ++i) {
            builders.add(new BarBuilder(tuToBar.getBarLengthTUFromBar(i), this));
        }
        return builders.get(bar);
    }

    private void log(int begTU, int lengthTU) {
        log(1, "%37s | Bar: %3d | Beg: %4d | End: %4d |", "", 
                tuToBar.convertTuToBar(begTU), begTU, begTU + lengthTU);
    }
    

    //---------------------------------------- BUILD ---------------------------------------------\\

    public Part buildComponent() {
        List<Bar> bars = new ArrayList<>();
        for (BarBuilder builder : builders) {
            bars.add(builder.build());
        }
        return new Part(bars);
    }


    //---------------------------------------- RESET ---------------------------------------------\\

    public void reset() {
        builders.clear();
    }
}
