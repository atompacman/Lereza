package com.atompacman.lereza.core.midi.in;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.atompacman.toolkat.annotations.DerivableFrom;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

public final class MIDITrack {

    //
    //  ~  FIELDS  ~  //
    //

    // Notes
    private Multimap<Long, MIDINote> startingNotes;
    private Multimap<Long, MIDINote> endingNotes;
    
    // Limits
    @DerivableFrom("startingNotes")
    private long startTick;
    @DerivableFrom("endingNotes")
    private long endTick;

    // Names
    private Optional<String> name;
    private Optional<String> instrumentName;

    // Instrument changes
    private Map<Long, MIDIInstrument> instrChanges;

    // Channel / Port
    private Optional<Integer> channelNumber;
    private Optional<Integer> midiPort;

    // Text info
    private List<String> textContent;
    private List<String> lyrics;
    private List<String> markers;
    private List<String> cuePoints;


    //
    //  ~  INIT  ~  //
    //

    MIDITrack() {
        // Notes
        this.startingNotes  = HashMultimap.create();
        this.endingNotes    = HashMultimap.create();
        
        // Limits
        this.startTick      = Integer.MAX_VALUE;
        this.endTick        = 0;

        // Names
        this.name           = Optional.empty();
        this.instrumentName = Optional.empty();

        // Instrument changes
        this.instrChanges   = new HashMap<>();

        // Channel / Port
        this.channelNumber  = Optional.empty();
        this.midiPort       = Optional.empty();

        // Text info
        this.textContent    = new LinkedList<>();
        this.lyrics         = new LinkedList<>();
        this.markers        = new LinkedList<>();
        this.cuePoints      = new LinkedList<>();
    }


    //
    //  ~  SETTERS  ~  //
    //

    void addNote(MIDINote note) {
        checkArgument(startingNotes.put(note.startTick(), note), "Cannot add starting note "
                + "\"%s\" multiple times at tick \"%s\"", note, note.startTick());
        checkArgument(endingNotes.put(note.endTick(), note), "Cannot add ending note "
                + "\"%s\" multiple times at tick \"%s\"", note, note.startTick());
        startTick = Math.min(startTick, note.startTick());
        endTick   = Math.max(endTick,   note.endTick());
    }

    void setChannelNumber(int channelNumber) {
        checkArgument(!this.channelNumber.isPresent(), "Channel number cannot be redefined");
        this.channelNumber = Optional.of(channelNumber);
    }

    void setMidiPort(int midiPort) {
        checkArgument(!this.midiPort.isPresent(), "Midi port cannot be redefined");
        this.midiPort = Optional.of(midiPort);
    }

    void setName(String name) {
        checkArgument(!this.name.isPresent(), "Track name cannot be redefined");
        this.name = Optional.of(name);
    }

    void setInstrumentName(String instrumentName) {
        checkArgument(!this.instrumentName.isPresent(), "Instrument name cannot be redefined");
        this.instrumentName = Optional.of(instrumentName);
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


    //
    //  ~  GETTERS  ~  //
    //

    public ImmutableMultimap<Long, MIDINote> getNotes() {
        return ImmutableMultimap.copyOf(startingNotes);
    }

    public ImmutableMultimap<Long, MIDINote> getEndingNotes() {
        return ImmutableMultimap.copyOf(endingNotes);
    }

    public long getStartTick() {
        return startTick;
    }

    public long getEndTick() {
        return endTick;
    }

    public Optional<Integer> getChannelNumber() {
        return channelNumber;
    }

    public Optional<Integer> getMIDIPort() {
        return midiPort;
    }

    public Optional<String> getName() {
        return name;
    }

    public Optional<String> getInstrumentName() {
        return instrumentName;
    }

    public ImmutableList<String> getTextContent() {
        return ImmutableList.copyOf(textContent);
    }

    public ImmutableList<String> getLyrics() {
        return ImmutableList.copyOf(lyrics);
    }

    public ImmutableList<String> getMarkers() {
        return ImmutableList.copyOf(markers);
    }

    public ImmutableList<String> getCuePoints() {
        return ImmutableList.copyOf(cuePoints);
    }
}
