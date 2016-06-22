package com.atompacman.lereza.core.analysis.filter;

import java.util.HashSet;
import java.util.Set;

import com.atompacman.lereza.core.analysis.MusicalStructure;
import com.atompacman.lereza.core.analysis.study.PieceStudySet;
import com.atompacman.toolkat.annotations.Implement;

public abstract class PerStructureFilter<A> implements Filter<A> {

    //
    //  ~  APPLY  ~  //
    //
    
    @Implement
    public <M extends MusicalStructure> Set<M> apply(Set<M>        structures, 
                                                     PieceStudySet studies, 
                                                     A             annotationData) {
        
        Set<M> set = new HashSet<>();
        structures.stream().forEach(m ->
        {
            if (apply(m, studies, annotationData)) {
                set.add(m);
            }
        });
        return set;
    }
    
    protected abstract boolean apply(MusicalStructure structure, 
                                     PieceStudySet    studies, 
                                     A                annotationData);
}