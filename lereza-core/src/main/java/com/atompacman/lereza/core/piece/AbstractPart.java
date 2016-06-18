package com.atompacman.lereza.core.piece;

import java.util.List;

public abstract class AbstractPart<T extends AbstractBar<?>> implements PieceComponent {

    //======================================= FIELDS =============================================\\

    private final List<T> bars;



    //======================================= METHODS ============================================\\

    //---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\

    AbstractPart(List<T> bars) {
        this.bars = bars;
    }


    //--------------------------------------- GETTERS --------------------------------------------\\

    public List<T> getBars() {
        return bars;
    }


    //---------------------------------------- STATE ---------------------------------------------\\

    public boolean isEmpty() {
        for (T bar : bars) {
            if (!bar.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public int numBars() {
        return bars.size();
    }


    //-------------------------------------- TO STRING -------------------------------------------\\

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