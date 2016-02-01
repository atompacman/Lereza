package com.atompacman.lereza.pluggin.builtin.drum.punctuation;

import com.atompacman.toolkat.math.Interval;

public class SilencePunctuation implements Punctuation {

    //======================================= FIELDS =============================================\\

    private final Interval<Integer> intervalTU;



    //======================================= METHODS ============================================\\

    //---------------------------------- PUBLIC CONSTRUCTOR --------------------------------------\\

    public SilencePunctuation(int begTU, int endTU) {
        super();
        this.intervalTU = new Interval<Integer>(begTU, endTU);
    }


    //--------------------------------------- GETTERS --------------------------------------------\\

    public Interval<Integer> getIntervalTU() {
        return intervalTU;
    }
}
