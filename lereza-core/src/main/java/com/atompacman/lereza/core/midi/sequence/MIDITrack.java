package com.atompacman.lereza.core.midi.sequence;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.atompacman.lereza.core.piece.PieceSubstructureModel;

public final class MIDITrack implements PieceSubstructureModel {

    //======================================= FIELDS =============================================\\

    // Notes
    private Map<Long, Set<MIDINote>> startingNotes;
    private Map<Long, Set<MIDINote>> endingNotes;
    private long                     startTick;
    private long                     endTick;

    // Names
    private String name;
    private String instrumentName;

    // Instrument changes
    private Map<Long, MIDIInstrument> instrChanges;

    // Channel / Port
    private Integer	channelNumber;
    private Integer	midiPort;

    // Text info
    private List<String> textContent;
    private List<String> lyrics;
    private List<String> markers;
    private List<String> cuePoints;



    //======================================= METHODS ============================================\\

    //---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\

    MIDITrack() {
        // Notes
        this.startingNotes  = new HashMap<>();
        this.endingNotes    = new HashMap<>();
        this.startTick      = 0;
        this.endTick        = 0;

        // Names
        this.name           = "Unspecified name";
        this.instrumentName = "Unspecified instrument name";

        // Instrument changes
        this.instrChanges   = new HashMap<>();

        // Channel / Port
        this.channelNumber  = null;
        this.midiPort       = null;

        // Text info
        this.textContent    = new LinkedList<>();
        this.lyrics         = new LinkedList<>();
        this.markers        = new LinkedList<>();
        this.cuePoints      = new LinkedList<>();
    }


    //--------------------------------------- SETTERS --------------------------------------------\\

    void addNote(MIDINote note) {
        addNoteToMap(note, startingNotes, note.startTick());
        addNoteToMap(note, endingNotes,   note.endTick());
        startTick = Math.min(startTick, note.startTick());
        endTick   = Math.max(endTick,   note.endTick());
    }

    private static void addNoteToMap(MIDINote note, Map<Long, Set<MIDINote>> noteMap, long tick) {
        Set<MIDINote> set = noteMap.get(tick);
        if (set == null) {
            set = new HashSet<>();
            noteMap.put(tick, set);
        }
        set.add(note);
    }

    void setChannelNumber(int channelNumber) {
        this.channelNumber = channelNumber;
    }

    void setMidiPort(int midiPort) {
        this.midiPort = midiPort;
    }

    void setName(String name) {
        this.name = name;
    }

    void setInstrumentName(String instrumentName) {
        this.instrumentName = instrumentName;
    }

    void addInstrumentChange(int patch, long tick) {
        this.instrChanges.put(tick, MIDIInstrument.values()[patch]);
    }

    void addText(String text) {
        this.textContent.add(text);
    }

    void addLyrics(String lyrics) {
        this.lyrics.add(lyrics);
    }

    void addMarker(String marker) {
        this.markers.add(marker);
    }

    void addCuePoint(String cuePoint) {
        this.cuePoints.add(cuePoint);
    }


    //--------------------------------------- GETTERS --------------------------------------------\\

    public Map<Long, Set<MIDINote>> getNotes() {
        return startingNotes;
    }

    public Map<Long, Set<MIDINote>> getEndingNotes() {
        return endingNotes;
    }

    public long getStartTick() {
        return startTick;
    }

    public long getEndTick() {
        return endTick;
    }

    public Integer getChannelNumber() {
        return channelNumber;
    }

    public Integer getMIDIPort() {
        return midiPort;
    }

    public String getName() {
        return name;
    }

    public String getInstrumentName() {
        return instrumentName;
    }

    public List<String> getTextContent() {
        return textContent;
    }

    public List<String> getLyrics() {
        return lyrics;
    }

    public List<String> getMarkers() {
        return markers;
    }

    public List<String> getCuePoints() {
        return cuePoints;
    }
}
