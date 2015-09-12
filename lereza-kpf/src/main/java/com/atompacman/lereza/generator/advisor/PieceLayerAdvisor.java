package com.atompacman.lereza.generator.advisor;

import java.time.Duration;

public abstract class PieceLayerAdvisor extends Advisor {

    //=================================== ABSTRACT METHODS =======================================\\

    //--------------------------------------- ADVICE ---------------------------------------------\\

    public abstract Advice<Duration> advicePieceDuration();
    
    public abstract Advice<Double> adviceTempo();
}
