package com.atompacman.lereza.core.piece.timeline;

import java.util.TreeMap;

import com.atompacman.lereza.core.theory.Key;

public class MIDIKeyTimeline extends PiecePropertyTimeline<Key> {

    //======================================= METHODS ============================================\\

    //------------------------------------- CONSTRUCTORS -----------------------------------------\\

    public MIDIKeyTimeline(int pieceLengthTU) {
        super(pieceLengthTU);
    }
    
    public MIDIKeyTimeline(TreeMap<Integer, Key> keyChangesTU, int pieceLengthTU) {
        super(keyChangesTU, pieceLengthTU);
    }
}
