package com.atompacman.lereza.core.piece.timeline;

import java.util.TreeMap;

public class TempoTimeline extends PiecePropertyTimeline<Double> {
    
    //======================================= METHODS ============================================\\

    //------------------------------------- CONSTRUCTORS -----------------------------------------\\

    public TempoTimeline(int pieceLengthTU) {
        super(pieceLengthTU);
    }
    
    public TempoTimeline(TreeMap<Integer, Double> tempoChangesTU, int pieceLengthTU) {
        super(tempoChangesTU, pieceLengthTU);
    }
}
