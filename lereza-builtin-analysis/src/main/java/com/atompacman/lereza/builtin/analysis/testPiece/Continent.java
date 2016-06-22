package com.atompacman.lereza.builtin.analysis.testPiece;

import com.atompacman.lereza.core.analysis.MusicalStructure;

public final class Continent extends MusicalStructure {

    private final String name;
    
    Continent(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
