package com.atompacman.lereza.kpf.key;

import java.io.File;
import java.util.List;

import com.atompacman.lereza.core.solfege.Semitones;
import com.atompacman.toolkat.math.Interval;

public abstract class HorseRaceKeyChangeDetector<T> extends AbstractKeyChangeDetector<T> {

    //====================================== CONSTANTS ===========================================\\

    private static final double MIN_QUALIF_WEIGHT = 0.5;



    //======================================= FIELDS =============================================\\

    // Temporaries
    private Interval<Integer> qualified;



    //======================================= METHODS ============================================\\

    //--------------------------------- PUBLIC CONSTRUCTORS --------------------------------------\\

    public HorseRaceKeyChangeDetector(File keyConsonanceWindow, int minKeySpan) {
        super(keyConsonanceWindow, minKeySpan);

        // Temporaries
        this.qualified = null;
    }


    //--------------------------------------- DETECT ---------------------------------------------\\

    protected List<KeyChange> detectKeyChanges(T noteStruct) {
        runQualifications();
        return null;
    }

    private void runQualifications() {
        // Find first qualified key
        int first = -1;
        for (int key = 0; key < Semitones.IN_OCTAVE; ++key) {
            if (isQualified(key)) {
                first = key;
                break;
            }
        }

        if (first == -1) {
            // TODO
            throw new RuntimeException("Not implemented");
        }

        // Find last qualified key
        int last = -1;
        for (int key = Semitones.IN_OCTAVE - 1; key >= 0; --key) {
            if (isQualified(key)) {
                last = key;
                break;
            }
        }

        qualified = new Interval<Integer>(first, last);
    }

    private boolean isQualified(int key) {
        int keySpan = 0;

        for (int i = 0; i < kwt.length; ++i) {
            if (kwt[key][i] > MIN_QUALIF_WEIGHT) {
                if (++keySpan == minKeySpan) {
                    return true;
                }
            } else {
                keySpan = 0;
            }
        }
        return false;
    }
}
