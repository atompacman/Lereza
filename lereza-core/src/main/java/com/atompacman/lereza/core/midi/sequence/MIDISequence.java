package com.atompacman.lereza.core.midi.sequence;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atompacman.lereza.core.solfege.Grouping;
import com.atompacman.lereza.core.solfege.Key;
import com.atompacman.lereza.core.solfege.NoteLetter;
import com.atompacman.lereza.core.solfege.TimeSignature;

public class MIDISequence {

    //====================================== CONSTANTS ===========================================\\

    static final byte NUM_32TH_NOTES_PER_BEAT = 8;

    private static final Key DEFAULT_KEY = Key.valueOf(NoteLetter.C);



    //======================================= FIELDS =============================================\\

    private List<MIDITrack>   tracks;

    private File              file;

    private Key               key;
    private TimeSignature     signature;
    private Map<Long, Double> tempoChanges;
    private String            copyrightNotice;



    //======================================= METHODS ============================================\\

    //---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\

    MIDISequence(File file) {
        this.tracks          = new ArrayList<>();

        this.file            = file;

        this.key             = null;
        this.signature       = null;
        this.tempoChanges    = new HashMap<>();
        this.copyrightNotice = null;
    }


    //-------------------------------------- ADD TRACK -------------------------------------------\\

    void addTrack(MIDITrack track) {
        tracks.add(track);
    }


    //--------------------------------------- SETTERS --------------------------------------------\\

    void setKey(Key key) {
        if (this.key != null) {
            throw new IllegalArgumentException("A MIDI sequence cannot specify multiple keys");
        }
        this.key = key;
    }

    void setSignature(int numMeter, int denMeter) {
        if (this.signature != null) {
            throw new IllegalArgumentException("A MIDI sequence cannot specify multiple meter");
        }
        this.signature = TimeSignature.valueOf(numMeter, denMeter, Grouping.DUPLETS);
    }

    void addTempoChange(long tick, double bpm) {
        tempoChanges.put(tick, bpm);
    }

    void setCopyrightNotice(String copyrightNotice) {
        if (this.copyrightNotice != null) {
            throw new IllegalArgumentException("A sequence cannot have multiple copyright notices");
        }
        this.copyrightNotice = copyrightNotice;
    }


    //--------------------------------------- GETTERS --------------------------------------------\\

    public MIDITrack getTrack(int num) {
        return tracks.get(num);
    }

    public File getFile() {
        return file;
    }

    public int getNumTracks() {
        return tracks.size();
    }

    public Key getKey() {
        return key == null ? DEFAULT_KEY : key;
    }

    public TimeSignature getTimeSignature() {
        return signature == null ? TimeSignature.STANDARD_4_4 : signature;
    }

    public Map<Long, Double> getTempoChanges() {
        return tempoChanges;
    }

    public String getCopyrightNotice() {
        return copyrightNotice;
    }
}
