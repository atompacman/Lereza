package com.atompacman.lereza.kpf;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.sound.midi.MidiEvent;

import org.apache.commons.io.FileUtils;

import com.atompacman.lereza.core.midi.tool.MIDINoteEventProcessor;
import com.atompacman.lereza.core.solfege.Semitones;

public class KeyPathFinder {

    //====================================== CONSTANTS ===========================================\\

    //private static final Logger logger = LogManager.getLogger(KeyPathFinder.class);

    
    
    //===================================== INNER TYPES ==========================================\\

    private enum SimpleKey { Ab, Eb, Bb, F, C, G, D, A, E, B, Fs, Cs };

    private class KeyPath implements Cloneable {
        int[]  tus     = new int[maxNumKeys];
        int[]  keys    = new int[maxNumKeys];
        int    numKeys = 0;
        double score   = 0;
        
        public KeyPath clone() {
            KeyPath o = new KeyPath();
            o.tus     = tus.clone();
            o.keys    = keys.clone();
            o.numKeys = numKeys;
            o.score   = score;
            return o;
        }
    }
    
    
    
    //======================================= FIELDS =============================================\\

    // Parameters
    private final int        maxTU;
    private final int        maxNumKeys;
    
    // Temporaries
    private final double[]   kcw;
    private final double[][] kwt;
    private final int[]      notesBeg;
    private int              endTU;
    private KeyPath          curr;
    private KeyPath          best;


    
    //======================================= METHODS ============================================\\

    //---------------------------------- PUBLIC CONSTRUCTOR --------------------------------------\\

    public KeyPathFinder(File keyConsonanceWindow, int maxTU, int maxNumKeys) {
        // Parameters
        this.maxTU      = maxTU;
        this.maxNumKeys = maxNumKeys;
        
        // Temporaries
        try {
            // Key consonance window
            this.kcw    = readKeyConsonanceWindowFile(keyConsonanceWindow);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error reading key consonance window file", e);
        }
        // Key weight timeline
        this.kwt        = new double[maxTU][Semitones.IN_OCTAVE];
        this.notesBeg   = new int[256];
        this.endTU      = 0;
        this.curr       = null;
        this.best       = null;
    }
    
    @SuppressWarnings("unchecked")
    private static double[] readKeyConsonanceWindowFile(File kcwFile) throws IOException {
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
    }

    
    //---------------------------------------- FIND ----------------------------------------------\\

    public KeyPath find(List<MidiEvent> events, int endTU) {
        // Check that end timeunit of event sequence is not greater than the maximum
        this.endTU = endTU;
        if (endTU > maxTU) {
            throw new IllegalArgumentException("Sequence length (" + endTU + 
                    " tu) exceeds maximum length (" + maxTU + " tu)");
        }
        
        // Create key weight timeline
        createKeyWeightTimeline(events);
        
        // Create best and current paths
        KeyPath best = new KeyPath();
        KeyPath curr = new KeyPath();

        // Try finding a better path for each number of keys in the path
        for (int numKeys = 1; numKeys < maxNumKeys; ++numKeys) {
            curr.numKeys = numKeys;
            // Start path at every possible starting key
            for (int startingKey = 0; startingKey < Semitones.IN_OCTAVE; ++startingKey) {
                curr.keys[0] = startingKey;
                find(1);
            }
        }
        
        return best;
    }
    
    private void find(int numKeys) {
        // Key before change
        int currKey = curr.keys[numKeys - 1];
        // Cumulative score for current key
        double keyScore = 0;
        // Current path score before executing the method
        double prevScore = curr.score;
        // Maximum timeunit for key change
        int maxTU = endTU - curr.numKeys + numKeys + 1;
        // Length of this key
        int keyLen = 0;
        
        // Try changing key at every moment possible
        for (int tu = curr.tus[numKeys - 1] + 1; tu < maxTU; ++tu) {
            // Increment basic key score for additional timeunit with this key
            keyScore += kwt[tu - 1][currKey];
            ++keyLen;
            
            // Update score (penalize short keys)
            curr.score = prevScore + (keyLen <= 10 ? keyScore / (10 - keyLen) : keyScore);

            // If maximum number of keys is reached
            if (numKeys == curr.numKeys) {
                // If end of sequence is reached
                if (tu != endTU) {
                    continue;
                }
                // If best score is beaten
                if (curr.score > best.score) {
                    best = curr.clone();
                }
            } else {
                // If best score cannot be beaten
                if (curr.score + endTU - tu < best.score) {
                    continue;
                }
                // Try changing to every possible key
                for (int key = 0; key < Semitones.IN_OCTAVE; ++key) {
                    // Cannot change to same key
                    if(key == currKey) {
                        continue;
                    }
                    curr.keys[numKeys] = key;
                    find(numKeys + 1);
                }
            }
        }
        
        // Restore score
        curr.score = prevScore;
    }
    
    
    // - - - - - - - - - - - - - - - CREATE KEY WEIGHT TIMELINE - - - - - - - - - - - - - - - - - \\

    private void createKeyWeightTimeline(List<MidiEvent> events) {
        // All keys starts with weight 1
        for (int i = 0; i < endTU; ++i) {
            Arrays.fill(kwt[i], 1.0);
        }
        
        // We store in this array the tick when the note begins
        Arrays.fill(notesBeg, 0);
        
        MIDINoteEventProcessor.process(events,
                // Note begins
                (hexNote, tick) -> notesBeg[hexNote] = (int) tick,
                // Note ends
                (hexNote, tick) -> { addNote(notesBeg[hexNote], (int) tick, hexNote);
                                     notesBeg[hexNote] = 0; });
        
        // Add notes that have end (we force them to end at the maximum timeunit)
        for (int i = 0; i < notesBeg.length; ++i) {
            if (notesBeg[i] != 0) {
                addNote(notesBeg[i], endTU, i);
            }
        }
        
        // Normalize values
        for (double[] frame : kwt) {
            double max = 0;
            for (int i = 0; i < frame.length; ++i) {
                if (frame[i] > max) {
                    max = frame[i];
                }
            }
            for (int i = 0; i < frame.length; ++i) {
                frame[i] /= max;
            }
        }
    }
    
    private void addNote(int beg, int end, int note) {
        for (int i = beg; i < end; ++i) {
            for (int j = 0; j < Semitones.IN_OCTAVE; ++j) {
                int index = (hexNoteToSimpleKey(note) - j + Semitones.IN_OCTAVE + 4);
                kwt[i][j] *= kcw[index % Semitones.IN_OCTAVE];
            }
        }
    }
    
    private int hexNoteToSimpleKey(int hexNote) {
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
