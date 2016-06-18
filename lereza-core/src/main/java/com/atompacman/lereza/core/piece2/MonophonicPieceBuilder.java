package com.atompacman.lereza.core.piece2;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import com.atompacman.lereza.core.piece.timeline.TimeunitToBarConverter;
import com.atompacman.lereza.core.theory.TimeSignature;
import com.atompacman.toolkat.annotations.Implement;
import com.atompacman.toolkat.task.TaskLogger;

public final class MonophonicPieceBuilder extends PieceBuilder<MonophonicPiece, MonophonicPart> {

    //
    //  ~  INIT  ~  //
    //

    public MonophonicPieceBuilder(int pieceLengthTU) {
        this(new TimeunitToBarConverter(pieceLengthTU), null);
    }

    public MonophonicPieceBuilder(TimeSignature firstTimeSign, int pieceLengthTU) {
        this(new TimeunitToBarConverter(firstTimeSign, pieceLengthTU), null);
    }

    public MonophonicPieceBuilder(TimeunitToBarConverter tuToBar, @Nullable TaskLogger taskLogger) {
        super(tuToBar, Optional.ofNullable(taskLogger));
    }


    //
    //  ~  ADD  ~  //
    //

    @Implement
    protected PartBuilder<MonophonicPart, ?> instantiateSubBuilder(TimeunitToBarConverter tuToBar,
            TaskLogger 		   taskLogger) {


    }


    //
    //  ~  BUILD  ~  //
    //

    @Implement
    protected MonophonicPiece instantiateStructure(List<MonophonicPart> parts) {
        return new MonophonicPiece(parts);
    }
}
