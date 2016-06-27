package com.atompacman.lereza.core.analysis.proxy;

import com.atompacman.lereza.core.analysis.AnalysisComponent;

public abstract class AnalysisComponentProxy<C extends AnalysisComponent> {

    //
    //  ~  FIELDS  ~  //
    //
    
    protected final Class<? extends C> componentClass;
    
    
    //
    //  ~  INIT  ~  //
    //

    protected AnalysisComponentProxy(Class<? extends C> componentClass) {
        this.componentClass = componentClass;
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //
    
    public Class<? extends C> getComponentClass() {
        return componentClass;
    }
}
