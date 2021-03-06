package com.atompacman.lereza.core.piece.timeline;

public final class TempoTimeline extends PiecePropertyTimeline<Double> {

    //
    //  ~  INIT  ~  //
    //

    public static TempoTimeline of(int pieceLengthTU) {
        return new TempoTimeline(pieceLengthTU);
    }

    private TempoTimeline(int pieceLengthTU) {
        super(pieceLengthTU);
    }
}
