package com.atompacman.lereza.core.piece;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPiece<T extends AbstractPart<?>> implements PieceComponent {

    //======================================= FIELDS =============================================\\

    private final List<T> parts;



    //======================================= METHODS ============================================\\

    //---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\

    AbstractPiece(List<T> parts) {
        this.parts = parts;
    }


    //--------------------------------------- GETTERS --------------------------------------------\\

    public List<T> getParts() {
        return new ArrayList<>(parts);
    }


    //---------------------------------------- STATE ---------------------------------------------\\

    public int numParts() {
        return parts.size();
    }


    //-------------------------------------- TO STRING -------------------------------------------\\

    public String toStaccato() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.size(); ++i) {
            sb.append('V').append(i).append(' ').append(parts.get(i).toStaccato()).append(' ');
        }
        return sb.toString();
    }
}
