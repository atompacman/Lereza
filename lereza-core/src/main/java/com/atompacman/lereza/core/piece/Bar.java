package com.atompacman.lereza.core.piece;

import java.util.List;

import com.atompacman.lereza.core.solfege.RythmicSignature;
import com.atompacman.lereza.core.solfege.Value;

public final class Bar<T extends Stack<? extends Note>> implements PieceComponent {

    //======================================= FIELDS =============================================\\

    private final List<T> noteStacks;
    private final RythmicSignature rythmicSign;



    //======================================= METHODS ============================================\\

    //---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\

    Bar(List<T> noteStacks, RythmicSignature rythmicSign) {
        this.noteStacks = noteStacks;
        this.rythmicSign = rythmicSign;
    }


    //--------------------------------------- GETTERS --------------------------------------------\\

    public T getNoteStack(int timeunit) {
        if (timeunit < 0 || timeunit >= rythmicSign.timeunitsInABar()) {
            throw new IllegalArgumentException("Cannot access note stack at timeunit \"" + timeunit 
                    + "\": bar is only " + rythmicSign.timeunitsInABar() + " timunits long");
        }
        return noteStacks.get(timeunit);
    }

    @SuppressWarnings("unchecked")
    public <N extends Note> N getNote(int timeunit) {
        T stack = getNoteStack(timeunit);
        if (stack.getNumStartingNotes() != 1) {
            throw new IllegalArgumentException("Expecting only one "
                    + "starting note at timeunit \"" + timeunit + "\"");
        }
        return (N) stack.getStartingNotes().iterator().next();
    }

    public RythmicSignature getRythmicSignature() {
        return rythmicSign;
    }


    //---------------------------------------- STATE ---------------------------------------------\\

    public boolean isEmpty() {
        for (Stack<? extends Note> stack : noteStacks) {
            if (stack.hasPlayingNotes()) {
                return false;
            }
        }
        return true;
    }

    public int getNumStartingNotes() {
        int sum = 0;
        for (Stack<? extends Note> stack : noteStacks) {
            sum += stack.getNumStartingNotes();
        }
        return sum;
    }

    public int numTimeunits() {
        return rythmicSign.timeunitsInABar();
    }


    //-------------------------------------- TO STRING -------------------------------------------\\

    public String toStaccato() {
        StringBuilder sb = new StringBuilder();
        int rests = 0;
        String notes = null;
        
        for (int i = 0; i < numTimeunits(); ++i) {
            T stack = noteStacks.get(i);
            if (stack.hasStartingNotes()) {
                if (notes != null) {
                    sb.append(notes).append('+');
                }
                if (rests > 0) {
                    sb.append('R');
                    for (Value value : Value.splitIntoValues(rests)) {
                        sb.append(value.toStaccato());
                    }
                    sb.append(' ');
                }
                notes = stack.toStaccato();
                rests = 0;
            } else {
                ++rests;
            }
        }
        
        if (notes != null) {
            sb.append(notes);
        }
        return sb.toString();
    }
}
