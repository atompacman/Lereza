package com.atompacman.lereza.core.piece;

import java.util.List;

import com.atompacman.lereza.core.theory.Value;

public abstract class AbstractBar<T extends AbstractNoteStack> implements PieceComponent {

    //======================================= FIELDS =============================================\\

    private final List<T> noteStacks;



    //======================================= METHODS ============================================\\

    //---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\

    AbstractBar(List<T> noteStacks) {
        this.noteStacks = noteStacks;
    }


    //--------------------------------------- GETTERS --------------------------------------------\\

    public T getNoteStack(int timeunit) {
        if (timeunit < 0 || timeunit >= noteStacks.size()) {
            throw new IllegalArgumentException("Cannot access note stack at timeunit \"" 
                    + timeunit + "\": bar is only " + noteStacks.size() + " timunits long");
        }
        return noteStacks.get(timeunit);
    }

    public List<T> getNoteStacks() {
        return noteStacks;
    }
    
    public Note getNote(int timeunit) {
        T stack = getNoteStack(timeunit);
        if (stack.getStartingNotes().size() != 1) {
            throw new IllegalArgumentException("Expecting only one "
                    + "starting note at timeunit \"" + timeunit + "\"");
        }
        return stack.getStartingNotes().iterator().next();
    }


    //---------------------------------------- STATE ---------------------------------------------\\

    public boolean isEmpty() {
        for (T stack : noteStacks) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public int getLengthTU() {
        return noteStacks.size();
    }


    //-------------------------------------- TO STRING -------------------------------------------\\

    public String toStaccato() {
        StringBuilder sb = new StringBuilder();
        int rests = 0;
        
        for (T stack : noteStacks) {
            if (stack.hasStartingNotes()) {
                if (rests > 0) {
                    sb.append('R');
                    for (Value value : Value.splitIntoValues(rests)) {
                        sb.append(value.toStaccato());
                    }
                    sb.append(' ');
                }
                sb.append(stack.toStaccato()).append(' ');
                rests = 0;

            } else if (!stack.hasStartedNotes()){
                ++rests;
            }
        }

        return sb.toString();
    }
}
