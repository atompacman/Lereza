package com.atompacman.lereza.core.piece;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.lereza.core.solfege.Pitch;
import com.atompacman.lereza.core.solfege.RythmicSignature;

public final class PartBuilder extends PieceComponentBuilder<Part<Stack<Note>>> {

    //======================================= FIELDS =============================================\\

    private final List<BarBuilder> builders;
    private final RythmicSignature rythmicSign;

    private int  currBegTU;
    private int  currLenTU;
    private byte currVelocity;



    //======================================= METHODS ============================================\\

    //---------------------------------- PUBLIC CONSTRUCTOR --------------------------------------\\

    public PartBuilder() {
        this(RythmicSignature.STANDARD_4_4);
    }

    public PartBuilder(RythmicSignature rythmicSign) {
        this.builders = new ArrayList<>();
        this.rythmicSign = rythmicSign;

        this.currBegTU = 0;
        this.currLenTU = 32;
        this.currVelocity = 100;
    }


    //----------------------------------------- ADD ----------------------------------------------\\

    public PartBuilder add(Pitch pitch, byte velocity, int begTU, int lengthTU) {
        int tuInBar = rythmicSign.timeunitsInABar();
        int barPosTU = begTU % tuInBar;
        int actualLen = lengthTU;

        if (barPosTU + lengthTU > tuInBar) {
            actualLen = tuInBar - barPosTU;
        }
        builderAt(begTU).add(pitch, velocity, barPosTU, actualLen, false);

        lengthTU -= actualLen;

        while (lengthTU != 0) {
            begTU += actualLen;
            if (lengthTU > tuInBar) {
                actualLen = tuInBar;
            } else {
                actualLen = lengthTU;
            }
            builderAt(begTU).add(pitch, velocity, 0, actualLen, true);
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

    private BarBuilder builderAt(int timestamp) {
        int barNum = (int)((double) timestamp / (double) rythmicSign.timeunitsInABar());
        while (barNum >= builders.size()) {
            BarBuilder barBuilder = new BarBuilder(rythmicSign);
            registerSubmodule(barBuilder);
            builders.add(barBuilder);
        }
        return builders.get(barNum);
    }


    //---------------------------------------- BUILD ---------------------------------------------\\

    public Part<Stack<Note>> buildComponent() {
        List<Bar<Stack<Note>>> bars = new ArrayList<>();
        for (BarBuilder builder : builders) {
            bars.add(builder.build());
        }
        return new Part<Stack<Note>>(bars, rythmicSign);
    }


    //---------------------------------------- RESET ---------------------------------------------\\

    public void reset() {
        builders.clear();
    }
}
