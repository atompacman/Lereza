package com.atompacman.lereza.core.piece;

import java.util.List;

public final class MonophonicPart extends HomophonicPart {

    //
    //  ~  INIT  ~  //
    //

    @SuppressWarnings({ "unchecked", "rawtypes" })
    MonophonicPart(List<MonophonicBar> bars) {
        super((List<HomophonicBar>) (List) bars);
    }
    

    //
    //  ~  GETTERS  ~  //
    //

    public MonophonicBar getBar(int bar) {
        return (MonophonicBar) super.getBar(bar);
    }
}
