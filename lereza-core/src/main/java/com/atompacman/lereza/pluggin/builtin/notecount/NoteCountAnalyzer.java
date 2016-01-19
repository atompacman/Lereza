package com.atompacman.lereza.pluggin.builtin.notecount;

import com.atompacman.lereza.core.analysis.OLDAnalyzer;
import com.atompacman.lereza.core.analysis.OLDAnalyzerDescription;
import com.atompacman.lereza.core.analysis.profile.PieceStructuralScope.PartGrouping;
import com.atompacman.lereza.core.analysis.profile.PieceStructuralScope.PartSubstructure;
import com.atompacman.lereza.core.analysis.profile.Profile;
import com.atompacman.lereza.core.analysis.profile.ProfileSet;
import com.atompacman.lereza.core.piece.NoteStack;

@OLDAnalyzerDescription (
    areSubstructuresInOuterLoop = false,
    partSubstructure            = { PartSubstructure.NOTE_STACK },
    partGrouping                = { PartGrouping.SINGLE }
)
public class NoteCountAnalyzer extends OLDAnalyzer {

    public Profile analyzeNoteStack(NoteStack stack, ProfileSet dependencies) {
        return new NoteCountProfile(stack.countStartingUntiedNotes()); 
    }
}
