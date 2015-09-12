package com.atompacman.lereza.kpf.key;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.stat.StatUtils;

import com.atompacman.lereza.core.solfege.Semitones;
import com.atompacman.toolkat.math.Interval;

public abstract class AbstractKeyChangeAnalyzer<T> {

    //===================================== INNER TYPES ==========================================\\

    private static final int    HEX_NOTE_TO_SIMPLE_KEY[] = { 4, 11, 6, 1, 8, 3, 10, 5, 0, 7, 2, 9 };
    private static final double MIN_QUALIF_WEIGHT        = 0.5;

    
    
    //======================================= FIELDS =============================================\\

    // :::: Parameters ::::
    // Key consonance window
    private final double[] kcw;
    private final int      minKeyChangeGap;
    private final double   keyChangeSensibility;

    // :::: Temporaries ::::
    // Key weight timeline
    private double[][]        kwt;
    private int               seqLenTU;
    private Interval<Integer> qualified;

    
    
    //=================================== ABSTRACT METHODS =======================================\\

    protected abstract int timeunitLengthOf(T noteStruct);

    protected abstract void addNotes(T noteStruct);
    

    
    //======================================= METHODS ============================================\\

    //--------------------------------- PUBLIC CONSTRUCTORS --------------------------------------\\

    public AbstractKeyChangeAnalyzer(File   keyConsonanceWindow, 
                                     int    minKeyChangeGap, 
                                     double keyChangeSensibility) {
        
        // Parameters
        this.kcw                  = readKeyConsonanceWindowFile(keyConsonanceWindow);
        this.minKeyChangeGap      = minKeyChangeGap;
        this.keyChangeSensibility = keyChangeSensibility;

        // Temporaries
        this.kwt       = null;
        this.seqLenTU  = 0;
        this.qualified = null;
    }
    
    @SuppressWarnings("unchecked")
    private static double[] readKeyConsonanceWindowFile(File kcwFile) {
        try {
            List<String> lines = FileUtils.readLines(kcwFile);
            if (lines.size() != Semitones.IN_OCTAVE) {
                throw new IllegalArgumentException("Invalid number of keys (" + lines.size() + ")");
            }
            double[] kcw = new double[Semitones.IN_OCTAVE];

            for (int i = 0; i < lines.size(); ++i) {
                try {
                    kcw[i] = Double.parseDouble(lines.get(i));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Values should be floating points");
                }
            }
            return kcw;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error reading key consonance window file", e);
        }
    }

    
    //--------------------------------------- DETECT ---------------------------------------------\\

    public List<KeyChange> detect(T noteStruct) {
        // Initialize key weight timeline
        seqLenTU = timeunitLengthOf(noteStruct);
        kwt = new double[Semitones.IN_OCTAVE][seqLenTU];
        
        // All keys starts with weight 1
        for (double[] keyWeights : kwt) {
            Arrays.fill(keyWeights, 1.0);
        }

        // Abstract part that adds notes from a particular data structure
        addNotes(noteStruct);
        
        // Scale values so that max weight is 1
        for (int tu = 0; tu < seqLenTU; ++tu) {
            double max = -1;
            for (int key = 0; key < Semitones.IN_OCTAVE; ++key) {
                max = Math.max(max, kwt[key][tu]);
            }
            if (max == 1.0) {
                continue;
            }
            double factor = 1.0 / max;
            for (int key = 0; key < Semitones.IN_OCTAVE; ++key) {
                kwt[key][tu] *= factor;
            }
        }
        
        // Run key change detection implementation
        return detectKeyChanges();
    }

    private List<KeyChange> detectKeyChanges() {
        // Only consider keys with a long enough valid weight sequence
        runQualifications();
        
        // Main algorithm
        List<KeyChange> changes = new ArrayList<>();
        List<Double> scores = new ArrayList<>(Semitones.IN_OCTAVE);
        int leaderKey = -1;
        int tu = 0;
        while (tu < seqLenTU) {
            // Compute key scores starting from TU
            for (int key = qualified.beg(); key < qualified.end(); ++key) {
                scores.set(key, StatUtils.sum(kwt[key], tu, minKeyChangeGap));
            }
            ++tu;
            
            // Find best key
            int bestKey = scores.indexOf(Collections.max(scores));
            
            // Check if it is the first round
            if (leaderKey == -1) {
                leaderKey = bestKey;
                continue;
            }
            
            // Check if leader key stays the same
            if (bestKey == leaderKey) {
                continue;
            }
            
            // Check if score of second best key is significantly better than that of leader key
            scores.set(bestKey, 0.0);
            double secondBestScore = Collections.max(scores);
            if (secondBestScore / scores.get(leaderKey) > keyChangeSensibility) {
                // Change leader key
                leaderKey = scores.indexOf(secondBestScore);
                changes.add(new KeyChange(tu - 1, leaderKey));
            }
        }
        
        return changes;
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
                if (++keySpan == minKeyChangeGap) {
                    return true;
                }
            } else {
                keySpan = 0;
            }
        }
        return false;
    }
    
    
    //-------------------------------------- ADD NOTE --------------------------------------------\\

    protected void addNote(int begTU, int endTU, int hexNote) {
        for (int i = begTU; i < endTU; ++i) {
            int index = HEX_NOTE_TO_SIMPLE_KEY[hexNote % Semitones.IN_OCTAVE]+Semitones.IN_OCTAVE+4;
            for (int j = 0; j < Semitones.IN_OCTAVE; ++j) {
                kwt[j][i] *= kcw[index % Semitones.IN_OCTAVE];
                --index;
            }
        }
    }
}
