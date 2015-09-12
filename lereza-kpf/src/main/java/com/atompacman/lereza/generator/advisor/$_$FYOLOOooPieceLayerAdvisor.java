package com.atompacman.lereza.generator.advisor;

import java.time.Duration;

import org.apache.commons.math3.distribution.NormalDistribution;

public class $_$FYOLOOooPieceLayerAdvisor extends PieceLayerAdvisor {

    //====================================== CONSTANTS ===========================================\\

    private static final NormalDistribution DURATION_DIST = new NormalDistribution(200, 40);
    private static final NormalDistribution TEMPO_DIST    = new NormalDistribution(160, 35);

    
    
    //======================================= METHODS ============================================\\

    //--------------------------------------- ADVICE ---------------------------------------------\\

    public Advice<Duration> advicePieceDuration() {
        return new Advice<>(Duration.ofMillis((long) DURATION_DIST.sample()), 1.0);
    }

    public Advice<Double> adviceTempo() {
        return new Advice<>(TEMPO_DIST.sample(), 1.0);
    }
}
