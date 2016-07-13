package com.atompacman.lereza.core.analysis.filter;

import java.lang.annotation.Annotation;
import java.util.Set;

import com.atompacman.lereza.core.analysis.AnalysisComponent;
import com.atompacman.lereza.core.analysis.structure.MusicalStructure;
import com.atompacman.lereza.core.analysis.study.PieceStudySet;

public abstract class Filter<A extends Annotation, 
                             M extends MusicalStructure> implements AnalysisComponent {

    //
    //  ~  APPLY  ~  //
    //

    public abstract Set<M> apply(Set<M> structures, PieceStudySet studies, A annotationData);
}
