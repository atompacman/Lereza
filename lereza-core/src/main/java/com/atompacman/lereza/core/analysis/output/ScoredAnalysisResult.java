package com.atompacman.lereza.core.analysis.output;

import com.atompacman.lereza.core.analysis.Analysis;

public final class ScoredAnalysisResult<T extends Analysis> extends AnalysisResult<T> {

    private double score;
    
    public ScoredAnalysisResult(T analysis, double score) {
        super(analysis);
        this.score = score;
    }
    
    public double getScore() {
        return score;
    }
}
