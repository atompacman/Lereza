package com.atompacman.lereza.core.generator.advisor;

import java.time.Duration;

public class PieceLayerCouncil extends Council<PieceLayerAdvisor> {

    //======================================= METHODS ============================================\\
    
    //-------------------------------------- DETERMINE -------------------------------------------\\

    public Duration determinePieceDuration() {
        return compileAdvices(advisor  -> advisor.advicePieceDuration(), 
                              duration -> duration.toMillis(),
                              value    -> Duration.ofMillis((long) value));
    }

    public double determineTempo() {
        return compileAdvices(advisor -> advisor.adviceTempo());
    }
}
