package com.atompacman.lereza.core.analysis;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayDeque;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import org.codehaus.plexus.util.dag.CycleDetectedException;
import org.codehaus.plexus.util.dag.DAG;

import com.atompacman.lereza.core.analysis.profile.PieceStructuralScope;
import com.atompacman.lereza.core.analysis.profile.Profile;
import com.atompacman.lereza.core.piece.PieceComponent;
import com.atompacman.toolkat.exception.Throw;
import com.atompacman.toolkat.exception.UnimplementedException;
import com.atompacman.toolkat.misc.AnnotationUtils;
import com.atompacman.toolkat.module.AnomalyDescription;
import com.atompacman.toolkat.module.AnomalyDescription.Severity;
import com.atompacman.toolkat.module.Module;
import com.google.common.base.Optional;

public class AnalysisManager extends Module {

    //===================================== INNER TYPES ==========================================\\

    private static class ScopedProfileClass {
        public Class<? extends Profile> clazz = null;
        public PieceStructuralScope     scope = null;

        public String toString() { return clazz.toString() + ": " + scope.toString(); }
    }
    
    private static class AnalysisMethodDesc {
        public ScopedProfileClass       scopedClass  = new ScopedProfileClass();
        public Method                   producer     = null;
        public List<ScopedProfileClass> dependencies = new LinkedList<>();
    }

    
    
    //===================================== INNER TYPES ==========================================\\

    private enum Anomaly {
        
        @AnomalyDescription (
                name          = "Analyzer class not extending Analyzer",
                description   = "Analyzer classes must extend the Analyzer abstract class",
                severity      = Severity.MODERATE,
                consequences  = "Analyzer will be discarded, so analysis may be incomplete")
        NON_ANALYZER_CLASS,
        
        @AnomalyDescription (
                name          = "Analyzer class with no analysis methods",
                description   = "Analyzer class has no method annotated as AnalysisMethod",
                severity      = Severity.MODERATE,
                consequences  = "Analyzer will be discarded, so analysis may be incomplete")
        ANALYZER_CLASS_WITH_NO_ANALYSIS_METHODS,
        
        @AnomalyDescription (
                name          = "Unaccessible analysis method",
                detailsFormat = "%s",
                description   = "Analysis method is not accessible from the library (not public?)",
                severity      = Severity.MODERATE,
                consequences  = "Analysis method will be discarded, so analysis may be incomplete")
        UNACCESSIBLE_ANALYSIS_METHOD,
        
        @AnomalyDescription (
                name          = "Analysis method does not return a Profile class",
                detailsFormat = "Return type is \"%s\"",
                severity      = Severity.MODERATE,
                consequences  = "Analysis method will be discarded, so analysis may be incomplete")
        ANALYSIS_METHOD_DOES_NOT_RETURN_A_PROFILE,
        
        @AnomalyDescription (
                name          = "Analysis method does not take any parameter",
                severity      = Severity.MODERATE,
                consequences  = "Analysis method will be discarded, so analysis may be incomplete")
        PARAMETERLESS_ANALYSIS_METHOD,
        
        @AnomalyDescription (
                name          = "Multiple piece component parameters",
                severity      = Severity.MODERATE,
                consequences  = "Analysis method will be discarded, so analysis may be incomplete")
        MULTIPLE_PIECE_COMPONENT_PARAMETERS,
        
        @AnomalyDescription (
                name          = "Invalid analysis method parameter class",
                severity      = Severity.MODERATE,
                consequences  = "Analysis method will be discarded, so analysis may be incomplete")
        INVALID_ANALYSIS_METHOD_PARAMETER_CLASS,
        
        @AnomalyDescription (
                name          = "Multiple analysis methods returning the same profile class",
                severity      = Severity.MODERATE,
                consequences  = "Analysis method will be discarded, so analysis may be incomplete")
        MULTIPLE_ANALYSIS_METHOD_RETURNING_SAME_PROFILE;
    }
    
    
    
    //======================================= METHODS ============================================\\

