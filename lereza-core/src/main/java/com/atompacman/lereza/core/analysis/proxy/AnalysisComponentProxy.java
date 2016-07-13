package com.atompacman.lereza.core.analysis.proxy;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Level;

import com.atompacman.lereza.core.analysis.AnalysisComponent;
import com.atompacman.lereza.core.analysis.filter.AnnotatedFilter;
import com.atompacman.lereza.core.analysis.filter.Filter;
import com.atompacman.lereza.core.analysis.study.Study;
import com.atompacman.lereza.core.analysis.study.StudyDependencies;
import com.atompacman.toolkat.task.TaskMonitor;
import com.google.common.collect.ImmutableSet;

public abstract class AnalysisComponentProxy<C extends AnalysisComponent> {

    //
    //  ~  FIELDS  ~  //
    //
    
    protected final Class<? extends C>                   componentClass;
    protected final ImmutableSet<Class<? extends Study>> studyDependencies;
    protected final ImmutableSet<AnnotatedFilter<?>>     annotatedFilters;
    
    
    //
    //  ~  INIT  ~  //
    //

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected AnalysisComponentProxy(Class<? extends C> componentClass,
                                     boolean            areStudyDependenciesLegal,
                                     boolean            areFiltersLegal,
                                     TaskMonitor        monitor) {
        
        this.componentClass = componentClass;
        
        // Scan annotations
        StudyDependencies dependencies  = null;
        Set<AnnotatedFilter<?>> filters = new HashSet<>();;
        for (Annotation annotation : componentClass.getAnnotations()) {
            if (annotation instanceof StudyDependencies) {
                if (areStudyDependenciesLegal) {
                    dependencies = (StudyDependencies) annotation;
                } else {
                    //TODO signal anomaly
                    monitor.log(Level.ERROR, "Study dependencies are not legal");
                }
            } else if (filterClasses.containsKey(annotation.getClass())) {
                if (areFiltersLegal) {
                    filters.add(AnnotatedFilter.of((Class) filterClasses.get(annotation.getClass()), 
                                                   annotation));
                } else {
                  //TODO signal anomaly
                    monitor.log(Level.ERROR, "Filters are not legal");
                }
            } else {
                monitor.log(Level.WARN, "Unknown annotation \"%s\" on analysis component class "
                        + "\"%s\"", annotation.getClass().getSimpleName(), getClass().getName());
            }
        }
        
        this.studyDependencies = dependencies == null ? ImmutableSet.of() : 
                                                        ImmutableSet.copyOf(dependencies.value());
        this.annotatedFilters = ImmutableSet.copyOf(filters);
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //
    
    public Class<? extends C> getComponentClass() {
        return componentClass;
    }
}
