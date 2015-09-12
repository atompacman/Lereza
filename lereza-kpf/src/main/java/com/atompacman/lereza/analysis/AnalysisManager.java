package com.atompacman.lereza.analysis;

import com.atompacman.lereza.analysis.AnalyzerDescription.MainScope;
import com.atompacman.lereza.analysis.builtin.NoteCountAnalyzer;
import com.atompacman.lereza.analysis.profile.Study;
import com.atompacman.lereza.analysis.profile.target.PieceStructuralScope;
import com.atompacman.lereza.analysis.profile.target.PieceStructuralScope.HorizontalScope;
import com.atompacman.lereza.analysis.profile.target.PieceStructuralScope.VerticalScope;
import com.atompacman.lereza.core.piece.Note;
import com.atompacman.lereza.core.piece.Piece;
import com.atompacman.lereza.core.piece.Stack;
import com.atompacman.lereza.core.piece.tool.PieceNavigator;
import com.atompacman.toolkat.module.Module;

public class AnalysisManager extends Module {

    //======================================= FIELDS =============================================\\

    // :::: Temporaries ::::
    private Piece<Stack<Note>> piece;
    private Study study;
    
    
    
    //======================================= METHODS ============================================\\

    //--------------------------------- PUBLIC CONSTRUCTORS --------------------------------------\\

    public AnalysisManager() {
        // :::: Temporaries ::::
        this.piece = null;
        this.study = null;
    }
    
    //--------------------------------------- ANALYZE --------------------------------------------\\

    public Study analyze(Piece<Stack<Note>> piece) {
        study = new Study();
        runAnalyzer(new NoteCountAnalyzer());
        return study;
    }
    
    private void runAnalyzer(Analyzer analyzer) {
        // Extract analyzer description annotation
        AnalyzerDescription desc = analyzer.getClass().getAnnotation(AnalyzerDescription.class);
        if (desc == null) {
            throw new RuntimeException("Missing AnalyzerDescription annotation"
                    + " for \"" + analyzer.getClass().getSimpleName() + "\".");
        }
        
        // Run analyzer for every structural scope in the order defined in the description annot.
        if (desc.scopeProgression() == MainScope.HORIZONTAL) {
            for (HorizontalScope horiz : desc.horizontalScopes()) {
                for (VerticalScope verti: desc.verticalScopes()) {
                    runAnalyzerOnScope(analyzer, new PieceStructuralScope(horiz, verti));
                } 
            }
        } else {
            for (VerticalScope verti: desc.verticalScopes()) {
                for (HorizontalScope horiz : desc.horizontalScopes()) {
                    runAnalyzerOnScope(analyzer, new PieceStructuralScope(horiz, verti));
                } 
            }
        }
    }
    
    private void runAnalyzerOnScope(Analyzer analyzer, PieceStructuralScope scope) {
        switch (scope.getHoriScope()) {
        case NOTE:
            switch (scope.getVertScope()) {
            case PART:
                PieceNavigator<Stack<Note>> navig = new PieceNavigator<>(piece);
                for (int i = 0; i < piece.numParts(); ++i) {
                    int j = 0;
                    while (!navig.endOfPart()) {
                        navig.goToNextNote();
                        Stack<? extends Note> stack = navig.getNoteStack();
                        for (Note note : stack.getStartingNotes()) {
                            analyzer.analyzeNote(note, null, null);
                        }
                    }

                }
                break;
            case PIECE:
                break;
            default:
                throw new RuntimeException("Unimplemented");
            }
            break;
        case NOTE_STACK:
            switch (scope.getVertScope()) {
            case PART:
                break;
            case PIECE:
                break;
            default:
                throw new RuntimeException("Unimplemented");
            }
            break;
        case BAR:
            switch (scope.getVertScope()) {
            case PART:
                break;
            case PIECE:
                break;
            default:
                throw new RuntimeException("Unimplemented");
            }
            break;
        case PIECE:
            switch (scope.getVertScope()) {
            case PART:
                break;
            case PIECE:
                break;
            default:
                throw new RuntimeException("Unimplemented");
            }
            break;
        default:
            throw new RuntimeException("Unimplemented");
        }
    }
}
