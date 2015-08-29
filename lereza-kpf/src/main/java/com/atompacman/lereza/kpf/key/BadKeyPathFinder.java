package com.atompacman.lereza.kpf.key;

import java.io.File;

import com.atompacman.lereza.core.piece.Bar;
import com.atompacman.lereza.core.piece.Note;
import com.atompacman.lereza.core.piece.Part;
import com.atompacman.lereza.core.piece.Stack;

public class BadKeyPathFinder extends AbstractKeyPathFinder<Part<Stack<Note>>> {

    //======================================= METHODS ============================================\\

    //--------------------------------- PUBLIC CONSTRUCTORS --------------------------------------\\

    public BadKeyPathFinder(File keyConsonanceWindow) {
        super(keyConsonanceWindow);
    }


    //------------------------------------ TU LENGTH OF ------------------------------------------\\

    protected int timeunitLengthOf(Part<Stack<Note>> part) {
        return part.finalTimeunit();
    }


    //-------------------------------------- ADD NOTE --------------------------------------------\\

    protected void addNotes(Part<Stack<Note>> part) {
        int timeunit = 0;

        for (int i = 0; i < part.numBars(); ++i) {
            Bar<Stack<Note>> bar = part.getBar(i);
            for (int j = 0; j < bar.numTimeunits(); ++j) {
                Stack<Note> stack = bar.getNoteStack(j);
                for (Note note : stack.getPlayingNotes()) {
                    addNote(timeunit, timeunit + 1, note.getPitch().getTone().semitoneValue());
                }
                ++timeunit;
            }
        }
    }
}
