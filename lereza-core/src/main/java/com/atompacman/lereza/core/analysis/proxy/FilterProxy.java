package com.atompacman.lereza.core.analysis.proxy;

import java.lang.annotation.Annotation;

import com.atompacman.lereza.core.analysis.filter.Filter;
import com.atompacman.lereza.core.analysis.structure.MusicalStructure;

public final class FilterProxy extends StudyDependentAnalysisComponentProxy<Filter<?,?>> {

    //
    //  ~  FIELDS  ~  //
    //
    
    private final Class<? extends Annotation>       annotationClass;
    private final Class<? extends MusicalStructure> structureClass;
    
    
    //
    //  ~  INIT  ~  //
    //
    
    @SuppressWarnings("unchecked")
    public <A extends Annotation, M extends MusicalStructure, F extends Filter<A,M>> 
    FilterProxy(Class<F> filterClass, Class<A> annotationClass, Class<M> structureClass) {
        
        super((Class<Filter<?,?>>) filterClass);
        this.annotationClass = annotationClass;
        this.structureClass  = structureClass;
    }

    
    //
    //  ~  GETTERS  ~  //
    //
    
    public Class<? extends Annotation> getAnnotationClass() {
        return annotationClass;
    }
    
    public Class<? extends MusicalStructure> getStructureClass() {
        return structureClass;
    }
}
