package com.atompacman.lereza.core.analysis.proxy;

import java.util.Set;

import org.apache.logging.log4j.Level;

import com.atompacman.lereza.core.analysis.AnalysisComponent;
import com.atompacman.toolkat.task.TaskMonitor;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.SetMultimap;

public final class AnalysisComponentProxySet {

    //
    //  ~  FIELDS  ~  //
    //
    
    private final ImmutableSetMultimap<AnalysisComponentType, AnalysisComponentProxy<?>> proxies;
    
    
    //
    //  ~  INIT  ~  //
    //
    
    public AnalysisComponentProxySet(Set<Class<? extends AnalysisComponent>> componentClasses,
                                     TaskMonitor                             ctorMonitor) {
        
        SetMultimap<AnalysisComponentType, AnalysisComponentProxy<?>> map = HashMultimap.create();
        for (Class<? extends AnalysisComponent> componentClass : componentClasses) {
            AnalysisComponentType type = AnalysisComponentType.of(componentClass);
            ctorMonitor.log(Level.DEBUG, "Create a %-22s proxy for component %s", 
                    type.name(), componentClass.getName());
            if (!map.put(type, type.buildProxy(componentClass))) {
                throw new IllegalStateException("Analysis component class \"" + 
                        componentClass.getName() + "\" was already added to this set");
            }
        }
        
        this.proxies = ImmutableSetMultimap.copyOf(map);
    }


    //
    //  ~  GET  ~  //
    //
    
    @SuppressWarnings("unchecked")
    public <P extends AnalysisComponentProxy<?>> ImmutableSet<P> getProxies(Class<P> proxyClass) {
        AnalysisComponentType type = AnalysisComponentType.PROXY_CLASS_2_TYPE.get(proxyClass);
        return (ImmutableSet<P>) proxies.get(type);
    }
}
