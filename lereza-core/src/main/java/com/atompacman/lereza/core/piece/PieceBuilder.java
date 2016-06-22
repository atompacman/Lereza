package com.atompacman.lereza.core.piece;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.atompacman.lereza.core.piece.timeline.TimeunitToBarConverter;
import com.atompacman.lereza.core.theory.Pitch;
import com.atompacman.toolkat.annotations.Implement;
import com.atompacman.toolkat.task.TaskLogger;

public final class PieceBuilder extends Builder<Piece> {

    //
    //  ~  FIELDS  ~  //
    //

    // Sub-builders
    private final List<PartBuilder<K,?>> builders;

    // Lifetime
    private final TimeunitToBarConverter tuToBar;

    // Temporary builder parameters
    private int  currPart;
    private int  currBegTU;
    private int  currLenTU;
    private byte currVelocity;


    //
    //  ~  INIT  ~  //
    //

    protected PieceBuilder(TimeunitToBarConverter tuToBar, Optional<TaskLogger> taskLogger) {
        super(taskLogger);

        // Sub-builders
        this.builders = new ArrayList<>();

        // Lifetime
        this.tuToBar = tuToBar;

        // Temporary builder parameters
        reset();
    }


    //
    //  ~  ADD  ~  //
    //

    public PieceBuilder<T,K> add(Pitch pitch, byte velocity, int part, int begTU, int lengthTU) {
        while (part >= builders.size()) {
            builders.add(instantiateSubBuilder(tuToBar, taskLogger));
        }

        taskLogger.log("Pitch: %3s | Velocity: %3d | Part: %2d | %8s | Beg: %4d | End: %4d |", 
                pitch, (int) velocity, part, "", begTU, begTU + lengthTU);

        builders.get(part).add(pitch, velocity, begTU, lengthTU);

        return this;
    }

    public PieceBuilder<T,K> add(byte hexNote, byte velocity, int part, int begTU, int lengthTU) {
        return add(Pitch.thatIsMoreCommonForHexValue(hexNote), velocity, part, begTU, lengthTU);
    }

    public PieceBuilder<T,K> add(Pitch pitch, int begTU, int lengthTU) {
        return add(pitch, currVelocity, currPart, begTU, lengthTU);
    }

    public PieceBuilder<T,K> add(Pitch pitch, int begTU) {
        return add(pitch, currVelocity, currPart, begTU, currLenTU);
    }

    public PieceBuilder<T,K> add(Pitch pitch) {
        return add(pitch, currVelocity, currPart, currBegTU, currLenTU);
    }

    public PieceBuilder<T,K> part(int partIndex) {
        this.currPart = partIndex;
        return this;
    }

    public PieceBuilder<T,K> pos(int timeunit) {
        this.currBegTU = timeunit;
        return this;
    }

    public PieceBuilder<T,K> length(int noteLenTU) {
        this.currLenTU = noteLenTU;
        return this;
    }

    public PieceBuilder<T,K> velocity(byte velocity) {
        this.currVelocity = velocity;
        return this;
    }

    protected abstract PartBuilder<K, ?> instantiateSubBuilder(TimeunitToBarConverter tuToBar, 
            TaskLogger 			  taskLogger);

    //
    //  ~  BUILD  ~  //
    //

    @Implement
    public T buildStructure() {
        List<K> parts = new LinkedList<>();
        for (PartBuilder<K, ?> builder : builders) {
            parts.add(builder.build());
        }
        return instantiateStructure(parts);
    }

    protected abstract T instantiateStructure(List<K> parts);

    @Implement
    protected void reset() {
        currPart     = 0;
        currBegTU    = 0;
        currLenTU    = 32;
        currVelocity = 100;
        builders.clear();
    }
}
