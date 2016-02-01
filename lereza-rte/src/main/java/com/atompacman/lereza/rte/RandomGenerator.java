package com.atompacman.lereza.rte;

import com.atompacman.lereza.core.solfege.Pitch;
import com.atompacman.toolkat.math.Interval;

public abstract class RandomGenerator implements RealTimeGenerator {

    //======================================= FIELDS =============================================\\

    protected Interval<Byte> pitchInterval;
    
    
    
    //======================================= METHODS ============================================\\

    //------------------------------------- CONSTRUCTORS -----------------------------------------\\

    public RandomGenerator() {
        this.pitchInterval = new Interval<>((byte) 0, Byte.MAX_VALUE);
    }
    
    public RandomGenerator(Pitch min, Pitch max) {
        setPitchInterval(min, max);
    }


    //--------------------------------------- SETTERS --------------------------------------------\\

    public void setPitchInterval(Pitch min, Pitch max) {
        byte minVal = (byte) min.semitoneValue();
        byte maxVal = (byte) max.semitoneValue();
        
        if (minVal > maxVal || minVal == maxVal) {
            throw new IllegalArgumentException("Invalid pitch interval");
        }
        
        this.pitchInterval = new Interval<>(minVal, maxVal);
    }
    
    
    //--------------------------------------- GETTERS --------------------------------------------\\

    public Interval<Byte> getPitchInterval() {
        return pitchInterval;
    }
}
