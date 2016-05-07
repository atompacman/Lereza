package com.atompacman.lereza.core.piece2;

import java.util.List;

public class HomophonicBar extends PolyphonicBar {

    //
    //  ~  INIT  ~  //
    //

    @SuppressWarnings({ "unchecked", "rawtypes" })
    HomophonicBar(List<HomophonicBarSlice> slices) {
        super((List<PolyphonicBarSlice>) (List) slices);
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //

    @Override
    public HomophonicBarSlice getSlice(int timeunit) {
        return (HomophonicBarSlice) super.getSlice(timeunit);
    }
}
