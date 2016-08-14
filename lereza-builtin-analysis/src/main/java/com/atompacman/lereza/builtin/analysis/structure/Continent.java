package com.atompacman.lereza.builtin.analysis.structure;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.atompacman.lereza.core.analysis.structure.MusicalStructure;
import com.google.common.collect.ImmutableSet;

public final class Continent extends MusicalStructure {

    //
    //  ~  FIELDS  ~  //
    //

    private final String name;
    
    private final Map<String, ContinentSubregion> subregions;
    
    
    //
    //  ~  INIT  ~  //
    //

    public Continent(String name) {
        this.name = name;
        
        this.subregions = new HashMap<>();
    }
    
    
    //
    //  ~  SETTERS  ~  //
    //

    void addSubregion(ContinentSubregion subregion) {
        subregions.put(subregion.getName(), subregion);
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //

    public String getName() {
        return name;
    }
    
    public ImmutableSet<ContinentSubregion> getSubregions() {
        return ImmutableSet.copyOf(subregions.values());
    }

    public @Nullable ContinentSubregion getSubregion(String name) {
        return subregions.get(name);
    }
}