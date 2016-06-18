package com.atompacman.lereza.core.analysis;

import com.atompacman.lereza.core.analysis.Analysis;
import com.atompacman.lereza.core.analysis.MusicalStructure;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class AnalyzedStructure<M extends MusicalStructure, A extends Analysis> {

    //
    //  ~  FIELDS  ~  //
    //
    
    public abstract M getStructure();
    public abstract A getAnalysis();
    
    
    //
    //  ~  INIT  ~  //
    //
    
    public static <M extends MusicalStructure, A extends Analysis> 
    AnalyzedStructure<M, A> of(M structure, A analysis) {
        return new AutoValue_AnalyzedStructure<M, A>(structure, analysis);
    }
}
