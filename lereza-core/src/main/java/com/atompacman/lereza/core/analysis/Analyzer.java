package com.atompacman.lereza.core.analysis;

import com.atompacman.lereza.core.analysis.profile.Profile;
import com.atompacman.lereza.core.analysis.profile.ProfileSet;
import com.atompacman.lereza.core.piece.Bar;
import com.atompacman.lereza.core.piece.Note;
import com.atompacman.lereza.core.piece.Part;
import com.atompacman.lereza.core.piece.Piece;
import com.atompacman.lereza.core.piece.NoteStack;
import com.atompacman.toolkat.module.Module;

public abstract class Analyzer extends Module {

    //======================================= METHODS ============================================\\

    //--------------------------------------- ANALYSIS -------------------------------------------\\

    // - - - - - - - - - - - - - - - - - - - - PIECE - - - - - - - - - - - - - - - - - - - - - - -\\

    public Profile analyzePiece(Piece piece, ProfileSet dependencies) { 
        return null; 
    }
    
    public Profile analyzePartGroup(Part[] parts, ProfileSet dependencies) { 
        return null; 
    }
    
    public Profile analyzePart(Part part, ProfileSet dependencies) { 
        return null; 
    }
    
    //- - - - - - - - - - - - - - - - - - - - SECTION - - - - - - - - - - - - - - - - - - - - - - \\

    public Profile analyzeSection(Bar[] section, ProfileSet dependencies) { 
        return null; 
    }
    
    public Profile analyzeSectionsOfPartGroup(Bar[][]sections, ProfileSet dependencies) {
        return null; 
    }
    
    public Profile analyzeSectionsOfParts(Bar[][] sections, ProfileSet dependencies) { 
        return null; 
    }
  
    //- - - - - - - - - - - - - - - - - - - - - BAR - - - - - - - - - - - - - - - - - - - - - - - \\

    public Profile analyzeBar(Bar bar, ProfileSet dependencies) { 
        return null; 
    }
    
    public Profile analyzeBarsOfPartGroup(Bar[] bars, ProfileSet dependencies) { 
        return null; 
    }
    
    public Profile analyzeBarsOfParts(Bar bars, ProfileSet dependencies) { 
        return null; 
    }

    // - - - - - - - - - - - - - - - - - - - Note STACK - - - - - - - - - - - - - - - - - - - - - \\

    public Profile analyzeNoteStack(NoteStack stack, ProfileSet dependencies) { 
        return null; 
    }

    public Profile analyzeNoteStacksOfPartGroup(NoteStack[] stacks, ProfileSet dependencies) { 
        return null; 
    }
    
    public Profile analyzeNoteStacksOfParts(NoteStack[] stacks, ProfileSet dependencies) { 
        return null; 
    }
    
    //- - - - - - - - - - - - - - - - - - - - - Note - -  - - - - - - - - - - - - - - - - - - - - \\

    public Profile analyzeNote(Note TiedNote, ProfileSet dependencies) { 
        return null; 
    }

    public Profile analyzeNotesOfPartGroup(Note[] TiedNotes, ProfileSet dependencies) { 
        return null; 
    }
    
    public Profile analyzeNotesOfParts(Note[] TiedNotes, ProfileSet dependencies) { 
        return null; 
    }
}
