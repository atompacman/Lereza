package com.atompacman.lereza.analysis;

import com.atompacman.lereza.analysis.profile.Profile;
import com.atompacman.lereza.analysis.profile.ProfileSet;
import com.atompacman.lereza.core.piece.Bar;
import com.atompacman.lereza.core.piece.Note;
import com.atompacman.lereza.core.piece.Part;
import com.atompacman.lereza.core.piece.Piece;
import com.atompacman.lereza.core.piece.Stack;
import com.atompacman.toolkat.module.Module;

public abstract class Analyzer extends Module {

    //======================================= METHODS ============================================\\

    //--------------------------------------- ANALYSIS -------------------------------------------\\

    // - - - - - - - - - - - - - - - - - - - - PIECE - - - - - - - - - - - - - - - - - - - - - - -\\

    public Profile analyzePiece(Piece<Stack<Note>> piece, ProfileSet dependencies) { 
        return null; 
    }
    
    public Profile analyzePartGroup(Part<Stack<Note>>[] parts, ProfileSet dependencies) { 
        return null; 
    }
    
    public Profile analyzePart(Part<Stack<Note>> part, ProfileSet dependencies) { 
        return null; 
    }
    
    //- - - - - - - - - - - - - - - - - - - - SECTION - - - - - - - - - - - - - - - - - - - - - - \\

    public Profile analyzeSection(Bar<Stack<Note>>[] section, ProfileSet dependencies) { 
        return null; 
    }
    
    public Profile analyzeSectionsOfPartGroup(Bar<Stack<Note>>[][]sections,ProfileSet dependencies){
        return null; 
    }
    
    public Profile analyzeSectionsOfParts(Bar<Stack<Note>>[][] sections, ProfileSet dependencies) { 
        return null; 
    }
  
    //- - - - - - - - - - - - - - - - - - - - - BAR - - - - - - - - - - - - - - - - - - - - - - - \\

    public Profile analyzeBar(Bar<Stack<Note>> bar, ProfileSet dependencies) { 
        return null; 
    }
    
    public Profile analyzeBarsOfPartGroup(Bar<Stack<Note>>[] bars, ProfileSet dependencies) { 
        return null; 
    }
    
    public Profile analyzeBarsOfParts(Bar<Stack<Note>> bars, ProfileSet dependencies) { 
        return null; 
    }

    // - - - - - - - - - - - - - - - - - - - NOTE STACK - - - - - - - - - - - - - - - - - - - - - \\

    public Profile analyzeNoteStack(Stack<Note> stack, ProfileSet dependencies) { 
        return null; 
    }

    public Profile analyzeNoteStacksOfPartGroup(Stack<Note>[] stacks, ProfileSet dependencies) { 
        return null; 
    }
    
    public Profile analyzeNoteStacksOfParts(Stack<Note>[] stacks, ProfileSet dependencies) { 
        return null; 
    }
    
    //- - - - - - - - - - - - - - - - - - - - - NOTE - -  - - - - - - - - - - - - - - - - - - - - \\


    public Profile analyzeNote(Note note, ProfileSet dependencies) { 
        return null; 
    }

    public Profile analyzeNotesOfPartGroup(Note[] notes, ProfileSet dependencies) { 
        return null; 
    }
    
    public Profile analyzeNotesOfParts(Note[] notes, ProfileSet dependencies) { 
        return null; 
    }
}
