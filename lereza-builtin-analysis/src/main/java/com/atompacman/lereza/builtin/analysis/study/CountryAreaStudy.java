package com.atompacman.lereza.builtin.analysis.study;

import com.atompacman.lereza.core.analysis.study.Study;

public final class CountryAreaStudy extends Study {

    //
    //  ~  FIELDS  ~  //
    //

    private final double area;
    
    
    //
    //  ~  INIT  ~  //
    //

    public CountryAreaStudy(double area) {
        this.area = area;
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //

    public double getArea() {
        return area;
    }
}
