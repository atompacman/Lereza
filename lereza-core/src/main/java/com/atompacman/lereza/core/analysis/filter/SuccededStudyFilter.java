package com.atompacman.lereza.core.analysis.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.atompacman.lereza.core.analysis.filter.SuccededStudyFilter.Succeeded;
import com.atompacman.lereza.core.analysis.structure.MusicalStructure;
import com.atompacman.lereza.core.analysis.study.FailableStudy;
import com.atompacman.lereza.core.analysis.study.StructureStudySet;
import com.atompacman.toolkat.annotations.Implement;

public final class SuccededStudyFilter extends PerStructureGenericFilter<Succeeded> {

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
    public boolean apply(MusicalStructure  structure, 
                         StructureStudySet studies, 
                         Succeeded         annotationData) {
        
        return studies.get(annotationData.value()).isAccepted();
    }
}
