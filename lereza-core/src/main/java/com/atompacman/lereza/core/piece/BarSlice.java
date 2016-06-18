package com.atompacman.lereza.core.piece;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.atompacman.lereza.core.analysis.MusicalStructure;

public class BarSlice implements MusicalStructure, Iterable<PolyphonicNoteNode> {

    private List<PolyphonicNoteNode> nodes;
    
    public BarSlice() {
        this.nodes = new LinkedList<>();
    }
    
    public PolyphonicNoteNode getNode(int i) {
        return nodes.get(i);
    }

    public int numNodes() {
        return nodes.size();
    }
    
    @Override
    public Iterator<PolyphonicNoteNode> iterator() {
        return nodes.iterator();
    }
}