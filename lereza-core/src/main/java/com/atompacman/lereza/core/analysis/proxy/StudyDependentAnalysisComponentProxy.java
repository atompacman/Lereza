package com.atompacman.lereza.core.analysis.proxy;

import com.atompacman.lereza.core.analysis.AnalysisComponent;
import com.atompacman.lereza.core.analysis.study.Study;
import com.atompacman.toolkat.task.TaskMonitor;
import com.google.common.collect.ImmutableSet;

public abstract class StudyDependentAnalysisComponentProxy<C extends AnalysisComponent> 
    extends AnalysisComponentProxy<C> {
    
    //
    //  ~  INIT  ~  //
    //
    
    protected StudyDependentAnalysisComponentProxy(Class<? extends C> componentClass,
                                                   TaskMonitor        monitor) {
        
        super(componentClass, true, false, monitor);
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //
    
    public ImmutableSet<Class<? extends Study>> getStudyDependencies() {
        return studyDependencies;
    }
}
