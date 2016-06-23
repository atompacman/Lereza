package com.atompacman.lereza.core.analysis;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;

import com.atompacman.lereza.core.piece.Piece;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

public final class AnalysisManager {

    //
    // ~ FIELDS ~ //
    //

    //private final AnalysisComponentSet components;

    
    //
    // ~ INIT ~ //
    //

    private AnalysisManager() throws IOException {
        /*this.components = */loadAnalysisComponents(new HashSet<>());
    }

    private static AnalysisComponentSet loadAnalysisComponents(Set<URL> plugginJarURLs) 
                                                                                throws IOException {
        ClassLoader currCL = Thread.currentThread().getContextClassLoader();
        URL[] urls = new URL[plugginJarURLs.size()];
        plugginJarURLs.toArray(urls);
        URLClassLoader urlCL = new URLClassLoader(urls, currCL);
        ImmutableSet<ClassInfo> classes = ClassPath.from(urlCL).getAllClasses();
        AnalysisComponentSet components = new AnalysisComponentSet();
        classes.stream().forEach(c -> components.addIfValid(c.load()));
        return components;
    }


    //
    // ~ ANALYZE ~ //
    //

    public void analyze(Piece piece) throws MalformedURLException {

    }
}
