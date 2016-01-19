package com.atompacman.lereza.core.analysis;

import org.apache.logging.log4j.Level;

import com.atompacman.toolkat.module.Module;

public abstract class Analyzer extends Module {

    //======================================= METHODS ============================================\\

    //------------------------------------- CONSTRUCTORS -----------------------------------------\\

    public Analyzer(AnalysisManager manager) {
        super(Level.INFO, manager);
    }
    
    public Analyzer(Level verbose, AnalysisManager manager) {
        super(verbose, manager);
    }
}
