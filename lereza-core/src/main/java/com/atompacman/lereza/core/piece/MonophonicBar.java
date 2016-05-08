package com.atompacman.lereza.core.piece;

import java.util.List;

public final class MonophonicBar extends HomophonicBar {

    //
    //  ~  INIT  ~  //
    //

    @SuppressWarnings({ "unchecked", "rawtypes" })
    MonophonicBar(List<MonophonicBarSlice> slices) {
        super((List<HomophonicBarSlice>) (List) slices);
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //

    @Override
    public MonophonicBarSlice getSlice(int timeunit) {
        return (MonophonicBarSlice) super.getSlice(timeunit);
    }
}
