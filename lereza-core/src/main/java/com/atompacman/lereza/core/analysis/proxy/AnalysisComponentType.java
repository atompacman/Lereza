package com.atompacman.lereza.core.analysis.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.atompacman.lereza.core.analysis.AnalysisComponent;
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
    //  ~  BUILD PROXY  ~  //
    //
    
    @SuppressWarnings("unchecked")
    public <C extends AnalysisComponent, P extends AnalysisComponentProxy<C>> 
    P buildProxy(Class<C> componentClass) {

        // Extract component class hierarchy
        Stack<Class<?>> classHierarchy = new Stack<>();
        Class<?> clazz = componentClass;
        while (clazz != baseClass) {
            classHierarchy.add(clazz);
            clazz = clazz.getSuperclass();
        }

        // Extract component generic type variable values
        List<Class<?>> proxyCtorArgs  = new ArrayList<>();
        
        while (!classHierarchy.isEmpty()) {
            clazz = classHierarchy.pop();
            
            Type generic = clazz.getGenericSuperclass();
            if (!(generic instanceof ParameterizedType)) {
                continue;
            }
            
            for (Type typeArg : ((ParameterizedType) generic).getActualTypeArguments()) {
                if (typeArg instanceof Class) {
                    if (clazz.getSuperclass() == baseClass) {
                        proxyCtorArgs.add((Class<?>) typeArg);
                    } else {
                        for (int i = 0; i < proxyCtorArgs.size(); ++i) {
                            if (proxyCtorArgs.get(i) == null) {
                                proxyCtorArgs.set(i, (Class<?>) typeArg);
                                break;
                            }
                        }
                    }
                } else {
                    if (clazz.getSuperclass() == baseClass) {
                        proxyCtorArgs.add(null);
                    }
                }
            }
        }
        
        proxyCtorArgs.add(0, componentClass);
        
        Class<?>[] ctorTypes = new Class<?>[proxyCtorArgs.size()];
        Arrays.fill(ctorTypes, Class.class);
        
        Constructor<P> ctor;
        try {
            ctor = (Constructor<P>) proxyClass.getDeclaredConstructor(ctorTypes);
        } catch (NoSuchMethodException | SecurityException e) {
            // TODO
            throw new RuntimeException(e);
        }
        try {
            return ctor.newInstance(proxyCtorArgs.toArray());
        } catch (InstantiationException   | IllegalAccessException | 
                 IllegalArgumentException | InvocationTargetException e) {
            // TODO
            throw new RuntimeException();
        }
    }
}