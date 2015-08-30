package com.atompacman.lereza.core.profile.drum;

import java.util.LinkedHashMap;
import java.util.Map;

public class DrumTrack {

    //======================================= FIELDS =============================================\\

    private Map<Integer, DrumPattern> patterns;



    //======================================= METHODS ============================================\\

    //---------------------------------- PUBLIC CONSTRUCTOR --------------------------------------\\

    public DrumTrack(int firstBar) {
        this.patterns = new LinkedHashMap<>();
    }


    //--------------------------------------- SETTERS --------------------------------------------\\

    public void addPattern(int firstBar, DrumPattern pattern) {
        this.patterns.put(firstBar, pattern);
    }


    //--------------------------------------- GETTERS --------------------------------------------\\

    public Map<Integer, DrumPattern> getPatterns() {
        return patterns;
    }
}
