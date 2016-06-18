package com.atompacman.lereza.core.analysis.output;

import com.atompacman.lereza.core.analysis.Analysis;

public class AnalysisResult<T extends Analysis> {

    private Analysis analysis;
    
    public AnalysisResult(Analysis analysis) {
        this.analysis = analysis;
    }
    
    public Analysis getAnalysis() {
        return analysis;
    }
}
