package com.atompacman.lereza.core.piece2;

import java.util.List;

import com.google.common.collect.ImmutableList;

abstract class Piece<T extends Part<?>> {

    //
    //  ~  FIELDS  ~  //
    //

    private final ImmutableList<T> parts;


    //
    //  ~  INIT  ~  //
    //

    protected Piece(List<T> parts) {
        this.parts = ImmutableList.copyOf(parts);
    }


    //
    //  ~  GETTERS  ~  //
    //

    public ImmutableList<T> getParts() {
        return parts;
    }


    //
    //  ~  STATE  ~  //
    //

    public boolean hasBeginningNote() {
        for (T bar : parts) {
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