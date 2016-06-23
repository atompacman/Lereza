package com.atompacman.lereza.core.pieceGEWD;

import java.util.List;

public final class MonophonicPart extends HomophonicPart {

    //
    //  ~  INIT  ~  //
    //

    @SuppressWarnings({ "unchecked", "rawtypes" })
    MonophonicPart(List<MonophonicBar> bars) {
        super((List) bars);
    }
    

    //
    //  ~  GETTERS  ~  //
    //

    @Override
    public MonophonicBar getBar(int bar) {
        return (MonophonicBar) super.getBar(bar);
    }
}
