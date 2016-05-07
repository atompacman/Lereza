package com.atompacman.lereza.core.piece2;

import java.util.List;

public class HomophonicPart extends PolyphonicPart {

    //
    //  ~  INIT  ~  //
    //

    @SuppressWarnings({ "unchecked", "rawtypes" })
    HomophonicPart(List<HomophonicBar> bars) {
        super((List<PolyphonicBar>) (List) bars);
    }
    

    //
    //  ~  GETTERS  ~  //
    //

    public HomophonicBar getBar(int bar) {
        return (HomophonicBar) super.getBar(bar);
    }
}
