package com.atompacman.lereza.core.profile.drum;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import com.atompacman.lereza.core.solfege.TimeSignature;

public class DrumBeat {

    //======================================= FIELDS =============================================\\

    private TimeSignature timeSign;
    private int           numBars;

    private Map<PercussionElement, PercussionPattern> patterns;



    //======================================= METHODS ============================================\\

    //---------------------------------- PUBLIC CONSTRUCTOR --------------------------------------\\

    public DrumBeat(TimeSignature timeSign, int numBars) {
        this.timeSign = timeSign;
        this.numBars  = numBars;
        this.patterns = new HashMap<>();
    }

    //- - - - - - - - - - - - - - - - - - - FROM BINARY - - - - - - - - - - - - - - - - - - - - - \\

    public DrumBeat(ByteBuffer buffer) {
        // Create beat from rhythmic signature and number of bars
        this(TimeSignature.valueOf(buffer), buffer.get());
        
        // Read percussion patterns
        int numTracks = buffer.get();
        for (int i = 0; i < numTracks; ++i) {
            addPattern(new PercussionPattern(buffer));
        }
    }
    
    
    //--------------------------------------- SETTERS --------------------------------------------\\

    public void addPattern(PercussionPattern pattern) {
        if (patterns.put(pattern.getPercussionElement(), pattern) != null) {
            throw new IllegalArgumentException("A " + pattern.getPercussionElement() + 
                    " pattern was already added to this pattern.");
        }
    }


    //--------------------------------------- GETTERS --------------------------------------------\\

    public TimeSignature getTimeSignature() {
        return timeSign;
    }

    public int getNumBars() {
        return numBars;
    }

    public int getLengthTU() {
        double numOnDen=(double)timeSign.getMeterNumerator()/(double)timeSign.getMeterDenominator();
        return (int)((double)numBars * numOnDen * 64.0); 
    }
    
    public Map<PercussionElement, PercussionPattern> getPatterns() {
        return patterns;
    }
}
