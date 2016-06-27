package com.atompacman.lereza.core.analysis.filter;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

import com.atompacman.lereza.core.analysis.MusicalStructure;
import com.atompacman.lereza.core.analysis.study.PieceStudySet;
import com.atompacman.toolkat.annotations.Implement;

public abstract class PerStructureFilter<A extends Annotation> extends Filter<A> {

    //
    //  ~  APPLY  ~  //
    //
    
    @Implement
    public <M extends MusicalStructure> Set<M> apply(Set<M>        structures, 
                                                     PieceStudySet studies, 
                                                     A             annotationData) {
        return structures.stream()
                         .filter(m -> apply(m, studies, annotationData))
                         .collect(Collectors.toSet());
    }
    
    protected abstract boolean apply(MusicalStructure structure, 
                                     PieceStudySet    studies, 
                                     A                annotationData);
}