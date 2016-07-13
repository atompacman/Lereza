package com.atompacman.lereza.core.analysis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.UnmodifiableDirectedGraph;

import com.atompacman.lereza.core.analysis.proxy.AnalyzerProxy;
import com.atompacman.lereza.core.analysis.study.Study;
import com.atompacman.toolkat.annotations.SubMethodOf;

@SuppressWarnings("serial")
public final class AnalysisExecutionGraph
    extends UnmodifiableDirectedGraph<AnalyzerProxy, DefaultEdge> {

    //
    //  ~  INIT  ~  //
    //

    AnalysisExecutionGraph(Set<AnalyzerProxy> analyzerProxies) {
        super(build(analyzerProxies));
    }
    
    @SubMethodOf("AnalysisExecutionGraph")
    private static DirectedGraph<AnalyzerProxy, DefaultEdge> 
    build(Set<AnalyzerProxy> analyzerProxies) {
        
        DirectedGraph<AnalyzerProxy, DefaultEdge> graph = 
                new SimpleDirectedGraph<>(DefaultEdge.class);
        
        // Build a study to analyzer map and graph vertex set
        final Map<Class<? extends Study>, AnalyzerProxy> study2Analyzer = new HashMap<>();
        analyzerProxies.forEach(analyzer -> {
            graph.addVertex(analyzer);
            study2Analyzer.put(analyzer.getStudyClass(), analyzer);
        });
        
        // Add an edge per analyzer dependency
        analyzerProxies.forEach(analyzer ->
            analyzer.getStudyDependenciesClasses().forEach(dependency ->
                graph.addEdge(study2Analyzer.get(dependency), analyzer)
            )
        );
        
        return graph;
    }
}
