package com.atompacman.lereza.core.analysis.filter;

import java.lang.annotation.Annotation;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class AnnotatedFilter<A extends Annotation> {

    //
    //  ~  FIELDS  ~  //
    //

    public abstract Class<? extends Filter<A,?>> getFilterClass();
    public abstract A                            getAnnotation();
    
    
    //
    //  ~  INIT  ~  //
    //

    public static <A extends Annotation> AnnotatedFilter<A> 
    of(Class<? extends Filter<A,?>> filterClass, A annotation) {
        
        return new AutoValue_AnnotatedFilter<A>(filterClass, annotation);
    }
}
