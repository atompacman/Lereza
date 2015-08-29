package com.atompacman.lereza.core.piece;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.lereza.core.midi.sequence.MIDISequence;
import com.atompacman.lereza.core.solfege.Pitch;
import com.atompacman.lereza.core.solfege.RythmicSignature;

public class PieceBuilder extends PieceComponentBuilder<Piece<Stack<Note>>> {

    //======================================= FIELDS =============================================\\

    private final List<PartBuilder> builders;
    private final RythmicSignature  rythmicSign;
    private final MIDISequence      midiSeq;

    private int  currPart;
    private int  currBegTU;
    private int  currLenTU;
    private byte currVelocity;



    //======================================= METHODS ============================================\\

    //---------------------------------- PUBLIC CONSTRUCTOR --------------------------------------\\

    public PieceBuilder() {
        this(null, RythmicSignature.STANDARD_4_4);
    }

    public PieceBuilder(RythmicSignature rythmicSignature) {
        this(null, rythmicSignature);
    }

    public PieceBuilder(MIDISequence midiSeq) {
        this(midiSeq, midiSeq.getRythmicSignature());
    }

    private PieceBuilder(MIDISequence midiSeq, RythmicSignature rythmicSign) {
        this.builders     = new ArrayList<>();
        this.rythmicSign  = rythmicSign;
        this.midiSeq      = midiSeq;

        this.currPart     = 0;
        this.currBegTU    = 0;
        this.currLenTU    = 32;
        this.currVelocity = 100;
    }


    //----------------------------------------- ADD ----------------------------------------------\\

    public PieceBuilder add(Pitch pitch, byte velocity, int part, int begTU, int lengthTU) {
        while (part >= builders.size()) {
            PartBuilder partBuilder = new PartBuilder(rythmicSign);
            registerSubmodule(partBuilder);
            builders.add(partBuilder);
        }
        builders.get(part).add(pitch, velocity, begTU, lengthTU);
        return this;
    }

    public PieceBuilder add(byte hexNote, byte velocity, int part, int begTU, int lengthTU) {
        return add(Pitch.thatIsMoreCommonForHexValue(hexNote), velocity, part, begTU, lengthTU);
    }

    public PieceBuilder add(Pitch pitch, int begTU, int lengthTU) {
        return add(pitch, currVelocity, currPart, begTU, lengthTU);
    }

    public PieceBuilder add(Pitch pitch, int begTU) {
        return add(pitch, currVelocity, currPart, begTU, currLenTU);
    }

    public PieceBuilder add(Pitch pitch) {
        return add(pitch, currVelocity, currPart, currBegTU, currLenTU);
    }

    public PieceBuilder part(int partIndex) {
        this.currPart = partIndex;
        return this;
    }

    public PieceBuilder pos(int timeunit) {
        this.currBegTU = timeunit;
        return this;
    }

    public PieceBuilder length(int noteLenTU) {
        this.currLenTU = noteLenTU;
        return this;
    }

    public PieceBuilder velocity(byte velocity) {
        this.currVelocity = velocity;
        return this;
    }


    //---------------------------------------- BUILD ---------------------------------------------\\

    public Piece<Stack<Note>> buildComponent() {
        List<Part<Stack<Note>>> parts = new ArrayList<>();
        for (PartBuilder builder : builders) {
            parts.add(builder.build());
        }
        return new Piece<Stack<Note>>(parts, rythmicSign, midiSeq);
    }


    //---------------------------------------- RESET ---------------------------------------------\\

    protected void reset() {
        builders.clear();
    }
}
