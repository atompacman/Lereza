package com.atompacman.lereza.midi.container;

import com.atompacman.lereza.midi.HexToNote;

public class MIDINote {
	
	private int hexNote;
	private int length;

	
	//------------ CONSTRUCTORS ------------\\

	public MIDINote(int hexNote, int noteLength) {
		this.hexNote = hexNote;
		this.length = noteLength;
	}

	
	//------------ SETTERS ------------\\

	public void setLength(int length) {
		this.length = length;
	}
	
	
	//------------ GETTERS ------------\\

	public int getNote() {
		return hexNote;
	}

	public int getLength() {
		return length;
	}
	

	//------------ STRING ------------\\

	public String toString() {
		return HexToNote.toString(hexNote) + " (" + length + ")";
	}
}
