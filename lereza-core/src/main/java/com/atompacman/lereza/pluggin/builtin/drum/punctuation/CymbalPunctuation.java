package com.atompacman.lereza.pluggin.builtin.drum.punctuation;

import com.atompacman.lereza.pluggin.builtin.drum.PercussionPattern;

public class CymbalPunctuation {

    //======================================= FIELDS =============================================\\

    private final PercussionPattern pattern;
    private final boolean			replace;



    //======================================= METHODS ============================================\\

    //---------------------------------- PUBLIC CONSTRUCTOR --------------------------------------\\

    public CymbalPunctuation(PercussionPattern pattern, boolean replace) {
        this.pattern = pattern;
        this.replace = replace;
    }


    //--------------------------------------- GETTERS --------------------------------------------\\

    public PercussionPattern getPattern() {
        return pattern;
    }

    public boolean doReplace() {
        return replace;
    }
}
