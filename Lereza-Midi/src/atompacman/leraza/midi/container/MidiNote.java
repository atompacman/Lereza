package atompacman.leraza.midi.container;

import atompacman.leraza.midi.utils.HexToNote;

public class MidiNote {
	
	private int note;
	private int length;

	
	//------------ CONSTRUCTORS ------------\\

	public MidiNote(int noteName, int noteLength) {
		this.note = noteName;
		this.length = noteLength;
	}

	
	//------------ SETTERS ------------\\

	public void setLength(int length) {
		this.length = length;
	}
	
	
	//------------ GETTERS ------------\\

	public int getNote() {
		return note;
	}

	public int getLength() {
		return length;
	}
	

	//------------ STRING ------------\\

	public String toString() {
		return HexToNote.toString(note) + " (" + length + ")";
	}
}
