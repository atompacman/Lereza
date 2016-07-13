package com.atompacman.lereza.builtin.analysis.study;

import com.atompacman.lereza.core.analysis.study.Study;

public final class GDPStudy extends Study {

    //
    //  ~  FIELDS  ~  //
    //

    private final double gdp;
    
    
    //
    //  ~  INIT  ~  //
    //

    public GDPStudy(double gdp) {
        this.gdp = gdp;
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //

    public double getGDP() {
        return gdp;
    }
}
