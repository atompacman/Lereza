package com.atompacman.lereza.core.pieceGEWD.timeline;

import static com.google.common.base.Preconditions.checkArgument;

abstract class BarBasedPiecePropertyTimeline<T> extends PiecePropertyTimeline<T> {

    //
    //  ~  FIELDS  ~  //
    //

    private final TimeunitToBarConverter tuToBar;
    
    
    //
    //  ~  INIT  ~  //
    //

    protected BarBasedPiecePropertyTimeline(TimeunitToBarConverter tuToBar) {
        super(tuToBar.pieceLengthTU);
        this.tuToBar = tuToBar;
    }
    
    
    //
    //  ~  ADD  ~  //
    //

    @Override
    public void addPropertyChangeAtTU(int tu, T value) {
        checkArgument(tuToBar.isABarBeginning(tu), 
                "Property change must be at the begining of a bar");
        super.addPropertyChangeAtTU(tu, value);
    }
    
    public void addPropertyChangeAtBar(int bar, T value) {
        super.addPropertyChangeAtTU(tuToBar.convertBarToTu(bar), value);
    }
    

    //
    //  ~  GETTERS  ~  //
    //

    public T getValueAtBar(int bar) {
        return getValueAtTimeunit(tuToBar.convertBarToTu(bar));
    }
}
