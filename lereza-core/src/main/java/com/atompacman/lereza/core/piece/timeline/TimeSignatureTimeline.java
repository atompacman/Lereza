package com.atompacman.lereza.core.piece.timeline;

import java.util.TreeMap;

import com.atompacman.lereza.core.solfege.TimeSignature;

public final class TimeSignatureTimeline extends BarBasedPiecePropertyTimeline<TimeSignature>{

    //======================================= METHODS ============================================\\

    //------------------------------------- CONSTRUCTORS -----------------------------------------\\

    public TimeSignatureTimeline(TimeunitToBarConverter tuToBar) {
        super(tuToBar);
    }
    
    public TimeSignatureTimeline(TreeMap<Integer, TimeSignature> timeSignChangesTU, 
                                 TimeunitToBarConverter          tuToBar) {
        super(timeSignChangesTU, tuToBar);
    }
}
