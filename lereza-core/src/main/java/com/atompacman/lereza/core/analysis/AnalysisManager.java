package com.atompacman.lereza.core.analysis;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;

import com.atompacman.lereza.core.analysis.proxy.AnalysisComponentProxySet;
import com.atompacman.lereza.core.piece.Piece;
import com.atompacman.toolkat.Log;
import com.atompacman.toolkat.task.TaskMonitor;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

import autovalue.shaded.com.google.common.common.collect.Sets;

public final class AnalysisManager {
    
    //
    //  ~  FIELDS  ~  //
    //

    private final TaskMonitor monitor;

    private final AnalysisComponentProxySet proxies;
    
    
    //
    //  ~  INIT  ~  //
    //

    private AnalysisManager(Set<URL> plugginJarURLs, TaskMonitor monitor) throws IOException {
        this.monitor = monitor;
        
        this.proxies = monitor.executeSubtaskExcep("Create analysis component proxies", mon -> {
            return createAnalysisComponentProxies(plugginJarURLs, mon);            
        });
    }
    
    private static AnalysisComponentProxySet createAnalysisComponentProxies(
            Set<URL> plugginJarURLs, TaskMonitor monitor) throws IOException {
                    
        ImmutableSet<ClassInfo> classes = 
        monitor.executeSubtaskExcep("Read pluggin jars content", mon -> {
            return readPlugginJarsContent(plugginJarURLs);
        });
        
        Set<Class<? extends AnalysisComponent>> components =
        monitor.executeSubtask("Load analysis component classes", mon -> {
            return loadPlugginAnalysisComponentClasses(plugginJarURLs, classes);
        });
        
        return monitor.executeSubtask("Create component proxies", mon -> {
            return new AnalysisComponentProxySet(components, mon);
        });
    }
    
    private static ImmutableSet<ClassInfo> readPlugginJarsContent(Set<URL> plugginJarURLs) 
                                                                                throws IOException {
        URL[] urls = new URL[plugginJarURLs.size()];
        plugginJarURLs.toArray(urls);
        URLClassLoader urlCL = new URLClassLoader(urls);
        return ClassPath.from(urlCL).getAllClasses();
    }
    
    @SuppressWarnings("unchecked")
    private static Set<Class<? extends AnalysisComponent>> loadPlugginAnalysisComponentClasses(
                                         Set<URL> plugginJarURLs, ImmutableSet<ClassInfo> classes) {
        
        Set<Class<? extends AnalysisComponent>> componentClasses = new HashSet<>();
        for (ClassInfo info : classes) {
            String fileName = info.url().getFile();
            boolean isFileInJars = false;
            for (URL url : plugginJarURLs) {
                if (fileName.contains(url.getPath())) {
                    isFileInJars = true;
                    break;
                }
            }
            if (!isFileInJars) {
                continue;
            }
            Class<?> clazz;
            try {
                clazz = info.load();
            } catch (LinkageError e) {
                // TODO
                throw new IllegalArgumentException(e);
            }
            if (AnalysisComponent.class.isAssignableFrom(clazz)) {
                componentClasses.add((Class<? extends AnalysisComponent>) clazz);
            }
        }
        
        return componentClasses;
    }
    
    
    //
    //  ~  ANALYZE  ~  //
    //

    public void analyze(Piece piece) throws MalformedURLException {

    }

    public static void main(String[] args) throws IOException {
        Log.setIgnoredPackagePart("com.atompacman");
        TaskMonitor monitor = TaskMonitor.of("Lereza");
        monitor.executeSubtaskExcep("Analysis", mon -> {
            URL url = new URL("file:\\B:\\Documents\\Dev\\Atompacman\\Lereza\\Lereza\\lereza-"
                    + "builtin-analysis\\target\\lereza-builtin-analysis-0.0.1-SNAPSHOT.jar");
            return new AnalysisManager(Sets.newHashSet(url), mon);
        });
    }
}
