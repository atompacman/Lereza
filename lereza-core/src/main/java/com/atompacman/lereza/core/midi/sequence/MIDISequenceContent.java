package com.atompacman.lereza.core.midi.sequence;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.atompacman.lereza.core.solfege.Key;
import com.atompacman.lereza.core.solfege.NoteLetter;
import com.atompacman.lereza.core.solfege.TimeSignature;

public class MIDISequenceContent {

    //====================================== CONSTANTS ===========================================\\

    private static final double DEFAULT_TEMPO           = 120.0;
    private static final Key    DEFAULT_KEY             = Key.valueOf(NoteLetter.C);
            static final byte   NUM_32TH_NOTES_PER_BEAT = 8;



    //======================================= FIELDS =============================================\\

    // Tracks
    private final List<MIDITrack> tracks;
    
    // Tempo/time signature/key changes
    private final TreeMap<Long, Double>        tempoChanges;
    private final TreeMap<Long, TimeSignature> timeSignChanges;
    private final TreeMap<Long, Key>           keySignChanges;

    // Other
    private final File   srcFile;
    private       int    lengthTU;
    private       int    ticksPer64thNote;
    private       String copyrightNotice;



    //======================================= METHODS ============================================\\

    //------------------------------------- CONSTRUCTORS -----------------------------------------\\

    MIDISequenceContent(File file) {
        // Tracks
        this.tracks = new ArrayList<>();

        // Tempo/time/key changes
        this.tempoChanges    = new TreeMap<>();
        this.timeSignChanges = new TreeMap<>();
        this.keySignChanges  = new TreeMap<>();
        
        this.tempoChanges   .put(0L, DEFAULT_TEMPO);
        this.timeSignChanges.put(0L, TimeSignature.STANDARD_4_4);
        this.keySignChanges .put(0L, DEFAULT_KEY);
        
        // Other
        this.srcFile          = file;
        this.lengthTU         = 0;
        this.ticksPer64thNote = 0;
        this.copyrightNotice  = null;
    }


    //--------------------------------------- SETTERS --------------------------------------------\\

    void addTrack(MIDITrack track) {
        tracks.add(track);
    }

    void addTempoChange(double bpm, long tick) {
        tempoChanges.put(tick, bpm);
    }

    void addTimeSignatureChange(TimeSignature timeSign, long tick) {
        timeSignChanges.put(tick, timeSign);
    }
    
    void addKeyChange(Key key, long tick) {
        keySignChanges.put(tick, key);
    }

    void setLengthTU(int lengthTick) {
        if (this.lengthTU != 0) {
            throw new IllegalArgumentException("Sequence timeunit length cannot be redefined");
        }
        this.lengthTU = lengthTick;
    }
    
    void setNumTicksPer64thNote(int ticksPer64thNote) {
        if (this.ticksPer64thNote != 0) {
            throw new IllegalArgumentException("Number of ticks per 64th notes can't be redefined");
        }
        this.ticksPer64thNote = ticksPer64thNote;
    }

    void setCopyrightNotice(String copyrightNotice) {
        if (this.copyrightNotice != null) {
            throw new IllegalArgumentException("A sequence cannot have multiple copyright notices");
        }
        this.copyrightNotice = copyrightNotice;
    }


    //--------------------------------------- GETTERS --------------------------------------------\\

    public List<MIDITrack> getTracks() {
        return tracks;
    }
    
    public File getSourceFile() {
        return srcFile;
    }

    public TreeMap<Long, Double> getTempoChanges() {
        return tempoChanges;
    }
    
    public TreeMap<Long, TimeSignature> getTimeSignChanges() {
        return timeSignChanges;
    }

    public TreeMap<Long, Key> getKeyChanges() {
        return keySignChanges;
    }

    public int getNumTicksPer64thNote() {
        return ticksPer64thNote;
    }

    public String getCopyrightNotice() {
        return copyrightNotice;
    }

    public int getSequenceLengthTU() {
        return lengthTU;
    }
}
