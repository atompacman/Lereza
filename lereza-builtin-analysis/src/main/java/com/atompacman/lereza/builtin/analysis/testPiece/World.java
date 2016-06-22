package com.atompacman.lereza.builtin.analysis.testPiece;

import java.util.HashSet;
import java.util.Set;

import com.atompacman.lereza.core.analysis.MusicalStructure;

public final class World extends MusicalStructure {

    private static final String[] CONTINENT_NAMES = {
        "Asia", "Africa", "Europe", "Oceania", "North America", "South America"
    };
    
    public Set<Continent> getContinents() {
        Set<Continent> continants = new HashSet<>();
        for (String name : CONTINENT_NAMES) {
            continants.add(new Continent(name));
        }
        return continants;
    }
}
