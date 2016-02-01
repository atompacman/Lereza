package com.atompacman.lereza.rte;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import com.atompacman.lereza.pluggin.builtin.drum.DrumBeat;
import com.atompacman.lereza.pluggin.builtin.drum.PercussionElement;
import com.atompacman.lereza.pluggin.builtin.drum.PercussionPattern;

public class BeatPlayer implements RealTimeGenerator {

    //======================================= FIELDS =============================================\\

    private DrumBeat beat;



    //======================================= METHODS ============================================\\

    //--------------------------------------- COMPOSE --------------------------------------------\\

    public List<Byte> generate(int tu) {
        List<Byte> notes = new LinkedList<>();
        
        for (Entry<PercussionElement, PercussionPattern> entry : beat.getPatterns().entrySet()) {
            PercussionPattern pattern = entry.getValue();
            if (pattern.hasAHit(tu % pattern.getLengthTU())) {
                notes.add(entry.getKey().getHexNote());
            }
        }
        return notes;
    }
    
    
    //--------------------------------------- SETTERS --------------------------------------------\\

    public void setBeat(DrumBeat beat) {
        this.beat = beat;
    }
}
