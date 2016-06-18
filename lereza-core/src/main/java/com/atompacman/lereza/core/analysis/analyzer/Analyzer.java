package com.atompacman.lereza.core.analysis.analyzer;

import com.atompacman.lereza.core.analysis.Analysis;
import com.atompacman.lereza.core.analysis.MusicalStructure;
import com.atompacman.lereza.core.analysis.output.AnalysisResult;

public abstract class Analyzer<A extends Analysis, 
                               O extends AnalysisResult<A>, 
                               M extends MusicalStructure> {

    public boolean isStructureSupported(M structure) {
        return true;
    }
    
    public abstract O analyze(M structure);
}
