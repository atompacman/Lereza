package com.atompacman.lereza.core.piece;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.lereza.core.piece.timeline.TimeunitToBarConverter;
import com.atompacman.lereza.core.solfege.Pitch;
import com.atompacman.lereza.core.solfege.TimeSignature;
import com.atompacman.toolkat.misc.Log;
import com.atompacman.toolkat.misc.StringHelper;
import com.atompacman.toolkat.module.BaseModule;

public class PieceBuilder extends PieceComponentBuilder<Piece> {

    //======================================= FIELDS =============================================\\

    // Sub-builders
    private final List<PartBuilder> builders;
    
    // Lifetime
    private final TimeunitToBarConverter tuToBar;
    
    // Builder parameters
    private int  currPart;
    private int  currBegTU;
    private int  currLenTU;
    private byte currVelocity;



    //======================================= METHODS ============================================\\

    //---------------------------------- PUBLIC CONSTRUCTOR --------------------------------------\\

    public PieceBuilder(int pieceLengthTU) {
        this(new TimeunitToBarConverter(pieceLengthTU), null);
    }

    public PieceBuilder(TimeSignature firstTimeSign, int pieceLengthTU) {
        this(new TimeunitToBarConverter(firstTimeSign, pieceLengthTU), null);
    }
    
    public PieceBuilder(TimeunitToBarConverter tuToBar, BaseModule parentModule) {
        super(parentModule);
        
        // Sub-builders
        this.builders = new ArrayList<>();
        
        // Lifetime
        this.tuToBar = tuToBar;

        // Builder parameters
        this.currPart     = 0;
        this.currBegTU    = 0;
        this.currLenTU    = 32;
        this.currVelocity = 100;
    }


    //----------------------------------------- ADD ----------------------------------------------\\

    public PieceBuilder add(Pitch pitch, byte velocity, int part, int begTU, int lengthTU) {
        while (part >= builders.size()) {
            builders.add(new PartBuilder(tuToBar, this));
        }
        
        log("Pitch: %3s | Velocity: %3d | Part: %2d | %8s | Beg: %4d | End: %4d |", 
                pitch, (int) velocity, part, "", begTU, begTU + lengthTU);
        
        builders.get(part).add(pitch, velocity, begTU, lengthTU);
        
        Log.trace(StringHelper.title(""));
        
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

    public Piece buildComponent() {
        List<Part> parts = new ArrayList<>();
        for (PartBuilder builder : builders) {
            parts.add(builder.build());
        }
        return new Piece(parts);
    }


    //---------------------------------------- RESET ---------------------------------------------\\

    protected void reset() {
        builders.clear();
    }
}
