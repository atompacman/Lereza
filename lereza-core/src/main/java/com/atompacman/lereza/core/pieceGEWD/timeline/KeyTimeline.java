package com.atompacman.lereza.core.pieceGEWD.timeline;

import com.atompacman.lereza.core.theory.Key;

public final class KeyTimeline extends PiecePropertyTimeline<Key> {

    //
    //  ~  INIT  ~  //
    //

    public static KeyTimeline of(int pieceLengthTU) {
        return new KeyTimeline(pieceLengthTU);
    }

    private KeyTimeline(int pieceLengthTU) {
        super(pieceLengthTU);
    }
}
