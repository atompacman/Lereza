package com.atompacman.lereza.core.analysis.proxy;

import com.atompacman.lereza.core.analysis.MusicalStructure;
import com.atompacman.lereza.core.analysis.analyzer.Analyzer;
import com.atompacman.lereza.core.analysis.study.Study;

public final class AnalyzerProxy extends ActiveAnalysisComponentProxy<Analyzer<?, ?>> {

    //
    //  ~  FIELDS  ~  //
    //
    
    private final Class<? extends MusicalStructure> structureClass;
    private final Class<? extends Study>            studyClass;
    
    
    //
    //  ~  INIT  ~  //
    //
    
    <M extends MusicalStructure, S extends Study, A extends Analyzer<M, S>> 
    AnalyzerProxy(Class<A> analyzerClass, Class<M> structureClass, Class<S> studyClass) {

        super(analyzerClass);
        
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
