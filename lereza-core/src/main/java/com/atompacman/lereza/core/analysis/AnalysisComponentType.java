package com.atompacman.lereza.core.analysis;

import com.atompacman.lereza.core.analysis.analyzer.Analyzer;
import com.atompacman.lereza.core.analysis.extractor.SubstructureExtractor;
import com.atompacman.lereza.core.analysis.filter.Filter;
import com.atompacman.lereza.core.analysis.output.AnalysisResult;

public enum AnalysisComponentType {
    
    ANALYZER                (Analyzer.class),
    ANALYSIS                (Analysis.class),
    ANALYSIS_RESULT         (AnalysisResult.class),
    FILTER                  (Filter.class),
    MUSICAL_STRUCTURE       (MusicalStructure.class),
    SUBSTRUCTURE_EXTRACTOR  (SubstructureExtractor.class);
    
    
    //
    //  ~  FIELDS ~  //
    //
    
    private final Class<?> baseClass;
    
    
    //
    //  ~  INIT  ~  //
    //
    
    private AnalysisComponentType(Class<?> baseClass) {
        this.baseClass = baseClass;
    }
    
    
    //
    //  ~  STATE  ~  //
    //
    
    public String basePackageName() {
        return baseClass.getPackage().getName();
    }
    
    
    //
    //  ~  IS COMPONENT TYPE OF ~  //
    //
    
    public boolean isComponentTypeOf(Class<?> clazz) {
        return baseClass.isAssignableFrom(clazz);
    }
}