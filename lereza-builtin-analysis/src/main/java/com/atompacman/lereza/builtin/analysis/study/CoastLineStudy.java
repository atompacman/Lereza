package com.atompacman.lereza.builtin.analysis.study;

import com.atompacman.lereza.core.analysis.study.Study;

public final class CoastLineStudy extends Study {

    //
    //  ~  FIELDS  ~  //
    //

    private final double areaSqrKm;
    
    
    //
    //  ~  INIT  ~  //
    //

    public CoastLineStudy(double areaSqrKm) {
        this.areaSqrKm = areaSqrKm;
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //
    
    public double getArea() {
        return areaSqrKm;
    }
}
