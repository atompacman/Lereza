package com.atompacman.lereza.core.analysis.filter;

import com.atompacman.lereza.core.analysis.MusicalStructure;

public abstract class ScoredFilter<M extends MusicalStructure> implements Filter<M> {

    //
    //  ~  FIELDS  ~  //
    //
    
    private final double threshold;
    
    
    //
    //  ~  INIT  ~  //
    //
    
    protected ScoredFilter(double threshold) {
        this.threshold = threshold;
    }
    
    
    //
    //  ~  APPLY  ~  //
    //
    
    public boolean apply(M structure) {
        return evaluateScore(structure) >= threshold;
    }

    protected abstract double evaluateScore(M structure);
}
