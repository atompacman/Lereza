package com.atompacman.lereza.core.piece;

import java.util.List;

import com.atompacman.lereza.core.analysis.MusicalStructure;
import com.google.common.collect.ImmutableList;

abstract class Part<T extends Bar<?>> implements MusicalStructure {

    //
    //  ~  FIELDS  ~  //
    //

    private final ImmutableList<T> bars;


    //
    //  ~  INIT  ~  //
    //

    protected Part(List<T> bars) {
        this.bars = ImmutableList.copyOf(bars);
    }


    //
    //  ~  GETTERS  ~  //
    //

    public ImmutableList<T> getBars() {
        return bars;
    }


    //
    //  ~  STATE  ~  //
    //

    public boolean hasBeginningNote() {
        for (T bar : bars) {
            if (bar.hasBeginningNote()) {
                return true;
            }
        }
        return false;
    }

    public int numBars() {
        return bars.size();
    }


    //
    //  ~  SERIALIZATION  ~  //
    //

    public String toStaccato() {
        StringBuilder sb = new StringBuilder();
        sb.append("I[").append("PIANO").append("] ");
        for (T bar : bars) {
            sb.append(bar.toStaccato()).append(" | ");
        }
        sb.setLength(sb.length() - 2);
        return sb.toString();
    }
}