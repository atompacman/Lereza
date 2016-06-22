package com.atompacman.lereza.core.analysis.study;

public abstract class FailableStudy extends Study {

    //
    //  ~  FIELDS  ~  //
    //
    
    private final boolean isAccepted;
    
    
    //
    //  ~  INIT  ~  //
    //
    
    public FailableStudy(boolean isAccepted) {
        super();
        this.isAccepted = isAccepted;
        
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //
    
    public boolean isAccepted() {
        return isAccepted;
    }
}
