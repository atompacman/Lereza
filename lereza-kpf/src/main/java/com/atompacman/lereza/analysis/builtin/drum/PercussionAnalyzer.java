package com.atompacman.lereza.analysis.builtin.drum;

import com.atompacman.lereza.analysis.Analyzer;
import com.atompacman.lereza.analysis.profile.Profile;
import com.atompacman.lereza.analysis.profile.ProfileSet;
import com.atompacman.lereza.core.piece.Note;
import com.atompacman.lereza.core.piece.Part;
import com.atompacman.lereza.core.piece.Stack;

public class PercussionAnalyzer extends Analyzer {

    //======================================= METHODS ============================================\\

    //--------------------------------------- ANALYSIS -------------------------------------------\\
    
    public Profile analyzePart(Part<Stack<Note>> part, ProfileSet dependencies) {
        return null;
    }
}
