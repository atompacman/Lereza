package com.atompacman.lereza.core.analysis.study;

import java.util.Comparator;
import java.util.Set;

import com.atompacman.lereza.core.analysis.MusicalStructure;
import com.google.common.collect.ComparisonChain;

public final class ScoredStudyComparator<M extends MusicalStructure> implements Comparator<M> {

    //
    //  ~  FIELDS  ~  //
    //
    
    private final Set<Class<? extends ScoredStudy>> criterias;
    private final StructureTypeStudySet<M>          studies;
    
    
    //
    //  ~  INIT  ~  //
    //
    
    public ScoredStudyComparator(StructureTypeStudySet<M>          studies,
                                 Set<Class<? extends ScoredStudy>> criterias) {
        
        this.criterias = criterias;
        this.studies   = studies;
    }
    
    
    //
    //  ~  COMPARE  ~  //
    //
    
    @Override
    public int compare(M o1, M o2) {
        ComparisonChain chain = ComparisonChain.start();
        for (Class<? extends ScoredStudy> criteria : criterias) {
            chain = chain.compare(studies.getStudySet(o1).get(criteria).getScore(), 
                                  studies.getStudySet(o1).get(criteria).getScore());
        }
        return chain.result();
    }
}
