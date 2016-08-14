package com.atompacman.lereza.core.analysis;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JApplet;
import javax.swing.JFrame;

import org.jgraph.JGraph;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.DefaultEdge;

import com.atompacman.lereza.core.analysis.proxy.AnalyzerProxy;
import com.google.common.collect.Sets;

@SuppressWarnings("serial")
/**
 * Based on org.jgrapht.demo.JGraphAdapterDemo
 */
public final class AnalysisExecutionGraphViewer extends JApplet {
    
    //
    //  ~  CONSTANTS  ~  //
    //

    private static final Dimension DEFAULT_WIN_SIZE = new Dimension(530, 320);


    //
    //  ~  MAIN  ~  //
    //

    public static void main(String args[]) {
        AnalysisExecutionGraphViewer aegv = AnalysisExecutionGraphViewer.of();
        aegv.setGraph(new AnalysisExecutionGraph(Sets.newHashSet(BirthRateAnalyzer.class, 
                                                                 DemographicAnalyzer.class)));
    }
    
    
    //
    //  ~  INIT  ~  //
    //

    public static AnalysisExecutionGraphViewer of() {
        return new AnalysisExecutionGraphViewer();
    }
    
    private AnalysisExecutionGraphViewer() {
        JFrame frame = new JFrame();
        frame.getContentPane().add(this);
        frame.setTitle("Analysis execution graph viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    
    public void setGraph(AnalysisExecutionGraph graph) {
        // Create a visualization using JGraph, via an adapter
        JGraphModelAdapter<AnalyzerProxy, DefaultEdge> adapter = new JGraphModelAdapter<>(graph);
        JGraph jgraph = new JGraph(adapter);
        
        // Adjust display settings
        jgraph.setPreferredSize(DEFAULT_WIN_SIZE);
        jgraph.setBackground(Color.LIGHT_GRAY);
        
        // Add graph to window
        getContentPane().add(jgraph);
        resize(DEFAULT_WIN_SIZE);
    }
}
