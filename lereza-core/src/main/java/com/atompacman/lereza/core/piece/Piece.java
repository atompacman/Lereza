package com.atompacman.lereza.core.piece;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.atompacman.lereza.core.analysis.MusicalStructure;

public final class Piece implements MusicalStructure, Iterable<Part> {

    private List<Part> parts;
    
    public Piece() {
        this.parts = new LinkedList<>();
    }
    
    public Part getPart(int i) {
        return parts.get(i);
    }

    public int numParts() {
        return parts.size();
    }
    
    @Override
    public Iterator<Part> iterator() {
        return parts.iterator();
    }
}
