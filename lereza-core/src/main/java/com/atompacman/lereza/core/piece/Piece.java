package com.atompacman.lereza.core.piece;

import java.util.Iterator;
import java.util.List;

import com.atompacman.lereza.core.analysis.structure.MusicalStructure;
import com.google.common.collect.ImmutableList;

public final class Piece extends MusicalStructure 
    implements StandardMusicalStructure, Iterable<PolyphonicPart> {

    //
    //  ~  FIELDS  ~  //
    //

    private final ImmutableList<PolyphonicPart> parts;
    
    
    //
    //  ~  INIT  ~  //
    //

    Piece(List<PolyphonicPart> parts) {
        this.parts = ImmutableList.copyOf(parts);
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //

    public PolyphonicPart getPart(int part) {
        return parts.get(part);
    }
    
    public Iterator<PolyphonicPart> iterator() {
        return parts.iterator();
    }

    
    //
    //  ~  STATE  ~  //
    //

    public boolean hasBeginningNote() {
        for (int i = 0; i < numParts(); ++i) {
            if (getPart(i).hasBeginningNote()) {
                return true;
            }
        }
        return false;
    }

    public int numParts() {
        return parts.size();
    }


    //
    //  ~  SERIALIZATION  ~  //
    //

    public String toStaccato() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numParts(); ++i) {
            sb.append('V').append(i).append(' ').append(getPart(i).toStaccato()).append(' ');
        }
        return sb.toString();
    }
}
