package com.atompacman.lereza.core.analysis.proxy;

import com.atompacman.lereza.core.analysis.analyzer.Analyzer;
import com.atompacman.lereza.core.analysis.structure.MusicalStructure;
import com.atompacman.lereza.core.analysis.study.Study;
import com.atompacman.toolkat.task.TaskMonitor;

public final class AnalyzerProxy extends FilterableAnalysisComponentProxy<Analyzer<?,?>> {

    //
    //  ~  FIELDS  ~  //
    //
    
    private final Class<? extends MusicalStructure> structureClass;
    private final Class<? extends Study>            studyClass;
    
    
    //
    //  ~  INIT  ~  //
    //
    
    public <M extends MusicalStructure, S extends Study, A extends Analyzer<M, S>> 
    AnalyzerProxy(Class<A>    analyzerClass, 
                  Class<M>    structureClass, 
                  Class<S>    studyClass, 
                  TaskMonitor monitor) {

        super(analyzerClass, monitor);
        
        this.structureClass = structureClass;
        this.studyClass     = studyClass;
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //
    
    public Class<? extends MusicalStructure> getStructureClass() {
        return structureClass;
    }

    public Class<? extends Study> getStudyClass() {
        return studyClass;
    }
}
