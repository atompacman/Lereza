package com.atompacman.lereza.core.piece;

import java.util.List;

import com.atompacman.lereza.core.solfege.TimeSignature;
import com.atompacman.lereza.core.solfege.Value;

public final class Bar implements PieceComponent {

    //======================================= FIELDS =============================================\\

    private final List<NoteStack> noteStacks;
    private final TimeSignature   timeSign;



    //======================================= METHODS ============================================\\

    //---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\

    Bar(List<NoteStack> noteStacks, TimeSignature timeSign) {
        this.noteStacks = noteStacks;
        this.timeSign   = timeSign;
    }


    //--------------------------------------- GETTERS --------------------------------------------\\

    public NoteStack getNoteStack(int timeunit) {
        if (timeunit < 0 || timeunit >= timeSign.timeunitsInABar()) {
            throw new IllegalArgumentException("Cannot access note stack at timeunit \"" + timeunit 
                    + "\": bar is only " + timeSign.timeunitsInABar() + " timunits long");
        }
        return noteStacks.get(timeunit);
    }

    public Note getNote(int timeunit) {
        NoteStack stack = getNoteStack(timeunit);
        if (stack.countStartingNotes() != 1) {
            throw new IllegalArgumentException("Expecting only one "
                    + "starting note at timeunit \"" + timeunit + "\"");
        }
        return stack.getStartingNotes().iterator().next();
    }

    public TimeSignature getTimeSignature() {
        return timeSign;
    }


    //---------------------------------------- STATE ---------------------------------------------\\

    public boolean isEmpty() {
        for (NoteStack stack : noteStacks) {
            if (stack.hasPlayingNotes()) {
                return false;
            }
        }
        return true;
    }

    public int getNumStartingUntiedNotes() {
        int sum = 0;
        for (NoteStack stack : noteStacks) {
            sum += stack.countStartingUntiedNotes();
        }
        return sum;
    }
    
    public int getNumStartingNotes() {
        int sum = 0;
        for (NoteStack stack : noteStacks) {
            sum += stack.countStartingNotes();
        }
        return sum;
    }

    public int numTimeunits() {
        return timeSign.timeunitsInABar();
    }


    //-------------------------------------- TO STRING -------------------------------------------\\

    public String toStaccato() {
        StringBuilder sb = new StringBuilder();
        int rests = 0;
        
        for (int i = 0; i < numTimeunits(); ++i) {
            NoteStack stack = noteStacks.get(i);
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
