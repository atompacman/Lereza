package com.atompacman.lereza.rte;

import java.util.Arrays;
import java.util.List;

import com.atompacman.lereza.core.solfege.Pitch;
import com.atompacman.lereza.core.solfege.Value;
import com.atompacman.toolkat.math.RandGen;

public class MonorhythmicRandomGenerator extends RandomGenerator {

    //======================================= FIELDS =============================================\\

    protected int rhythmValue;
    
    
    
    //======================================= METHODS ============================================\\

    //------------------------------------- CONSTRUCTORS -----------------------------------------\\

    public MonorhythmicRandomGenerator(Value rhythmValue) {
        super();
        setRhythmValue(rhythmValue);
    }
    
    public MonorhythmicRandomGenerator(Pitch min, Pitch max, Value rhythmValue) {
        super(min, max);
        setRhythmValue(rhythmValue);
    }

    
    //--------------------------------------- GENERATE -------------------------------------------\\

    public List<Byte> generate(int tu) {
        if (tu % rhythmValue != 0) {
            return null;
        }
        return Arrays.asList(RandGen.nextByte(pitchInterval.beg(), pitchInterval.end()));
    }


    //--------------------------------------- SETTERS --------------------------------------------\\

    public void setRhythmValue(Value value) {
        this.rhythmValue = value.toTimeunit();
    }
    
    
    //--------------------------------------- GETTERS --------------------------------------------\\

    public Value getRhythmValue() {
        return Value.fromTimeunit(rhythmValue);
    }
}
