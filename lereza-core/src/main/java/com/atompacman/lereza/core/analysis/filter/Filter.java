package com.atompacman.lereza.core.analysis.filter;

import java.util.Set;

import com.atompacman.lereza.core.analysis.MusicalStructure;
import com.atompacman.lereza.core.analysis.study.PieceStudySet;

public interface Filter<A> {

    //
    //  ~  APPLY  ~  //
    //

    <M extends MusicalStructure> Set<M> apply(Set<M>        structures, 
                                              PieceStudySet studies, 
                                              A             annotationData);
}
