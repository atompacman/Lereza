package com.atompacman.lereza.core.analysis.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.atompacman.lereza.core.analysis.filter.ScoredStudyFilter.MinimumScore;
import com.atompacman.lereza.core.analysis.structure.MusicalStructure;
import com.atompacman.lereza.core.analysis.study.ScoredStudy;
import com.atompacman.lereza.core.analysis.study.StructureStudySet;
import com.atompacman.toolkat.annotations.Implement;

public final class ScoredStudyFilter extends PerStructureGenericFilter<MinimumScore> {

    //
    //  ~  INNER TYPES  ~  //
    //
        
    @Target    (ElementType.TYPE)
    @Retention (RetentionPolicy.RUNTIME)
    public @interface MinimumScore {
    
        Class<? extends ScoredStudy> study();
        
        double score();
    }


    
    //
    //  ~  APPLY  ~  //
    //

    @Implement
    public boolean apply(MusicalStructure  structure, 
                         StructureStudySet studies, 
                         MinimumScore      annotationData) {
        
        return studies.get(annotationData.study()).getScore() >= annotationData.score();
    }
}
