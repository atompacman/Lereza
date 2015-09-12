package com.atompacman.lereza.core.piece;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.lereza.core.solfege.Pitch;
import com.atompacman.lereza.core.solfege.TimeSignature;

public final class PartBuilder extends PieceComponentBuilder<Part<Stack<TiedNote>>> {

    //======================================= FIELDS =============================================\\

    private final List<BarBuilder> builders;
    private final TimeSignature    timeSign;

    private int  currBegTU;
    private int  currLenTU;
    private byte currVelocity;



    //======================================= METHODS ============================================\\

    //---------------------------------- PUBLIC CONSTRUCTOR --------------------------------------\\

    public PartBuilder() {
        this(TimeSignature.STANDARD_4_4);
    }

    public PartBuilder(TimeSignature timeSign) {
        this.builders     = new ArrayList<>();
        this.timeSign     = timeSign;

        this.currBegTU    = 0;
        this.currLenTU    = 32;
        this.currVelocity = 100;
    }


    //----------------------------------------- ADD ----------------------------------------------\\

    public PartBuilder add(Pitch pitch, byte velocity, int begTU, int lengthTU) {
        int tuInBar = timeSign.timeunitsInABar();
        int barPosTU = begTU % tuInBar;
        int actualLen = lengthTU;

        // Check if note spans more than one bar
        if (barPosTU + lengthTU > tuInBar) {
            actualLen = tuInBar - barPosTU;
        }
        
        // Add first untied note
        TiedNote untiedNote = builderAt(begTU).add(pitch, velocity, barPosTU, actualLen, null);
        lengthTU -= actualLen;

        // Add tied notes
        while (lengthTU != 0) {
            begTU += actualLen;
            if (lengthTU > tuInBar) {
                actualLen = tuInBar;
            } else {
                actualLen = lengthTU;
            }
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
        int barNum = (int)((double) timeunit / (double) timeSign.timeunitsInABar());
        while (barNum >= builders.size()) {
            BarBuilder barBuilder = new BarBuilder(timeSign);
            registerSubmodule(barBuilder);
            builders.add(barBuilder);
        }
        return builders.get(barNum);
    }


    //---------------------------------------- BUILD ---------------------------------------------\\

    public Part<Stack<TiedNote>> buildComponent() {
        List<Bar<Stack<TiedNote>>> bars = new ArrayList<>();
        for (BarBuilder builder : builders) {
            bars.add(builder.build());
        }
        return new Part<Stack<TiedNote>>(bars, timeSign);
    }


    //---------------------------------------- RESET ---------------------------------------------\\

    public void reset() {
        builders.clear();
    }
}
