package com.atompacman.lereza.core.midi.in;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

import com.atompacman.lereza.core.theory.Key;
import com.atompacman.lereza.core.theory.NoteLetter;
import com.atompacman.lereza.core.theory.TimeSignature;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedMap;

public final class MIDISequenceContent {

    //
    //  ~  CONSTANTS  ~  //
    //

    private static final double DEFAULT_TEMPO           = 120.0;
    private static final Key    DEFAULT_KEY             = Key.of(NoteLetter.C);
            static final byte   NUM_32TH_NOTES_PER_BEAT = 8;


    //
    //  ~  FIELDS  ~  //
    //

    // Tracks
    private final List<MIDITrack> tracks;
    
    // Tempo/time signature/key changes
    private final TreeMap<Long, Double>        tempoChanges;
    private final TreeMap<Long, TimeSignature> timeSignChanges;
    private final TreeMap<Long, Key>           keySignChanges;

    // File
    private final File srcFile;

    // Other
    private Optional<Integer> lengthTU;
    private Optional<Integer> ticksPer64thNote;
    private Optional<String>  copyrightNotice;


    //
    //  ~  INIT  ~  //
    //

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
        this.lengthTU         = Optional.empty();
        this.ticksPer64thNote = Optional.empty();
        this.copyrightNotice  = Optional.empty();
    }


    //
    //  ~  SETTERS  ~  //
    //

    
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

    void setLengthTU(int lengthTU) {
        checkArgument(!this.lengthTU.isPresent(), "Sequence timeunit length cannot be redefined");
        this.lengthTU = Optional.of(lengthTU);
    }
    
    void setNumTicksPer64thNote(int ticksPer64thNote) {
        checkArgument(!this.ticksPer64thNote.isPresent(), 
                "Number of ticks per 64th notes can't be redefined");
        this.ticksPer64thNote = Optional.of(ticksPer64thNote);
    }

    void setCopyrightNotice(String copyrightNotice) {
        checkArgument(!this.copyrightNotice.isPresent(), 
                "A sequence cannot have multiple copyright notices");
        this.copyrightNotice = Optional.of(copyrightNotice);
    }


    //
    //  ~  GETTERS  ~  //
    //

    public ImmutableList<MIDITrack> getTracks() {
        return ImmutableList.copyOf(tracks);
    }
    
    public File getSourceFile() {
        return srcFile;
    }

    public ImmutableSortedMap<Long, Double> getTempoChanges() {
        return ImmutableSortedMap.copyOf(tempoChanges);
    }
    
    public ImmutableSortedMap<Long, TimeSignature> getTimeSignChanges() {
        return ImmutableSortedMap.copyOf(timeSignChanges);
    }

    public ImmutableSortedMap<Long, Key> getKeyChanges() {
        return ImmutableSortedMap.copyOf(keySignChanges);
    }

    public int getNumTicksPer64thNote() {
        checkArgument(ticksPer64thNote.isPresent(),"Num of ticks per 64th note should've been set");
        return ticksPer64thNote.get();
    }

    public int getSequenceLengthTU() {
        checkArgument(lengthTU.isPresent(),"Sequence timeunit length should've been set");
        return lengthTU.get();
    }
    
    public Optional<String> getCopyrightNotice() {
        return copyrightNotice;
    }
}