    public Queue<Method> createDependencyAwareMethodQueue(Set<Class<?>> analyzerClasses) {
        Map<String, AnalysisMethodDesc> methods = extractAnalysisMethods(analyzerClasses);
        
        DAG analyzerMethodGraph = new DAG();
        
        for (AnalysisMethodDesc method : methods.values()) {
            analyzerMethodGraph.addVertex(method.scopedClass.toString());
        }
        
        for (Entry<String, AnalysisMethodDesc> entry : methods.entrySet()) {
            for (ScopedProfileClass dependancy : entry.getValue().dependencies) {
                AnalysisMethodDesc dependencyMethod = methods.get(dependancy.toString());
                
                if (dependencyMethod == null) {
                    Throw.aRuntime(UnimplementedException.class, "Yo");
                }
                
                try {
                    analyzerMethodGraph.addEdge(dependancy.toString(), entry.getKey());
                } catch (CycleDetectedException e) {
                    Throw.aRuntime(UnimplementedException.class, "Yo", e);
                }
            }
        }
        
        Set<Method> methodSet = new LinkedHashSet<>();
        for (String methodName : methods.keySet()) {
            for (String successor : analyzerMethodGraph.getSuccessorLabels(methodName)) {
                methodSet.add(methods.get(successor).producer);
            }
        }
        
        return new ArrayDeque<>(methodSet);
    }
    
    private Map<String, AnalysisMethodDesc> extractAnalysisMethods(Set<Class<?>> analyzerClasses) {
        Map<String, AnalysisMethodDesc> methods = new LinkedHashMap<>();
        
        for (Class<?> analyzerClass : analyzerClasses) {
            if (!Analyzer.class.isAssignableFrom(analyzerClass)) {
                signal(Anomaly.NON_ANALYZER_CLASS);
                continue;
            }
            boolean hasAnAnalysisMethod = false;
            
            for (Method method : analyzerClass.getDeclaredMethods()) {
                Optional<AnalysisMethodDesc> desc = extractAnalysisMethodInfo(method);
                
                if (!desc.isPresent()) {
                    continue;
                }
                
                if (methods.put(desc.get().scopedClass.toString(), desc.get()) != null) {
                    signal(Anomaly.MULTIPLE_ANALYSIS_METHOD_RETURNING_SAME_PROFILE);
                    continue;
                }
                
                hasAnAnalysisMethod = true;
            }
            
            if (!hasAnAnalysisMethod) {
                signal(Anomaly.ANALYZER_CLASS_WITH_NO_ANALYSIS_METHODS);
            }
        }
        
        return methods;
    }
    
    @SuppressWarnings("unchecked")
    private Optional<AnalysisMethodDesc> extractAnalysisMethodInfo(Method method) {
        // Check that method is tagged as an analysis method
        if (!AnnotationUtils.hasAnnotation(method, AnalysisMethod.class)) {
            return Optional.absent();
        }
        
        // Check that it is accessible
        int mod = method.getModifiers();
        if (!Modifier.isPublic(mod) || Modifier.isAbstract(mod) || 
             Modifier.isNative(mod) || Modifier.isStatic(mod)) {
            signal(Anomaly.UNACCESSIBLE_ANALYSIS_METHOD);
            return Optional.absent();
        }
        
        Class<?>[] paramTypes = method.getParameterTypes();
        
        // Check that it has at least one parameter
        if (paramTypes.length == 0) {
            signal(Anomaly.PARAMETERLESS_ANALYSIS_METHOD);
            return Optional.absent();
        }
        
        // Create descriptor
        AnalysisMethodDesc desc = new AnalysisMethodDesc();
        desc.producer = method;
        
        // Find the PieceComponent parameter
        for (Class<?> paramClass : paramTypes) {
            if (PieceComponent.class.isAssignableFrom(paramClass)) {
                if (desc.scopedClass.scope != null) {
                    signal(Anomaly.MULTIPLE_PIECE_COMPONENT_PARAMETERS);
                    return Optional.absent();
                }
                desc.scopedClass.scope = PieceStructuralScope.of(
                        (Class<? extends PieceComponent>) paramClass);
            }
        }
        
        // Find Profile parameters
        for (Class<?> paramClass : paramTypes) {
            // Skip the PieceComponent parameter
            if (PieceComponent.class.isAssignableFrom(paramClass)) {
                continue;
            }
            // Check that parameters extend Profile
            if (!Profile.class.isAssignableFrom(paramClass)) {
                signal(Anomaly.INVALID_ANALYSIS_METHOD_PARAMETER_CLASS);
                return Optional.absent();
            }
            // Register dependency
            ScopedProfileClass dependency = new ScopedProfileClass();
            dependency.clazz = (Class<? extends Profile>) paramClass;
            dependency.scope = desc.scopedClass.scope;
            desc.dependencies.add(dependency);
        }
        
        Class<?> returnType = method.getReturnType();

        // Check that return type is valid
        if (!Profile.class.isAssignableFrom(returnType)) {
            signal(Anomaly.ANALYSIS_METHOD_DOES_NOT_RETURN_A_PROFILE);
            return Optional.absent();
        }
        
        desc.scopedClass.clazz = (Class<? extends Profile>) returnType;
        
        return Optional.of(desc);
    }
}
