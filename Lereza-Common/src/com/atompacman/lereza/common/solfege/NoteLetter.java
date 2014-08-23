package com.atompacman.lereza.common.solfege;

import java.util.ArrayList;
import java.util.List;

public enum NoteLetter {
	
	C (0), D (2), E (4), F (5), G (7), A (9), B (11);
	
	
	private int basicSemitoneValue;
	
	
	//------------ CONSTRUCTORS ------------\\

	private NoteLetter(int basicSemitoneValue) {
		this.basicSemitoneValue = basicSemitoneValue;
	}
	
	
	//------------ STATIC CONSTRUCTORS ------------\\

	public static NoteLetter fromDiatonicToneValue(int diatonicToneValue) {
		return values()[DiatonicTones.normalize(diatonicToneValue)];
	}
	
	public static List<NoteLetter> fromSemitoneValue(int semitoneValue) {
		List<NoteLetter> notes = new ArrayList<NoteLetter>(); 
		for (NoteLetter note : values()) {
			if (note.canBeAssignedFrom(semitoneValue)) {
				notes.add(note);
			}
		}
		return notes;
	}
	
	
	//------------ CAN BE ASSIGNED FROM ------------\\

	public boolean canBeAssignedFrom(int semitoneValue) {
		int delta = Math.abs(basicSemitoneValue - Semitones.normalize(semitoneValue));
		return delta <= 1 || (Semitones.IN_OCTAVE - delta) <= 1;
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
		return basicSemitoneValue;
	}
}
