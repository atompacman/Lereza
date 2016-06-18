package com.atompacman.lereza.core.piece;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.atompacman.lereza.core.analysis.MusicalStructure;

public final class Part implements MusicalStructure, Iterable<Bar> {

    private List<Bar> bars;
    
    public Part() {
        this.bars = new LinkedList<>();
    }
    
    public Bar getBar(int i) {
        return bars.get(i);
    }

    public int numBars() {
        return bars.size();
    }
    
    @Override
    public Iterator<Bar> iterator() {
        return bars.iterator();
    }
}
