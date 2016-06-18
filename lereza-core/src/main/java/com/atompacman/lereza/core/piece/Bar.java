package com.atompacman.lereza.core.piece;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.atompacman.lereza.core.analysis.MusicalStructure;

public final class Bar implements MusicalStructure, Iterable<BarSlice> {

    private List<BarSlice> slices;
    
    public Bar() {
        this.slices = new LinkedList<>();
    }
    
    public BarSlice getSlice(int i) {
        return slices.get(i);
    }

    public int numSlices() {
        return slices.size();
    }
    
    @Override
    public Iterator<BarSlice> iterator() {
        return slices.iterator();
    }
}
