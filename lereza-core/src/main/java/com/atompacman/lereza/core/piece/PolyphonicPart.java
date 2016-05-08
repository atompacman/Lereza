package com.atompacman.lereza.core.piece;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class PolyphonicPart implements MusicalStructure {

    //
    //  ~  FIELDS  ~  //
    //

    private final ImmutableList<PolyphonicBar> bars;


    //
    //  ~  INIT  ~  //
    //

    protected PolyphonicPart(List<PolyphonicBar> bars) {
        this.bars = ImmutableList.copyOf(bars);
    }


    //
    //  ~  GETTERS  ~  //
    //

    public PolyphonicBar getBar(int bar) {
        return bars.get(bar);
    }


    //
    //  ~  STATE  ~  //
    //

    public boolean hasBeginningNote() {
        for (PolyphonicBar bar : bars) {
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
        for (PolyphonicBar bar : bars) {
            sb.append(bar.toStaccato()).append(" | ");
        }
        sb.setLength(sb.length() - 2);
        return sb.toString();
    }
}
