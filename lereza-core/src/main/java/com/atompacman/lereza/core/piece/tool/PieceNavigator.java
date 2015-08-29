package com.atompacman.lereza.core.piece.tool;

import com.atompacman.lereza.core.piece.Bar;
import com.atompacman.lereza.core.piece.Note;
import com.atompacman.lereza.core.piece.Part;
import com.atompacman.lereza.core.piece.Piece;
import com.atompacman.lereza.core.piece.Stack;
import com.atompacman.toolkat.exception.Throw;
import com.atompacman.toolkat.misc.Log;

public class PieceNavigator<T extends Stack<?>> {

    //======================================= FIELDS =============================================\\

    private final Piece<T> piece;

    private PBT pbt;
    private int noteInBar;
    private int noteInPart;



    //======================================= METHODS ============================================\\

    //---------------------------------- PUBLIC CONSTRUCTOR --------------------------------------\\

    public PieceNavigator(Piece<T> piece) {
        if (piece.numParts() == 0) {
            throwExcep("Cannot navigate in a piece with no parts.");
        }
        this.piece      = piece;
        
        this.pbt        = new PBT(0,0,0);
        this.noteInBar  = 0;
        this.noteInPart = 0;
        
        goToBeginningOfPiece();
    }


    //---------------------------------- NAVIGATE IN PARTS ---------------------------------------\\

    public void goToNextPart() {
        if (endOfPiece()) {
            throwExcep("Cannot go to next part: end of piece reached.");
        }
        ++pbt.part;
        pbt.bar      = 0;
        pbt.timeunit = 0;

        noteInBar    = 0;
        noteInPart   = 0;
    }

    public void goToPreviousPart() {
        if (pbt.part == 0) {
            throwExcep("Cannot go to previous part: current part is the first one.");
        }
        --pbt.part;
        pbt.bar      = 0;
        pbt.timeunit = 0;

        noteInPart   = 0;
        noteInBar    = 0;
    }

    public void goToPart(int partNo) {
        if (partNo >= piece.numParts() || partNo < 0) {
            throwExcep("Cannot go to part \"%d\": Current piece only "
                    + "has %d parts.", partNo, piece.numParts());
        }
        while (partNo > pbt.part) {
            goToNextPart();
        }
        while (partNo < pbt.part) {
            goToPreviousPart();
        }
    }

    public void goToFirstUnemptyPart() {
        pbt.part     = 0;
        pbt.bar      = 0;
        pbt.timeunit = 0;

        noteInPart   = 0;
        noteInBar    = 0;

        try {
            while (getCurrentPart().isEmpty()) {
                goToNextPart();
            }
        } catch (Exception e) {
            throwExcep("Cannot go to first unempty part: There are no unempty parts.", e);
        }
    }


    //----------------------------------- NAVIGATE IN BARS ---------------------------------------\\

    public void goToNextBar() {
        if (endOfPart()) {
            throwExcep("Cannot go to next bar: Current bar is the last one.");
        }
        noteInPart -= noteInBar;
        noteInPart += countNotesInBar();

        ++pbt.bar;
        pbt.timeunit = 0;

        noteInBar    = 0;
    }

    public void goToPreviousBar() {
        if (pbt.bar == 0) {
            throwExcep("Cannot go to previous bar: Current bar is the first one.");
        }
        noteInPart -= noteInBar;
        noteInPart -= countNotesInBar();

        --pbt.bar;
        pbt.timeunit = 0;
        
        noteInBar    = 0;
    }

    public void goToBar(int barNo) {
        if (barNo >= getCurrentPart().numBars() || barNo < 0) {
            throwExcep("Cannot go to bar \"%d\": The current part only "
                    + "has %d bars.", barNo, getCurrentPart().numBars());
        }
        while (barNo > pbt.bar) {
            goToNextBar();
        }
        while (barNo < pbt.bar) {
            goToPreviousBar();
        }
    }

    public void goToFirstUnemptyBar() {
        pbt.bar      = 0;
        pbt.timeunit = 0;
        
        noteInBar    = 0;
        noteInPart   = 0;

        try {
            while (getCurrentBar().isEmpty()) {
                goToNextBar();
            }
        } catch (Exception e) {
            throwExcep("Cannot go to first unempty bar of part %d: "
                    + "there are no unempty bars.", e, pbt.part);
        }
    }


    //-------------------------------- NAVIGATE IN TIMEUNITS -------------------------------------\\

