package com.atompacman.lereza.core.analysis.filter;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

import com.atompacman.lereza.core.analysis.structure.MusicalStructure;
import com.atompacman.lereza.core.analysis.study.PieceStudySet;
import com.atompacman.lereza.core.analysis.study.StructureStudySet;
import com.atompacman.toolkat.annotations.Implement;

public abstract class PerStructureFilter<A extends Annotation, M extends MusicalStructure>
    extends Filter<A, M> {

    //
    //  ~  APPLY  ~  //
    //
    
    @Implement
    public Set<M> apply(Set<M> structures, PieceStudySet studies, A annotationData) {
        return structures.stream()
                         .filter(m -> apply(m, studies.getStudySet(m), annotationData))
                         .collect(Collectors.toSet());
    }
    
    protected abstract boolean apply(M                 structure, 
                                     StructureStudySet studies, 
                                     A                 annotationData);
}