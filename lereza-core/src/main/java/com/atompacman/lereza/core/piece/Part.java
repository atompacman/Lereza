package com.atompacman.lereza.core.piece;

import java.util.List;

import com.atompacman.lereza.core.solfege.TimeSignature;

public final class Part<T extends Stack<? extends TiedNote>> implements PieceComponent {

    //======================================= FIELDS =============================================\\

    private final List<Bar<T>>  bars;
    private final TimeSignature timeSign;



    //======================================= METHODS ============================================\\

    //---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\

    Part(List<Bar<T>> bars, TimeSignature timeSign) {
        this.bars     = bars;
        this.timeSign = timeSign;
    }


    //--------------------------------------- GETTERS --------------------------------------------\\

    public Bar<T> getBar(int bar) {
        if (bar < 0 || bar >= bars.size()) {
            throw new IllegalArgumentException("Cannot access bar no." + 
                    bar + "\": Part has " + bars.size() + " bars.");
        }
        return bars.get(bar);
    }

    public Bar<T> getBarAt(int timestamp) {
        return getBar((int)((double) timestamp / (double) timeSign.timeunitsInABar()));
    }

    public TimeSignature getTimeSignature() {
        return timeSign;
    }


    //---------------------------------------- STATE ---------------------------------------------\\

    public boolean isEmpty() {
        for (Bar<T> bar : bars) {
            if (!bar.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public int numBars() {
        return bars.size();
    }

    public int numNotes() {
        int sum = 0;
        for (Bar<T> bar : bars) {
            sum += bar.getNumStartingNotes();
        }
        return sum;
    }

    public int finalTimeunit() {
        return bars.size() * timeSign.timeunitsInABar();
    }


    //-------------------------------------- TO STRING -------------------------------------------\\

    public String toStaccato() {
        StringBuilder sb = new StringBuilder();
        sb.append("I[").append("PIANO").append("] ");
        for (Bar<T> bar : bars) {
            sb.append(bar.toStaccato()).append(" | ");
        }
        sb.setLength(sb.length() - 2);
        return sb.toString();
    }
}
