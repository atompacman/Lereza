package atompacman.leraza.midi.container;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MIDIFile {

	private int midiType;
	private int divisionOfABeat;
	private int beatPerMeasure;
	private int valueOfTheBeatNote;
	private int clockTicksPerBeat;
	private int nb32thNotesPerBeat;
	private int tempo;
	private List<String> infos;
	private List<Integer> timeBeforeFirstTrackNote;
	private List<List<MIDINote>> notes;
	private File file;

	
	//////////////////////////////
	//       Constructor        //
	//////////////////////////////
	
	public MIDIFile(File file){
		this.infos = new ArrayList<String>();
		this.timeBeforeFirstTrackNote = new ArrayList<Integer>();
		this.notes = new ArrayList<List<MIDINote>>();
		this.file = file;
	}
	
	
	//////////////////////////////
	//        GET NOTES         //
	//////////////////////////////
	
	public List<List<MIDINote>> getNotes() {
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

	public void setTempo(int tempo) {
		this.tempo = tempo;
	}

	public void addInfo(String info) {
		this.infos.add(info);
	}

	public void setNotes(List<List<MIDINote>> notes) {
		this.notes = notes;
	}

	public void setTimeBeforeFirstNote(int timeBeforeFirstNote, int trackNo) {
		this.timeBeforeFirstTrackNote.set(trackNo, timeBeforeFirstNote);
	}

	public void setNbTracks(int nbTracks) {
		for (int i = 0; i < nbTracks; ++i) {
			notes.add(new ArrayList<MIDINote>());
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

	public int getTempo() {
		return tempo;
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

	public File getFile() {
		return file;
	}
}
