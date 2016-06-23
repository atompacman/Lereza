package com.atompacman.lereza.core.analysis;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

final class AnalysisComponentSet {

    //
    //  ~  FIELDS  ~  //
    //
    
    private final Map<AnalysisComponentType, Set<Class<?>>> components;
    
    
    //
    //  ~  INIT  ~  //
    //
    
    AnalysisComponentSet() {
        this.components = new EnumMap<>(AnalysisComponentType.class);
        for (AnalysisComponentType type : AnalysisComponentType.values()) {
            components.put(type, new HashSet<>());
        }
    }
    
    
    //
    //  ~  ADD  ~  //
    //
    
    void addIfValid(Class<?> componentClass) {
        for (AnalysisComponentType type : AnalysisComponentType.values()) {
            if (type.isTypeOf(componentClass)) {
                if (!components.get(type).add(componentClass)) {
                    throw new IllegalStateException("Analysis component class \"" + 
                            componentClass.getName() + "\" was already added to this set");
                }
                return;
            }
        }
    }
}
