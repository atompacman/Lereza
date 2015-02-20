package com.atompacman.lereza.midi;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atompacman.lereza.solfege.Grouping;
import com.atompacman.lereza.solfege.Key;
import com.atompacman.lereza.solfege.Meter;
import com.atompacman.lereza.solfege.NoteLetter;
import com.atompacman.lereza.solfege.RythmicSignature;
import com.atompacman.lereza.solfege.Tone;

public class MIDISequence {

	//====================================== CONSTANTS ===========================================\\

	static final int						DEFAULT_NUM_32TH_NOTES_PER_BEAT = 8;
	private static final Key 				DEFAULT_KEY = Key.valueOf(Tone.valueOf(NoteLetter.C));
	private static final RythmicSignature 	DEFAULT_RYTHMIC_SIGNATURE = 
			new RythmicSignature(new Meter(4, 4), Grouping.DUPLETS);
	
	
	
	//======================================= FIELDS =============================================\\

	private List<MIDITrack> 	tracks;
	
	private File				file;
	
	private Key					key;
	private RythmicSignature	signature;
	private Integer				num32thNotesPerBeat;
	private Map<Long, Double>  	tempoChanges;
	private String				copyrightNotice;

	
	
	//======================================= METHODS ============================================\\

	//---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\

	MIDISequence(File file) {
		this.tracks = new ArrayList<>();
		this.file = file;
		this.tempoChanges = new HashMap<>();
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

	void setSignature(Meter meter) {
		if (this.signature != null) {
			throw new IllegalArgumentException("A MIDI sequence cannot specify multiple meter");
		}
		this.signature = new RythmicSignature(meter, Grouping.DUPLETS);
	}

	void setNum32thNotesPerBeat(int num32thNotesPerBeat) {
		if (this.num32thNotesPerBeat != null) {
			throw new IllegalArgumentException("A MIDI sequence cannot "
					+ "specify multiple number of 32th notes per beat");
		}
		this.num32thNotesPerBeat = num32thNotesPerBeat;
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

	public RythmicSignature getRythmicSignature() {
		return signature == null ? DEFAULT_RYTHMIC_SIGNATURE : signature;
	}

	public int getNum32thNotesPerBeat() {
		return num32thNotesPerBeat == null ? DEFAULT_NUM_32TH_NOTES_PER_BEAT : num32thNotesPerBeat;
	}
	
	public Map<Long, Double> getTempoChanges() {
		return tempoChanges;
	}
	
	public String getCopyrightNotice() {
		return copyrightNotice;
	}
}
