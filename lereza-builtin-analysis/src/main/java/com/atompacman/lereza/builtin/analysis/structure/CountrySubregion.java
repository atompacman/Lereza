package com.atompacman.lereza.builtin.analysis.structure;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.atompacman.lereza.core.analysis.structure.MusicalStructure;
import com.google.common.collect.ImmutableSet;

public final class CountrySubregion extends MusicalStructure {

    //
    //  ~  FIELDS  ~  //
    //

    private final String name;
    
    private final Map<String, City> cities;
    
    
    //
    //  ~  INIT  ~  //
    //

    public CountrySubregion(String name) {
        this.name = name;
        
        this.cities = new HashMap<>();
    }
    
    
    //
    //  ~  SETTERS  ~  //
    //

    void addCity(City city) {
        cities.put(city.getName(), city);
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //

    public String getName() {
        return name;
    }
    
    public ImmutableSet<City> getCities() {
        return ImmutableSet.copyOf(cities.values());
    }
    
    public @Nullable City getCity(String name) {
        return cities.get(name);
    }
}
