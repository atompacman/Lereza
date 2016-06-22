package com.atompacman.lereza.core.analysis.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.atompacman.lereza.core.analysis.MusicalStructure;
import com.atompacman.lereza.core.analysis.filter.SuccededStudyFilter.Succeeded;
import com.atompacman.lereza.core.analysis.study.FailableStudy;
import com.atompacman.lereza.core.analysis.study.PieceStudySet;
import com.atompacman.toolkat.annotations.Implement;

public class SuccededStudyFilter extends PerStructureFilter<Succeeded> {

    //
    //  ~  INNER TYPES  ~  //
    //
    
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Succeeded {

        Class<? extends FailableStudy> value();
    }

    
    //
    //  ~  APPLY  ~  //
    //
    
    @Implement
    public boolean apply(MusicalStructure structure, 
                         PieceStudySet    studies, 
                         Succeeded        annotationData) {
        
        return studies.getStudySet(structure).get(annotationData.value()).isAccepted();
    }
}
