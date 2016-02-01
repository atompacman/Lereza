package com.atompacman.lereza.pluggin.builtin.drum;

import com.atompacman.lereza.pluggin.builtin.drum.punctuation.Punctuation;

public class DrumPattern {

    //======================================= FIELDS =============================================\\

    private final DrumBeat    beat;
    private final int         numRepet;
    private final Punctuation punctuation;
    private final DrumBeat    fill;



    //======================================= METHODS ============================================\\

    //---------------------------------- PUBLIC CONSTRUCTOR --------------------------------------\\

    public DrumPattern(DrumBeat beat, int numRepet, Punctuation punctuation, DrumBeat fill) {
        this.beat        = beat;
        this.numRepet    = numRepet;
        this.punctuation = punctuation;
        this.fill        = fill;
    }


    //--------------------------------------- GETTERS --------------------------------------------\\

    public DrumBeat getBeat() {
        return beat;
    }

    public int getNbRepet() {
        return numRepet;
    }

    public Punctuation getPunctuation() {
        return punctuation;
    }

    public DrumBeat getFill() {
        return fill;
    }

    public int getNumBars() {
        return numRepet * beat.getNumBars();
    }
}
