package com.atompacman.lereza.builtin.analysis.structure;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.atompacman.lereza.core.analysis.structure.MusicalStructure;
import com.google.common.collect.ImmutableSet;

public final class ContinentSubregion extends MusicalStructure {

    //
    //  ~  FIELDS  ~  //
    //

    private final String name;
    
    private final Map<String, Country> countries;
    
    
    //
    //  ~  INIT  ~  //
    //

    public ContinentSubregion(String name) {
        this.name = name;
        
        this.countries = new HashMap<>();
    }
    
    
    //
    //  ~  SETTERS  ~  //
    //

    void addCountry(Country country) {
        countries.put(country.getCommonName(), country);
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //

    public String getName() {
        return name;
    }
    
    public ImmutableSet<Country> getCountries() {
        return ImmutableSet.copyOf(countries.values());
    }
    
    public @Nullable Country getCountry(String name) {
        return countries.get(name);
    }
}
