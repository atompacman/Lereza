package com.atompacman.lereza.midi.container;

import com.atompacman.lereza.midi.HexToNote;

public class MIDINote {
	
	private int note;
	private int length;

	
	//------------ CONSTRUCTORS ------------\\

	public MIDINote(int noteName, int noteLength) {
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
