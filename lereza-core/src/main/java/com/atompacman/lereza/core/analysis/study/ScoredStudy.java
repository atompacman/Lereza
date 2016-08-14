package com.atompacman.lereza.core.analysis.study;

public abstract class ScoredStudy extends Study {

    //
    //  ~  FIELDS  ~  //
    //
    
    private final double score;
    
    
    //
    //  ~  INIT  ~  //
    //
    
    public ScoredStudy(double score) {
        super();
        this.score = score;
        
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //
    
    public double getScore() {
        return score;
    }
}
