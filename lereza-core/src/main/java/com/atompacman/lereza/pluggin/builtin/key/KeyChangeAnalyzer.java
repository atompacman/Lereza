package com.atompacman.lereza.pluggin.builtin.key;

import java.io.File;

import com.atompacman.lereza.core.analysis.profile.Profile;
import com.atompacman.lereza.core.analysis.profile.ProfileSet;
import com.atompacman.lereza.core.piece.Bar;
import com.atompacman.lereza.core.piece.Note;
import com.atompacman.lereza.core.piece.Part;
import com.atompacman.lereza.core.piece.NoteStack;

public class KeyChangeAnalyzer extends AbstractKeyChangeDetector<Part> {

    //======================================= METHODS ============================================\\

    //--------------------------------- PUBLIC CONSTRUCTORS --------------------------------------\\

    public KeyChangeAnalyzer(File   keyConsonanceWindow, 
                             int    minKeyChangeGap, 
                             double keyChangeSensibility) {
        
        super(keyConsonanceWindow, minKeyChangeGap, keyChangeSensibility);
    }

    //--------------------------------------- ANALYSIS -------------------------------------------\\

    public Profile analyzePart(Part part, ProfileSet dependencies) {
        detect(part);
        return null;
    }
    
    //------------------------------------ TU LENGTH OF ------------------------------------------\\

    protected int timeunitLengthOf(Part part) {
        return part.finalTimeunit();
    }


    //-------------------------------------- ADD NOTE --------------------------------------------\\

    protected void addNotes(Part part) {
        int timeunit = 0;

        for (int i = 0; i < part.numBars(); ++i) {
            Bar bar = part.getBar(i);
            for (int j = 0; j < bar.numTimeunits(); ++j) {
                NoteStack stack = bar.getNoteStack(j);
                for (Note note : stack.getPlayingNotes()) {
                    addNote(timeunit, timeunit + 1, note.getPitch().getTone().semitoneValue());
                }
                ++timeunit;
            }
        }
    }
}
