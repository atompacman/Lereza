package com.atompacman.lereza.builtin.analysis.structure;

import com.atompacman.lereza.core.analysis.structure.MusicalStructure;

public final class City extends MusicalStructure {

    //
    //  ~  FIELDS  ~  //
    //

    private final String name;
    
    
    //
    //  ~  INIT  ~  //
    //

    public City(String name) {
        this.name = name;
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //

    public String getName() {
        return name;
    }
}
