package atompacman.leraza.midi.container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import atompacman.leraza.midi.utilities.Instrument;

public class MIDIFile {

	private int midiType;
	private int divisionOfABeat;
	private int beatPerMeasure;
	private int valueOfTheBeatNote;
	private int clockTicksPerBeat;
	private int nb32thNotesPerBeat;
	private int nbSharps;
	private boolean isInMajorKey;
	private int tempo;
	private int finalTimestamp;
	private List<String> infos;
	private List<Integer> timeBeforeFirstTrackNote;
	private List<Instrument> trackInstruments;
	private List<Map<Integer, Stack<MIDINote>>> notes;
	private String filePath;

	
	//////////////////////////////
	//       Constructor        //
	//////////////////////////////
	
	public MIDIFile(String filePath){
		this.infos = new ArrayList<String>();
		this.timeBeforeFirstTrackNote = new ArrayList<Integer>();
		this.trackInstruments = new ArrayList<Instrument>();
		this.notes = new ArrayList<Map<Integer, Stack<MIDINote>>>();
		this.filePath = filePath;
	}
	
	
	//////////////////////////////
	//        GET NOTES         //
	//////////////////////////////
	
	public List<Map<Integer, Stack<MIDINote>>> getNotes() {
		return notes;
	}
	
	
	//////////////////////////////
	//         SETTERS          //
	//////////////////////////////
	
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

	public void addInfo(String info) {
		this.infos.add(info);
	}

	public void setTimeBeforeFirstNote(int timeBeforeFirstNote, int trackNo) {
		this.timeBeforeFirstTrackNote.set(trackNo, timeBeforeFirstNote);
	}

	public void setTrackInstrument(int index, Instrument instr) {
		while (trackInstruments.size() <= index) {
			trackInstruments.add(null);
		}
		this.trackInstruments.set(index, instr);
	}
	
	public void setNbTracks(int nbTracks) {
		for (int i = 0; i < nbTracks; ++i) {
			notes.add(new HashMap<Integer, Stack<MIDINote>>());
			timeBeforeFirstTrackNote.add(null);
		}
	}
	
	
	//////////////////////////////
	//         GETTERS          //
	//////////////////////////////
	
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

	public List<Instrument> getInstruments() {
		return trackInstruments;
	}
	
	public int getNbTracks() {
		return notes.size();
	}

	public int getTimeBeforeFirstNote(int trackNb) {
		return timeBeforeFirstTrackNote.get(trackNb);
	}
	
	public List<String> getInfos() {
		return infos;
	}

	public String getFilePath() {
		return filePath;
	}
}
