package com.atompacman.lereza.core.analysis.output;

import com.atompacman.lereza.core.analysis.Analysis;

public final class BinaryAnalysisResult<T extends Analysis> extends AnalysisResult<T>{

    private boolean isSucess;
    
    public BinaryAnalysisResult(T analysis, boolean isSuccess) {
        super(analysis);
        this.isSucess = isSuccess;
    }
    
    public boolean isSuccess() {
        return isSucess;
    }
}
