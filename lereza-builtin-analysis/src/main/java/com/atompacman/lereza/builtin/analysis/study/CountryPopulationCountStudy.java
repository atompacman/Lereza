package com.atompacman.lereza.builtin.analysis.study;

import com.atompacman.lereza.core.analysis.study.Study;

public final class CountryPopulationCountStudy extends Study {

    //
    //  ~  FIELDS  ~  //
    //

    private final int population;
    
    
    //
    //  ~  INIT  ~  //
    //

    public CountryPopulationCountStudy(int population) {
        this.population = population;
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //
    
    public int getPopulation() {
        return population;
    }
}
