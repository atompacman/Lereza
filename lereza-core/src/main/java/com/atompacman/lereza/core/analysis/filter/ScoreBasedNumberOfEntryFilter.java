package com.atompacman.lereza.core.analysis.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import com.atompacman.lereza.core.analysis.MusicalStructure;
import com.atompacman.lereza.core.analysis.filter.ScoreBasedNumberOfEntryFilter.LimitNumberOfEntriesBasedOnScore;
import com.atompacman.lereza.core.analysis.study.PieceStudySet;
import com.atompacman.lereza.core.analysis.study.ScoredStudy;
import com.atompacman.lereza.core.analysis.study.ScoredStudyComparator;
import com.atompacman.lereza.core.analysis.study.StructureTypeStudySet;
import com.atompacman.toolkat.annotations.Implement;

import autovalue.shaded.com.google.common.common.collect.Sets;

public final class ScoreBasedNumberOfEntryFilter 
    implements Filter<LimitNumberOfEntriesBasedOnScore> {

    //
    //  ~  INNER TYPES  ~  //
    //
        
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface LimitNumberOfEntriesBasedOnScore {
    
        int                            max();
        Class<? extends ScoredStudy>[] orderedCriterias();
    }

    
    //
    //  ~  APPLY  ~  //
    //

    @Implement
    public <M extends MusicalStructure> Set<M> apply(
                                            Set<M>                           structures, 
                                            PieceStudySet                    studies, 
                                            LimitNumberOfEntriesBasedOnScore annotationData) {
                                            
        // No structure to discard
        if (structures.size() <= annotationData.max()) {
            return structures;
        }
        
        // Create structure comparator
        @SuppressWarnings("unchecked")
        Class<M> clazz = (Class<M>) structures.iterator().next().getClass();
        StructureTypeStudySet<M> typeSet = studies.getTypeStudySet(clazz);
        Set<Class<? extends ScoredStudy>> criterias = 
                Sets.newHashSet(annotationData.orderedCriterias());
        Comparator<M> comparator = new ScoredStudyComparator<M>(typeSet, criterias);
        
        // Rank structures
        TreeSet<M> rankings = Sets.newTreeSet(comparator);
        structures.stream().forEach(m ->
        {
            rankings.add(m);
        });
        
        // Keep a certain number of them
        Iterator<M> it = rankings.iterator();
        Set<M> kept = new HashSet<>();
        for (int i = 0; i < annotationData.max(); ++i) {
            kept.add(it.next());
        }
        
        return kept;
    }
}
