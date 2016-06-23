package com.atompacman.lereza.core.analysis.analyzer;

import com.atompacman.lereza.core.analysis.MusicalStructure;
import com.atompacman.lereza.core.analysis.study.DependentStudySet;
import com.atompacman.lereza.core.analysis.study.Study;

public abstract class Analyzer<M extends MusicalStructure, S extends Study> {

    //
    //  ~  ANALYZE  ~  //
    //
    
    public abstract S analyze(M structure, DependentStudySet dependencies);
}
