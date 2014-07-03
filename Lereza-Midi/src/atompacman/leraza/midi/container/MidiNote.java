package atompacman.leraza.midi.container;

import atompacman.leraza.midi.utils.HexToNote;

public class MidiNote {
	
	private int note;
	private int length;

	
	public MidiNote(int noteName, int noteLength) {
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
