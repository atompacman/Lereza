package com.atompacman.lereza.core.analysis.proxy;

import com.atompacman.lereza.core.analysis.MusicalStructure;
import com.atompacman.lereza.core.analysis.extractor.SubstructureExtractor;
import com.atompacman.lereza.core.analysis.study.Study;

public final class SubstructureExtractorProxy extends 
    ActiveAnalysisComponentProxy<SubstructureExtractor<?, ?, ?, ?>> {

    //
    //  ~  FIELDS  ~  //
    //
    
    private final Class<? extends MusicalStructure> parentStructureClass;
    private final Class<? extends MusicalStructure> substructureClass;
    private final Class<? extends Study>            substructureExtractionStudyClass;
    private final Class<? extends Study>            extractionStudyClass;
    
    
    //
    //  ~  INIT  ~  //
    //
    
    <M1 extends MusicalStructure, M2 extends MusicalStructure, S1 extends Study, S2 extends Study> 
    SubstructureExtractorProxy(Class<SubstructureExtractor<M1, M2, S1, S2>> componentClass,
                               Class<M1> parentStructureClass, 
                               Class<M2> substructureClass, 
                               Class<S1> substructureExtractionStudyClass,
                               Class<S2> extractionStudyClass) {
        
        super(componentClass);
        
        this.parentStructureClass             = parentStructureClass;
        this.substructureClass                = substructureClass;
        this.substructureExtractionStudyClass = substructureExtractionStudyClass;
        this.extractionStudyClass             = extractionStudyClass;
    }


    //
    //  ~  GETTERS  ~  //
    //
    
    public Class<? extends MusicalStructure> getParentStructureClass() {
        return parentStructureClass;
    }

    public Class<? extends MusicalStructure> getSubstructureClass() {
        return substructureClass;
    }

    public Class<? extends Study> getSubstructureExtractionStudyClass() {
        return substructureExtractionStudyClass;
    }

    public Class<? extends Study> getExtractionStudyClass() {
        return extractionStudyClass;
    }
}
