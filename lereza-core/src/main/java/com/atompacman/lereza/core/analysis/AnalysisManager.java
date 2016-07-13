package com.atompacman.lereza.core.analysis;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.logging.log4j.Level;

import com.atompacman.lereza.core.analysis.proxy.AnalysisComponentProxy;
import com.atompacman.lereza.core.analysis.proxy.AnalyzerProxy;
import com.atompacman.lereza.core.analysis.study.Study;
import com.atompacman.lereza.core.piece.Piece;
import com.atompacman.toolkat.annotations.SubMethodOf;
import com.atompacman.toolkat.task.TaskMonitor;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;

public final class AnalysisManager {
    
    //
    //  ~  FIELDS  ~  //
    //

    // Modified in constructor only
    private final SetMultimap<AnalysisComponentType, AnalysisComponentProxy<?>> proxies;
    
    
    //
    //  ~  INIT  ~  //
    //

    // TODO not public
    public AnalysisManager(Set<URL> plugginJarURLs, TaskMonitor monitor) {
        this.proxies = HashMultimap.create();
        
        monitor.executeSubtask("Initialization", mon -> initialize(plugginJarURLs, mon));
    }
    
    @SubMethodOf("AnalysisManager")
    private void initialize(Set<URL> plugginJarURLs, TaskMonitor monitor) {
        monitor.executeSubtaskExcep("Analysis component proxies creation", 
                plugginJarURLs, (mon, urls) -> {
                                
            for (URL url : urls) {
                mon.executeSubtask("Pluggin jar \"" + url.getPath() + "\"", submon -> {
                    loadAnalysisComponentsInJar(url, submon);
                });
                
                final Map<Class<? extends Study>, AnalyzerProxy> study2Analyzer = 
                mon.executeSubtask("Remove study with multiple analyzers", submon -> {
                    return checkThatOneAnalyzerPerStudy(submon);
                });
                
                mon.executeSubtask("Execution graph creation", submon -> {
                    new AnalysisExecutionGraph(Sets.newHashSet(study2Analyzer.values()));
                });
            }
        });
    }
    
    @SubMethodOf("AnalysisManager")
    @SuppressWarnings({ "unchecked" })
    private void loadAnalysisComponentsInJar(URL jarURL, TaskMonitor monitor) {
        // Open a jar file stream and create a class loader for this jar 
        try (JarFile        jar   = new JarFile(jarURL.getPath());
             URLClassLoader urlCL = new URLClassLoader(new URL[]{ jarURL })) {
            
            Enumeration<JarEntry> entries = jar.entries();
            
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                
                // Only keep files
                if (entry.isDirectory()) {
                    continue;
                }
                
                // Only keep class files
                if (!entry.getName().endsWith(".class")) {
                    continue;
                }
                
                // Convert class file path to class name
                String className = entry.getName().substring(0, entry.getName().length() - 6);
                className = className.replace('/', '.');
            
                // Load class
                Class<?> clazz = urlCL.loadClass(className);
                
                // Only keep analysis component subclasses
                if (!AnalysisComponent.class.isAssignableFrom(clazz)) {
                    continue;
                }
                
                // Get analysis component type
                createProxy((Class<? extends AnalysisComponent>) clazz, monitor);
            }
        } catch (ClassNotFoundException e) {
            // TODO
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO
            throw new RuntimeException();
        }
    }

    @SuppressWarnings("unchecked")
    private <C extends AnalysisComponent, P extends AnalysisComponentProxy<C>> 
    void createProxy(Class<C> componentClass, TaskMonitor monitor) {
        
        // Find analysis component type
        AnalysisComponentType type = AnalysisComponentType.of(componentClass);
        
        // Create proxy
        monitor.log(Level.DEBUG, "Creating proxy of type %-22s created for component %s", 
                type.name(), componentClass.getName());
        
        // Extract component class hierarchy
        Stack<Class<?>> classHierarchy = new Stack<>();
        Class<?> clazz = componentClass;
        while (clazz != type.getBaseClass()) {
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
                    if (clazz.getSuperclass() == type.getBaseClass()) {
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
                    if (clazz.getSuperclass() == type.getBaseClass()) {
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
            ctor = (Constructor<P>) type.getProxyClass().getDeclaredConstructor(ctorTypes);
        } catch (NoSuchMethodException | SecurityException e) {
            // TODO
            throw new RuntimeException(e);
        }
        
        AnalysisComponentProxy<C> proxy;
        try {
            proxy = ctor.newInstance(proxyCtorArgs.toArray());
        } catch (InstantiationException   | IllegalAccessException | 
                 IllegalArgumentException | InvocationTargetException e) {
            // TODO
            throw new RuntimeException(e);
        }
        // Add to map
        if (!proxies.put(type, proxy)) {
            // TODO
            throw new IllegalStateException("Analysis component class \"" + 
                    componentClass.getName() + "\" was already added to this set");
        }
    }

    @SubMethodOf("validateAnalysisComponents")
    private Map<Class<? extends Study>, AnalyzerProxy> 
    checkThatOneAnalyzerPerStudy(TaskMonitor monitor) {
        
        // Get loaded analyzers
        Set<AnalyzerProxy> analyzerProxies = getProxies(AnalyzerProxy.class);
        
        // Create a study -> analyzers map
        final HashMultimap<Class<? extends Study>, AnalyzerProxy> study2Analyzers
            = HashMultimap.create();
        
        // Fill map
        for (AnalyzerProxy proxy : analyzerProxies) {
            study2Analyzers.put(proxy.getStudyClass(), proxy);
        }
        
        // Remove extra analyzers and create one to one map
        Map<Class<? extends Study>, AnalyzerProxy> study2Analyzer = new HashMap<>();
        
        for (Class<? extends Study> studyClass : study2Analyzers.keySet()) {
            Iterator<AnalyzerProxy> it = study2Analyzers.get(studyClass).iterator();
            AnalyzerProxy proxy = it.next();
            study2Analyzer.put(studyClass, proxy);

            // No extra analyzers
            if (!it.hasNext()) {
                continue;
            }
            
            // TODO anomaly instead of log
            StringBuilder sb = new StringBuilder();
            sb.append("Multiple analyzer classes found for study class \"");
            sb.append(studyClass.getName());
            sb.append(": ");
            sb.append(it.next().getComponentClass().getName());
            while (it.hasNext()) {
                proxy = it.next();
                sb.append(", ").append(proxy.getComponentClass().getName());
                analyzerProxies.remove(proxy);
            }
            monitor.log(Level.ERROR, sb.toString());
        }
        
        return study2Analyzer;
    }

    @SuppressWarnings("unchecked")
    private <P extends AnalysisComponentProxy<?>> Set<P> getProxies(Class<P> proxyClass) {
        return (Set<P>) proxies.get(AnalysisComponentType.PROXY_CLASS_2_TYPE.get(proxyClass));
    }
    
    
    //
    //  ~  ANALYZE  ~  //
    //

    public void analyze(Piece piece) throws MalformedURLException {

    }
}
