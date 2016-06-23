package com.atompacman.lereza.core.pieceGEWD.timeline;

import com.atompacman.lereza.core.theory.TimeSignature;

public final class TimeSignatureTimeline extends BarBasedPiecePropertyTimeline<TimeSignature>{

    //
    //  ~  INIT  ~  //
    //

    public static TimeSignatureTimeline of(TimeunitToBarConverter tuToBar) {
        return new TimeSignatureTimeline(tuToBar);
    }

    private TimeSignatureTimeline(TimeunitToBarConverter tuToBar) {
        super(tuToBar);
    }
}
