package com.atompacman.lereza.core.pieceGEWD.timeline;

import java.util.TreeMap;

import com.atompacman.lereza.core.theory.TimeSignature;

public final class InfiniteTimeunitToBarConverter extends TimeunitToBarConverter {

    //
    //  ~  INIT  ~  //
    //
    
    public static InfiniteTimeunitToBarConverter of() {
        return new InfiniteTimeunitToBarConverter(TimeSignature.STANDARD_4_4);
    }
    
    public static InfiniteTimeunitToBarConverter of(TimeSignature timeSign) {
        return new InfiniteTimeunitToBarConverter(timeSign);
    }
    
    @SuppressWarnings("serial")
    private InfiniteTimeunitToBarConverter(TimeSignature timeSign) {
        super(new TreeMap<Integer, TimeSignature>(){{ put(0, timeSign); }}, Integer.MAX_VALUE);
    }
}
