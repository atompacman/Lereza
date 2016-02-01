package com.atompacman.lereza.rte;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.atompacman.lereza.core.solfege.Pitch;
import com.atompacman.lereza.core.solfege.Scale;
import com.atompacman.lereza.core.solfege.Value;
import com.atompacman.toolkat.math.RandGen;

public class MonorhythmicScaleRandomGenerated extends MonorhythmicRandomGenerator {

    //======================================= FIELDS =============================================\\

    public List<Pitch> possiblePitches;

    
    
    //------------------------------------- CONSTRUCTORS -----------------------------------------\\

    public MonorhythmicScaleRandomGenerated(Pitch min, Pitch max, Value rhythmValue, Scale scale) {
        super(min, max, rhythmValue);
        setScale(scale);
    }

    
    //--------------------------------------- GENERATE -------------------------------------------\\

    public List<Byte> generate(int tu) {
        if (tu % rhythmValue != 0) {
            return null;
        }
        
        int index = RandGen.nextInt(0, possiblePitches.size() - 1);
        
        return Arrays.asList((byte) possiblePitches.get(index).semitoneValue());
    }

    
    //--------------------------------------- SETTERS --------------------------------------------\\

    public void setScale(Scale scale) {
        possiblePitches = new ArrayList<>();
        
        for (byte i = pitchInterval.beg(); i <= pitchInterval.end(); ++i) {
            Pitch pitch = Pitch.thatIsMoreCommonForHexValue(i);
            if (scale.contains(pitch.getTone())) {
                possiblePitches.add(pitch);
            }
        }
    }
    
    
    //--------------------------------------- GETTERS --------------------------------------------\\

    public List<Pitch> getPossiblePitches() {
        return possiblePitches;
    }
}
