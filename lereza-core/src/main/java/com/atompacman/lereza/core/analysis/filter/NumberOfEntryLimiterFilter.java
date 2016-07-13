package com.atompacman.lereza.core.analysis.filter;

import java.lang.annotation.Annotation;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import com.atompacman.lereza.core.analysis.structure.MusicalStructure;
import com.atompacman.lereza.core.analysis.study.PieceStudySet;
import com.atompacman.lereza.core.analysis.study.StructureTypeStudySet;
import com.atompacman.toolkat.annotations.Implement;

import autovalue.shaded.com.google.common.common.collect.Sets;

public abstract class NumberOfEntryLimiterFilter<A extends Annotation, 
                                                 M extends MusicalStructure> extends Filter<A,M> {

    //
    //  ~  APPLY  ~  //
    //

    @Implement
    public Set<M> apply(Set<M> structures, PieceStudySet studies, A annotationData) {
        // No structure to discard
        int max = getMaxNumberOfEntries(annotationData);
        if (structures.size() <= max) {
            return structures;
        }
        
        // Create structure comparator
        @SuppressWarnings("unchecked")
        Class<M> clazz = (Class<M>) structures.iterator().next().getClass();
        StructureTypeStudySet<M> typeSet = studies.getTypeStudySet(clazz);
        Comparator<M> comparator = createComparator(typeSet, annotationData);
        
        // Rank structures
        TreeSet<M> rankings = Sets.newTreeSet(comparator);
        structures.forEach(MusicalStructure -> rankings.add(MusicalStructure));
        
        // Keep a certain number of them
        Iterator<M> it = rankings.iterator();
        Set<M> kept = new HashSet<>();
        for (int i = 0; i < max; ++i) {
            kept.add(it.next());
        }
        
        return kept;
    }
    
    protected abstract int getMaxNumberOfEntries(A annotationData);
    
    protected abstract Comparator<M> createComparator(StructureTypeStudySet<M> studies, 
                                                      A                        annotationData);
}
