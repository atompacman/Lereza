package com.atompacman.lereza.core.analysis.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Comparator;

import com.atompacman.lereza.core.analysis.filter.ScoreBasedNumberOfEntryFilter.LimitNumberOfEntriesBasedOnScore;
import com.atompacman.lereza.core.analysis.structure.MusicalStructure;
import com.atompacman.lereza.core.analysis.study.ScoredStudy;
import com.atompacman.lereza.core.analysis.study.ScoredStudyComparator;
import com.atompacman.lereza.core.analysis.study.StructureTypeStudySet;

import autovalue.shaded.com.google.common.common.collect.Sets;

public final class ScoreBasedNumberOfEntryFilter 
    extends NumberOfEntryLimiterFilter<LimitNumberOfEntriesBasedOnScore, MusicalStructure> {

    //
    //  ~  INNER TYPES  ~  //
    //
    
    @Target    (ElementType.TYPE)
    @Retention (RetentionPolicy.RUNTIME)
    public @interface LimitNumberOfEntriesBasedOnScore {
    
        int                            max();
        Class<? extends ScoredStudy>[] orderedCriterias();
    }

    
    //
    //  ~  APPLY  ~  //
    //

    @Override
    protected int getMaxNumberOfEntries(LimitNumberOfEntriesBasedOnScore annotationData) {
        return annotationData.max();
    }

    @Override
    protected Comparator<MusicalStructure> createComparator(
                                           StructureTypeStudySet<MusicalStructure> studies,
                                           LimitNumberOfEntriesBasedOnScore        annotationData) {
        
        return new ScoredStudyComparator<>(studies, 
                                           Sets.newHashSet(annotationData.orderedCriterias()));
    }
}
