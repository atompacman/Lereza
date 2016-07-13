package com.atompacman.lereza.builtin.analysis.study;

import com.atompacman.lereza.core.analysis.study.Study;

public final class PopulationDensityStudy extends Study {

    //
    //  ~  FIELDS  ~  //
    //

    private final double popDensity;

    
    //
    //  ~  INIT  ~  //
    //
    
    public PopulationDensityStudy(double popDensity) {
        this.popDensity = popDensity;
    }

    
    //
    //  ~  GETTERS  ~  //
    //
    
    public double getPopulationDensity() {
        return popDensity;
    }
}
