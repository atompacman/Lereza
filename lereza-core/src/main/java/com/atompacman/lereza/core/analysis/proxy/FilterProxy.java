package com.atompacman.lereza.core.analysis.proxy;

import java.lang.annotation.Annotation;

import com.atompacman.lereza.core.analysis.filter.Filter;

public final class FilterProxy extends ActiveAnalysisComponentProxy<Filter<?>> {

    //
    //  ~  FIELDS  ~  //
    //
    
    private final Class<? extends Annotation> annotationClass;
    
    
    //
    //  ~  INIT  ~  //
    //
    
    @SuppressWarnings("unchecked")
    <A extends Annotation, F extends Filter<A>> 
    FilterProxy(Class<F> filterClass, Class<A> annotationClass) {
        
        super((Class<Filter<?>>) filterClass);
        this.annotationClass = annotationClass;
    }

    
    //
    //  ~  GETTERS  ~  //
    //
    
    public Class<? extends Annotation> getAnnotationClass() {
        return annotationClass;
    }
}
