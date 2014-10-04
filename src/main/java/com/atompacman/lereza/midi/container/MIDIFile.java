package com.atompacman.lereza.midi.container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.atompacman.lereza.common.database.Storable;

public class MIDIFile implements Storable {

	private List<Map<Integer, Stack<MIDINote>>> notes;
	private List<MIDIInstrument> trackInstruments;

	private MIDIFileInfo fileInfos;

	private int midiType;
	private int divisionOfABeat;
	private int beatPerMeasure;
	private int valueOfTheBeatNote;
	private int clockTicksPerBeat;
	private int nb32thNotesPerBeat;
	private int valueOfShortestNote;
	private int nbSharps;
	private boolean isInMajorKey;
	private int tempo;
	private int finalTimestamp;
	private List<String> textInfos;

	
	//------------ CONSTRUCTORS ------------\\

	public MIDIFile(MIDIFileInfo fileInfos) {
		this.notes = new ArrayList<Map<Integer, Stack<MIDINote>>>();
		this.trackInstruments = new ArrayList<MIDIInstrument>();
		this.fileInfos = fileInfos;
		this.textInfos = new ArrayList<String>();
	}
	
	public MIDIFile() {
		this(null);
	}
	
	
	//------------ SETTERS ------------\\

	public void setNbTracks(int nbTracks) {
		for (int i = 0; i < nbTracks; ++i) {
			notes.add(new HashMap<Integer, Stack<MIDINote>>());
		}
	}
	
	public void setTrackInstrument(int index, MIDIInstrument instr) {
		while (trackInstruments.size() <= index) {
			trackInstruments.add(null);
		}
		this.trackInstruments.set(index, instr);
	}
	
	public void setFileInfo(MIDIFileInfo fileInfos) {
		this.fileInfos = fileInfos;
	}
	
	public void setMidiType(int midiType) {
		this.midiType = midiType;
	}

	public void setDivisionOfABeat(int divisionOfABeat) {
		this.divisionOfABeat = divisionOfABeat;
	}

	public void setBeatPerMeasure(int beatPerMeasure) {
		this.beatPerMeasure = beatPerMeasure;
	}

	public void setValueOfTheBeatNote(int valueOfTheBeatNote) {
		this.valueOfTheBeatNote = valueOfTheBeatNote;
	}

	public void setClockTicksPerBeat(int clockTicksPerBeat) {
		this.clockTicksPerBeat = clockTicksPerBeat;
	}

	public void setNb32thNotesPerBeat(int nb32thNotesPerBeat) {
		this.nb32thNotesPerBeat = nb32thNotesPerBeat;
	}

	public void setValueOfShortestNote(int valueOfShortestNote) {
		this.valueOfShortestNote = valueOfShortestNote;
	}
	
	public void setNbSharps(int nbSharps) {
		this.nbSharps = nbSharps;
	}
	
	public void setIfIsInMajorKey(boolean isInMajorKey) {
		this.isInMajorKey = isInMajorKey;
	}
	
	public void setTempo(int tempo) {
		this.tempo = tempo;
	}
	
	public void setFinalTimestamp(int finalTimestamp) {
		this.finalTimestamp = finalTimestamp;
	}

	public void addTextInfo(String info) {
		this.textInfos.add(info);
	}


	//------------ GETTERS ------------\\

	public List<Map<Integer, Stack<MIDINote>>> getNotes() {
		return notes;
	}
	
	public int getNbTracks() {
		return notes.size();
	}
	
	public List<MIDIInstrument> getInstruments() {
		return trackInstruments;
	}

	public MIDIFileInfo getFileInfos() {
		return fileInfos;
	}
	
	public int getMidiType() {
		return midiType;
	}

	public int getDivisionOfABeat() {
		return divisionOfABeat;
	}

	public int getBeatPerMeasure() {
		return beatPerMeasure;
	}

	public int getValueOfTheBeatNote() {
		return valueOfTheBeatNote;
	}

	public int getClockTicksPerBeat() {
		return clockTicksPerBeat;
	}

	public int getNb32thNotesPerBeat() {
		return nb32thNotesPerBeat;
	}

	public int getValueOfShortestNote() {
		return valueOfShortestNote;
	}
	
	public int getNbSharps() {
		return nbSharps;
	}
	
	public boolean isInMajorKey() {
		return isInMajorKey;
	}
	
	public int getTempo() {
		return tempo;
	}
	
	public int getFinalTimestamp() {
		return finalTimestamp;
	}

	public List<String> getTextInfos() {
		return textInfos;
	}
}
