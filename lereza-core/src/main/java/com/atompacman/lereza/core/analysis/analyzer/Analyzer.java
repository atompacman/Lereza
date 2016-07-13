package com.atompacman.lereza.core.analysis.analyzer;

import com.atompacman.lereza.core.analysis.AnalysisComponent;
import com.atompacman.lereza.core.analysis.structure.MusicalStructure;
import com.atompacman.lereza.core.analysis.study.DependentStudySet;
import com.atompacman.lereza.core.analysis.study.Study;

public abstract class Analyzer<M extends MusicalStructure, S extends Study> 
    implements AnalysisComponent {

    //
    //  ~  ANALYZE  ~  //
    //
    
    public abstract S analyze(M structure, DependentStudySet dependencies);
}
