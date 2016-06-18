package com.atompacman.lereza.core.analysis.extractor;

import java.util.Set;

import com.atompacman.lereza.core.analysis.Analysis;
import com.atompacman.lereza.core.analysis.AnalyzedStructure;
import com.atompacman.lereza.core.analysis.MusicalStructure;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ExtractionResult<M extends MusicalStructure, 
                                       A extends Analysis, 
                                       B extends Analysis> {

    //
    //  ~  FIELDS  ~  //
    //
    
    public abstract Set<AnalyzedStructure<M, A>> getExtractedSubstructures();
    public abstract B                            getExtractionAnalysis();
    
    
    //
    //  ~  INIT  ~  //
    //
    
    public static <M extends MusicalStructure, A extends Analysis, B extends Analysis> 
    ExtractionResult<M, A, B> of(Set<AnalyzedStructure<M, A>> extractSubstruct, B extractAnalysis) {
        return new AutoValue_ExtractionResult<M, A, B>(extractSubstruct, extractAnalysis);
    }
}
