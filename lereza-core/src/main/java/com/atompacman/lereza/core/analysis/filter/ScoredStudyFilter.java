package com.atompacman.lereza.core.analysis.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.atompacman.lereza.core.analysis.MusicalStructure;
import com.atompacman.lereza.core.analysis.filter.ScoredStudyFilter.MinimumScore;
import com.atompacman.lereza.core.analysis.study.PieceStudySet;
import com.atompacman.lereza.core.analysis.study.ScoredStudy;
import com.atompacman.toolkat.annotations.Implement;

public final class ScoredStudyFilter extends PerStructureFilter<MinimumScore> {

    //
    //  ~  INNER TYPES  ~  //
    //
        
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MinimumScore {
    
        Class<? extends ScoredStudy> study();
        
        double score();
    }


    
    //
    //  ~  APPLY  ~  //
    //

    @Implement
    public boolean apply(MusicalStructure structure, 
                         PieceStudySet    studies, 
                         MinimumScore     annotationData) {
        
        ScoredStudy study = studies.getStudySet(structure).get(annotationData.study());
        return study.getScore() >= annotationData.score();
    }
}
