package com.atompacman.lereza.core.analysis;

import com.atompacman.lereza.core.analysis.analyzer.Analyzer;
import com.atompacman.lereza.core.analysis.extractor.SubstructureExtractor;
import com.atompacman.lereza.core.analysis.filter.Filter;
import com.atompacman.lereza.core.analysis.study.Study;

public enum AnalysisComponentType {
    
    ANALYZER                (Analyzer             .class, false),
    FILTER                  (Filter               .class, false),
    MUSICAL_STRUCTURE       (MusicalStructure     .class, false),
    STUDY                   (Study                .class, false),
    SUBSTRUCTURE_EXTRACTOR  (SubstructureExtractor.class, false);
    
    
    //
    //  ~  FIELDS ~  //
    //
    
    private final Class<?> baseClass;
    private final boolean  areBuiltinOnly;
    
    
    //
    //  ~  INIT  ~  //
    //
    
    private AnalysisComponentType(Class<?> baseClass, boolean areBuiltinOnly) {
        this.baseClass      = baseClass;
        this.areBuiltinOnly = areBuiltinOnly;
    }
    
    
    //
    //  ~  STATE  ~  //
    //
    
    public String basePackageName() {
        return baseClass.getPackage().getName();
    }
    
    public boolean areBuiltinOnly() {
        return areBuiltinOnly;
    }
    
    
    //
    //  ~  IS COMPONENT TYPE OF ~  //
    //
    
    public boolean isTypeOf(Class<?> clazz) {
        return baseClass.isAssignableFrom(clazz);
    }
}