package com.atompacman.lereza.ryff;

import com.atompacman.lereza.core.analysis.AnalysisManager;
import com.atompacman.lereza.core.analysis.AnalysisMethod;
import com.atompacman.lereza.core.analysis.Analyzer;
import com.atompacman.lereza.core.analysis.SplitterMethod;
import com.atompacman.lereza.core.analysis.profile.Profile;
import com.atompacman.lereza.core.piece.Bar;
import com.atompacman.lereza.core.piece.Part;
import com.atompacman.lereza.core.piece.Piece;

public class Exemple {
    
    public static class ProfileA extends Profile {}
    public static class ProfileB extends Profile {}
    public static class ProfileC extends Profile {}
    
    public static class AnalyzerA extends Analyzer { public AnalyzerA(AnalysisManager manager) { super(manager); }
        @AnalysisMethod public ProfileA method1(Piece piece) { return null; }
    }

    public static class AnalyzerB extends Analyzer { public AnalyzerB(AnalysisManager manager) { super(manager); }        
        @AnalysisMethod public ProfileB method2(Piece piece, ProfileA a) { return null; }
        @SplitterMethod public Part[] split1 (Piece piece) { return null; }
        @AnalysisMethod public ProfileB method3(Part part) { return null; }
        @AnalysisMethod public ProfileC method4(Bar bar, ProfileA a, ProfileB b) { return null; }
    }
}
