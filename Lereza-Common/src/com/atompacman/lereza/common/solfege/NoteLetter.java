package com.atompacman.lereza.common.solfege;

public enum NoteLetter {
	
	C (0), D (2), E (4), F (5), G (7), A (9), B (11);
	
	
	private int basicNbSemitones;
	
	
	//------------ CONSTRUCTORS ------------\\

	private NoteLetter(int basicNbSemitones) {
		this.basicNbSemitones = basicNbSemitones;
	}
	
	
	//------------ PREVIOUS / NEXT ------------\\

	public NoteLetter getNext() {
		return NoteLetter.values()[(ordinal() + 1) % values().length];
	}
	
	public NoteLetter getPrevious() {
		return NoteLetter.values()[(ordinal() + 6) % values().length];
	}

	
	//------------ SEMITONE VALUE ------------\\

	public int basicSemitoneValue() {
		return basicNbSemitones;
	}
}
