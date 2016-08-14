package com.atompacman.lereza.core.analysis;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.atompacman.lereza.core.analysis.proxy.AnalysisComponentProxy;
import com.atompacman.lereza.core.analysis.proxy.AnalyzerProxy;
import com.atompacman.lereza.core.analysis.proxy.FilterProxy;
import com.atompacman.lereza.core.analysis.proxy.MusicalStructureProxy;
import com.atompacman.lereza.core.analysis.proxy.StudyProxy;
import com.atompacman.lereza.core.analysis.proxy.SubstructureExtractorProxy;
import com.google.common.collect.ImmutableMap;

public enum AnalysisComponentType {
        
    ANALYZER                (AnalyzerProxy             .class),
    FILTER                  (FilterProxy               .class),
    MUSICAL_STRUCTURE       (MusicalStructureProxy     .class),
    STUDY                   (StudyProxy                .class),
    SUBSTRUCTURE_EXTRACTOR  (SubstructureExtractorProxy.class);
    
    
    //
    //  ~  STATIC FIELDS ~  //
    //
    
    static final ImmutableMap<Class<? extends AnalysisComponentProxy<?>>, 
                              AnalysisComponentType> PROXY_CLASS_2_TYPE;
    
    //
    //  ~  FIELDS ~  //
    //
    
    private final Class<? extends AnalysisComponent>         baseClass;
    private final Class<? extends AnalysisComponentProxy<?>> proxyClass;
    
    
    //
    //  ~  INIT  ~  //
    //
    
    static {
        Map<Class<? extends AnalysisComponentProxy<?>>, 
            AnalysisComponentType> map = new HashMap<>();
        for (AnalysisComponentType type : values()) {
            map.put(type.proxyClass, type);
        }
        PROXY_CLASS_2_TYPE = ImmutableMap.copyOf(map);
    }
    
    @SuppressWarnings("unchecked")
    private <C extends AnalysisComponent, P extends AnalysisComponentProxy<C>> 
    AnalysisComponentType(Class<P> proxyClass) {
        
        ParameterizedType superType = (ParameterizedType) proxyClass.getGenericSuperclass();
        Type paramType = superType.getActualTypeArguments()[0];
        if (paramType instanceof ParameterizedType) {
           paramType = ((ParameterizedType) paramType).getRawType();
        }
        
        this.baseClass  = (Class<C>) paramType;
        this.proxyClass = proxyClass;
    }
    
    public static AnalysisComponentType of(Class<? extends AnalysisComponent> componentClass) {
        for (AnalysisComponentType t : values()) {
            if (t.baseClass.isAssignableFrom(componentClass)) {
                return t;
            }
        }
        // TODO
        throw new RuntimeException();
    }

    
    //
    //  ~  GETTERS  ~  //
    //
    
    public Class<? extends AnalysisComponent> getBaseClass() {
        return baseClass;
    }

    public Class<? extends AnalysisComponentProxy<?>> getProxyClass() {
        return proxyClass;
    }
}