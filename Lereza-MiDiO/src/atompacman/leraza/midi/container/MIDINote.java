package atompacman.leraza.midi.container;

import atompacman.leraza.midi.utilities.HexToNote;

public class MIDINote {
	
	private int note;
	private int length;

	
	public MIDINote(int noteName, int noteLength) {
		this.note = noteName;
		this.length = noteLength;
	}

	public int getNote() {
		return note;
	}

	public int getLength() {
		return Math.abs(length);
	}

	public boolean isRest() {
		return (note == 0);
	}
	
	public boolean isStaccato() {
		return length < 0;
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
	public String toString() {
		if (isRest()) {
			return "[R] (" + length + ")";
		} else {
			return HexToNote.toString(note) + " (" + length + ")";
		}
	}
}
