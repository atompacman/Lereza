package com.atompacman.lereza.core.midi.sequence;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MIDITrack {

	//======================================= FIELDS =============================================\\

	// Notes
	private Map<Long, Set<MIDINote>> notes;

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
	
	// Temporary field
	private MIDINote[] noteBuffer;

	
	
	//======================================= METHODS ============================================\\

	//---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\

	MIDITrack() {
	    // Notes
		this.notes 			= new HashMap<>();
		
		// Names
		this.name			= "Unspecified name";
		this.instrumentName = "Unspecified instrument name";
		
		// Instrument changes
		this.instrChanges	= new HashMap<>();
		
		// Channel / Port
		this.channelNumber	= null;
		this.midiPort		= null;
		
		// Text info
		this.textContent 	= new LinkedList<>();
		this.lyrics		 	= new LinkedList<>();
		this.markers		= new LinkedList<>();
		this.cuePoints		= new LinkedList<>();
		
	    // Temporary field
        this.noteBuffer     = new MIDINote[128];
	}
	
	
	//--------------------------------------- SETTERS --------------------------------------------\\

	void addNote(byte hexNote, byte velocity, long tick) {
		MIDINote note = noteBuffer[hexNote];
		if (note == null) {
			noteBuffer[hexNote] = new MIDINote(hexNote, velocity, tick);
		} else {
			note.setEndTick(tick);
			Set<MIDINote> set = notes.get(note.startTick());
			if (set == null) {
				set = new HashSet<>();
				notes.put(note.startTick(), set);
			}
			set.add(note);
			noteBuffer[hexNote] = null;
		}
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
		return notes;
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
