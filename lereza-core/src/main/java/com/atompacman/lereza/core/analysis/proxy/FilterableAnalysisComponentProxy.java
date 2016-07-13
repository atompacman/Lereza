package com.atompacman.lereza.core.analysis.proxy;

import com.atompacman.lereza.core.analysis.AnalysisComponent;
import com.atompacman.lereza.core.analysis.filter.AnnotatedFilter;
import com.atompacman.lereza.core.analysis.study.Study;
import com.atompacman.toolkat.task.TaskMonitor;
import com.google.common.collect.ImmutableSet;

public abstract class FilterableAnalysisComponentProxy<C extends AnalysisComponent> 
    extends AnalysisComponentProxy<C> {
    
    //
    //  ~  INIT  ~  //
    //
    
    protected FilterableAnalysisComponentProxy(Class<? extends C> componentClass,
                                               TaskMonitor        monitor) {
        
        super(componentClass, true, true, monitor);
    }

    
    //
    //  ~  GETTERS  ~  //
    //
    
    public ImmutableSet<Class<? extends Study>> getStudyDependencies() {
        return studyDependencies;
    }
    
    public ImmutableSet<AnnotatedFilter<?>> getAnnotatedFilters() {
        return annotatedFilters;
    }
}
