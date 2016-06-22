package com.atompacman.lereza.core.piece;

import java.util.List;

import com.atompacman.lereza.core.analysis.MusicalStructure;
import com.google.common.collect.ImmutableList;

public final class Piece extends MusicalStructure {

    //
    //  ~  FIELDS  ~  //
    //

    private final ImmutableList<PolyphonicPart> parts;


    //
    //  ~  INIT  ~  //
    //

    protected Piece(List<PolyphonicPart> parts) {
        this.parts = ImmutableList.copyOf(parts);
    }


    //
    //  ~  GETTERS  ~  //
    //

    public ImmutableList<PolyphonicPart> getParts() {
        return parts;
    }


    //
    //  ~  STATE  ~  //
    //

    public boolean hasBeginningNote() {
        for (PolyphonicPart bar : parts) {
            if (bar.hasBeginningNote()) {
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
        for (int i = 0; i < parts.size(); ++i) {
            sb.append('V').append(i).append(' ').append(parts.get(i).toStaccato()).append(' ');
        }
        return sb.toString();
    }
}