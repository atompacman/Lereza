package com.atompacman.lereza.pluggin.builtin.notecount;

import com.atompacman.lereza.core.analysis.Analyzer;
import com.atompacman.lereza.core.analysis.AnalyzerDescription;
import com.atompacman.lereza.core.analysis.AnalyzerDescription.MainScope;
import com.atompacman.lereza.core.analysis.profile.PieceStructuralScope.HorizontalScope;
import com.atompacman.lereza.core.analysis.profile.PieceStructuralScope.VerticalScope;
import com.atompacman.lereza.core.analysis.profile.Profile;
import com.atompacman.lereza.core.analysis.profile.ProfileSet;
import com.atompacman.lereza.core.piece.NoteStack;

@AnalyzerDescription(
    scopeProgression = MainScope.VERTICAL,
    horizontalScopes = { HorizontalScope.NOTE_STACK, HorizontalScope.BAR, HorizontalScope.PIECE },
    verticalScopes   = { VerticalScope.PIECE, VerticalScope.PART }
)
public class NoteCountAnalyzer extends Analyzer {

    public Profile analyzeNoteStack(NoteStack stack, ProfileSet dependencies) {
        return new NoteCountProfile(stack.countStartingUntiedNotes()); 
    }
}
