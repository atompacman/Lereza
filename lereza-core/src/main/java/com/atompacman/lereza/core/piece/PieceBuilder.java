package com.atompacman.lereza.core.piece;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.atompacman.lereza.core.piece.timeline.InfiniteTimeunitToBarConverter;
import com.atompacman.lereza.core.piece.timeline.TimeunitToBarConverter;
import com.atompacman.lereza.core.theory.Dynamic;
import com.atompacman.lereza.core.theory.Pitch;
import com.atompacman.lereza.core.theory.RhythmValue;
import com.atompacman.lereza.core.theory.TimeSignature;
import com.atompacman.toolkat.Builder;
import com.atompacman.toolkat.annotations.Implement;
import com.atompacman.toolkat.task.TaskMonitor;

public final class PieceBuilder extends Builder<Piece> {

    //
    //  ~  FIELDS  ~  //
    //

    // Lifetime
    private final TimeunitToBarConverter tuToBar;

    // Data for build
    private final List<PartBuilder> builders;

    // Temporary builder parameters
    private int  currPart;
    private int  currBegTU;
    private int  currLenTU;
    private byte currVelocity;


    //
    //  ~  INIT  ~  //
    //

    public static PieceBuilder of() {
        return new PieceBuilder(InfiniteTimeunitToBarConverter.of());
    }
    
    public static PieceBuilder of(TimeSignature timeSign) {
        return new PieceBuilder(InfiniteTimeunitToBarConverter.of(timeSign));
    }
    
    public static PieceBuilder of(int pieceLengthTU) {
        return new PieceBuilder(TimeunitToBarConverter.of(pieceLengthTU));
    }
    
    public static PieceBuilder of(TimeunitToBarConverter tuToBar) {
        return new PieceBuilder(tuToBar);
    }
    
    private PieceBuilder(TimeunitToBarConverter tuToBar) {
        // Lifetime
        this.tuToBar = tuToBar;

        // Data for build
        this.builders = new ArrayList<>();

        // Temporary builder parameters
        reset();
    }


    //
    //  ~  ADD  ~  //
    //

    public PieceBuilder add(Pitch pitch, byte velocity, int part, int begTU, int lengthTU) {
        // Create missing part builders
        while (part >= builders.size()) {
            builders.add(PartBuilder.of(tuToBar));
        }

        // Redirect note to a part builder
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


    //
    //  ~  BUILD  ~  //
    //

    @Implement
    protected Piece buildImpl(TaskMonitor monitor) {
        List<PolyphonicPart> parts = new LinkedList<>();
        
        // Build each part separately 
        // (this means they can be of different ranks in the complexity hierarchy)
        for (PartBuilder builder : builders) {
            parts.add(builder.build(monitor));
        }
        
        return new Piece(parts);
    }

    @Implement
    public void reset() {
        builders.clear();
        
        currPart     = 0;
        currBegTU    = 0;
        currLenTU    = RhythmValue.QUARTER.toTimeunit();
        currVelocity = Dynamic.Marker.FORTE.getMinimumVelocity();
    }
}