    public void goToNextTimeunit() {
        ++pbt.timeunit;
        if (pbt.timeunit < piece.getRythmicSignature().timeunitsInABar()) {
            int nbNotesInStack = countStartingNotesInStack();
            noteInBar  += nbNotesInStack;
            noteInPart += nbNotesInStack;
        } else {
            goToNextBar();
        }
    }

    public void goToPreviousTimeunit() {
        if (pbt.timeunit != 0) {
            --pbt.timeunit;
            int nbNotesInStack = countStartingNotesInStack();
            noteInBar  -= nbNotesInStack;
            noteInPart -= nbNotesInStack;
        } else {
            goToPreviousBar();
        }
    }

    public void goToTimeunitNo(int timeunitNo) {
        if (timeunitNo >= countStartingNotesInStack() || timeunitNo < 0) {
            throwExcep("Cannot go to timeunit \"%d\": Current bar only has %d "
                    + "timeunits.", timeunitNo, countStartingNotesInStack());
        }
        while (timeunitNo > pbt.timeunit) {
            goToNextTimeunit();
        }
        while (timeunitNo < pbt.timeunit) {
            goToPreviousTimeunit();
        }
    }


    //---------------------------------- NAVIGATE IN NOTES ---------------------------------------\\

    public void goToNextNote() {
        goToNextTimeunit();
        while (!endOfPart() && !currentNotesStartsSomething()) {
            goToNextTimeunit();
        }
    }

    public void goToPreviousNote() {
        goToPreviousTimeunit();
        while (!currentNotesStartsSomething()) {
            goToPreviousTimeunit();
        }
    }

    public void goToNoteNo(int noteNo) {
        if (noteNo > countNotesInBar() || noteNo <= 0) {
            throwExcep("Cannot go to note \"%d\": Current "
                    + "bar only has %d notes.", noteNo, countNotesInBar());
        }
        while (noteNo > noteInBar + countStartingNotesInStack()) {
            goToNextNote();
        }
        while (noteNo < noteInBar) {
            goToPreviousNote();
        }
    }

    public void goToFirstNoteOfBar() {
        goToNoteNo(1);
    }

    public void goToBeginningOfPiece() {
        goToFirstUnemptyPart();
        goToFirstUnemptyBar();
        goToFirstNoteOfBar();
    }


    //--------------------------------------- GETTERS --------------------------------------------\\

    public final Part<T> getCurrentPart() {
        return piece.getPart(pbt.part);
    }

    public final Bar<T> getCurrentBar() {
        return getCurrentPart().getBar(pbt.bar);
    }

    public final Stack<? extends Note> getNoteStack() {
        return getCurrentBar().getNoteStack(pbt.timeunit);
    }


    //---------------------------------------- STATE ---------------------------------------------\\

    public boolean endOfPiece() {
        return pbt.part == piece.numParts();
    }

    public boolean endOfPart() {
        return pbt.bar == getCurrentPart().numBars();
    }

    public int getCurrentTimestamp() {
        return pbt.bar * piece.getRythmicSignature().timeunitsInABar() + pbt.timeunit;
    }

    public final PBT getPBT() {
        return pbt;
    }

    private boolean currentNotesStartsSomething() {
        return countStartingNotesInStack() != 0;
    }


    //---------------------------------------- COUNT ---------------------------------------------\\

    public int countNotesInBar() {
        return getCurrentBar().getNumStartingNotes();
    }

    public int countStartingNotesInStack() {
        return getNoteStack().getNumStartingNotes();
    }


    //-------------------------------------- TO STRING -------------------------------------------\\

    public void printLocation() {
        Log.info(getFullLocation());
    }

    public String getFullLocation() {
        return String.format("PieceNavigator - " + getPBT().toString() + 
                " - Part %1d - Bar %3d - Timestamp %5d - Note in part %3d - "
                + "Note in bar %2d - Timeunit in bar %2d", 
                pbt.part, pbt.bar, getCurrentTimestamp(), 
                noteInPart, noteInBar, pbt.timeunit);
    }


    //------------------------------------ THROW EXCEPTION ---------------------------------------\\

    private void throwExcep(String format, Object...args) {
        throwExcep(format, null, args);
    }

    private void throwExcep(String format, Throwable cause, Object...args) {
        String msg =  String.format("%s: " + format, pbt.toString(), args);
        Throw.aRuntime(PieceNavigatorException.class, msg, cause);
    }
}
