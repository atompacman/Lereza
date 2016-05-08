package com.atompacman.lereza.core.piece.timeline;

import com.atompacman.lereza.core.theory.Key;

public final class MIDIKeyTimeline extends PiecePropertyTimeline<Key> {

    //
    //  ~  INIT  ~  //
    //

    public static MIDIKeyTimeline of(int pieceLengthTU) {
        return new MIDIKeyTimeline(pieceLengthTU);
    }

    private MIDIKeyTimeline(int pieceLengthTU) {
        super(pieceLengthTU);
    }
}
