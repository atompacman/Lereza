package com.atompacman.lereza.analysis.builtin;

import java.util.List;

import com.atompacman.lereza.analysis.Analyzer;
import com.atompacman.lereza.analysis.AnalyzerDescription;
import com.atompacman.lereza.analysis.AnalyzerDescription.MainScope;
import com.atompacman.lereza.analysis.profile.Profile;
import com.atompacman.lereza.analysis.profile.target.PieceStructuralScope.HorizontalScope;
import com.atompacman.lereza.analysis.profile.target.PieceStructuralScope.VerticalScope;
import com.atompacman.lereza.core.piece.Note;
import com.atompacman.lereza.core.piece.Stack;

@AnalyzerDescription(
    scopeProgression = MainScope.VERTICAL,
    horizontalScopes = { HorizontalScope.NOTE_STACK, HorizontalScope.BAR, HorizontalScope.PIECE },
    verticalScopes   = { VerticalScope.PIECE, VerticalScope.PART }
)
public class NoteCountAnalyzer extends Analyzer {

    public Profile analyzeNoteStack(Stack<Note>   stack, 
                                    List<Profile> preprocessed, 
                                    List<Profile> dependencies) {
        
        return new NoteCountProfile(stack.getNumPlayingNotes()); 
    }
}
