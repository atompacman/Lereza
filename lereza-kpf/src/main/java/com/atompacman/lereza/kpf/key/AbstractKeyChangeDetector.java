package com.atompacman.lereza.kpf.key;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.util.MathArrays;

import com.atompacman.lereza.core.solfege.Semitones;

public abstract class AbstractKeyChangeDetector<T> {

    //===================================== INNER TYPES ==========================================\\

    enum SimpleKey { Ab, Eb, Bb, F, C, G, D, A, E, B, Fs, Cs };


    
    //======================================= FIELDS =============================================\\

    // Parameters
    // Key consonance window
    private   final double[] kcw;
    protected final int      minKeySpan;
    
    // Temporaries
    // Key weight timeline
    protected double[][] kwt;
    
    
    
    //=================================== ABSTRACT METHODS =======================================\\

    protected abstract int timeunitLengthOf(T noteStruct);

    protected abstract void addNotes(T noteStruct);

    protected abstract List<KeyChange> detectKeyChanges(T noteStruct);
    

    
    //======================================= METHODS ============================================\\

    //--------------------------------- PUBLIC CONSTRUCTORS --------------------------------------\\

    public AbstractKeyChangeDetector(File keyConsonanceWindow, int minKeySpan) {
        // Parameters
        this.kcw        = readKeyConsonanceWindowFile(keyConsonanceWindow);
        this.minKeySpan = minKeySpan;
        
        // Temporaries
        this.kwt = null;
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
        kwt = new double[Semitones.IN_OCTAVE][timeunitLengthOf(noteStruct)];
        
        // All keys starts with weight 1
        for (double[] frame : kwt) {
            Arrays.fill(frame, 1.0);
        }

        // Abstract part that adds notes from a particular data structure
        addNotes(noteStruct);
        
        // Scale values
        for (double[] frame : kwt) {
            MathArrays.scaleInPlace(1.0 / StatUtils.max(frame), frame);
        }
        
        // Run key change detection implementation
        return detectKeyChanges(noteStruct);
    }

    protected void addNote(int beg, int end, int note) {
        for (int i = beg; i < end; ++i) {
            int index = hexNoteToSimpleKey(note) + Semitones.IN_OCTAVE + 4;
            for (int j = 0; j < Semitones.IN_OCTAVE; ++j) {
                kwt[i][j] *= kcw[index % Semitones.IN_OCTAVE];
                --index;
            }
        }
    }

    private static int hexNoteToSimpleKey(int hexNote) {
        switch (hexNote % 12) {
        case 0:   return SimpleKey.C.ordinal();
        case 1:   return SimpleKey.Cs.ordinal();
        case 2:   return SimpleKey.D.ordinal();
        case 3:   return SimpleKey.Eb.ordinal();
        case 4:   return SimpleKey.E.ordinal();
        case 5:   return SimpleKey.F.ordinal();
        case 6:   return SimpleKey.Fs.ordinal();
        case 7:   return SimpleKey.G.ordinal();
        case 8:   return SimpleKey.Ab.ordinal();
        case 9:   return SimpleKey.A.ordinal();
        case 10:  return SimpleKey.Bb.ordinal();
        case 11:  return SimpleKey.B.ordinal();
        default:  throw new IllegalArgumentException();
        }
    }
}
