package com.atompacman.lereza.core.analysis.proxy;

import java.util.HashSet;
import java.util.Set;

import com.atompacman.lereza.core.analysis.AnalysisComponent;
import com.atompacman.lereza.core.analysis.study.Study;
import com.atompacman.lereza.core.analysis.study.StudyDependencies;
import com.google.common.collect.ImmutableSet;

import autovalue.shaded.com.google.common.common.collect.Sets;

public abstract class ActiveAnalysisComponentProxy<C extends AnalysisComponent> 
    extends AnalysisComponentProxy<C>{

    //
    //  ~  FIELDS  ~  //
    //
    
    private final Set<Class<? extends Study>> dependencies;
    
    
    //
    //  ~  INIT  ~  //
    //
    
    protected ActiveAnalysisComponentProxy(Class<? extends C> componentClass) {
        super(componentClass);
        
        // Extract dependencies
        StudyDependencies annot = componentClass.getAnnotation(StudyDependencies.class);
        this.dependencies = annot == null ? new HashSet<>() : Sets.newHashSet(annot.value());
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //
    
    public ImmutableSet<Class<? extends Study>> getStudyDependenciesClasses() {
        return ImmutableSet.copyOf(dependencies);
    }
}
