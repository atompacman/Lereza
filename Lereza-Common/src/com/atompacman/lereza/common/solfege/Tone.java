package com.atompacman.lereza.common.solfege;

import com.atompacman.lereza.common.helper.EnumRepresConstructor;

public class Tone {

	public static final int NB_SEMITONES_IN_OCTAVE = 12;
	public static final int NB_TONES_IN_OCTAVE = 8;
	
	private NoteLetter note;
	private Accidental alteration;


	//------------ CONSTRUCTORS ------------\\

	public Tone(NoteLetter note) {
		this(note, Accidental.NONE);
	}
	
	public Tone(NoteLetter note, Accidental alteration) {
		this.note = note;
		this.alteration = alteration;
	}

	
	//------------ STATIC CONSTRUCTORS ------------\\

	public static Tone valueOf(String repres) {
		return (new EnumRepresConstructor<Tone>(Tone.class)).newInstance(repres);
	}
	
	public static Tone fromSemitoneValue(int semitoneValue) {
		int hexTone = semitoneValue % 12;
		switch(hexTone) {
		case 0 :  return new Tone(NoteLetter.C, Accidental.NONE);
		case 1 :  return new Tone(NoteLetter.C, Accidental.SHARP);
		case 2 :  return new Tone(NoteLetter.D, Accidental.NONE);
		case 3 :  return new Tone(NoteLetter.E, Accidental.FLAT);
		case 4 :  return new Tone(NoteLetter.E, Accidental.NONE);
		case 5 :  return new Tone(NoteLetter.F, Accidental.NONE);
		case 6 :  return new Tone(NoteLetter.F, Accidental.SHARP);
		case 7 :  return new Tone(NoteLetter.G, Accidental.NONE);
		case 8 :  return new Tone(NoteLetter.A, Accidental.FLAT);
		case 9 :  return new Tone(NoteLetter.A, Accidental.NONE);
		case 10 : return new Tone(NoteLetter.B, Accidental.FLAT);
		case 11 : return new Tone(NoteLetter.B, Accidental.NONE);
		default : return null;
		}
	}
	
	public static Tone fromSemitonesValue(int semitoneValue, Accidental accidental) {
		Tone tone = fromSemitoneValue(semitoneValue);
		tone.switchToAlteration(accidental);
		return tone;
	}
	
	
	//------------ GETTERS ------------\\

	public NoteLetter getNote() {
		return note;
	}

	public Accidental getAlteration() {
		return alteration;
	}

	
	//------------ SWITCH ALTERATION ------------\\

	public void switchAlteration() {
		switch (alteration) {
		case FLAT:
			note = note.getPrevious();
			alteration = Accidental.SHARP;
			break;
		case SHARP:
			note = note.getNext();
			alteration = Accidental.FLAT;
			break;
		default: break;
		}
	}

	public void switchToAlteration(Accidental accidental) {
		if (alteration != accidental && accidental != Accidental.NONE) {
			switchAlteration();
		}
	}
	
	
	//------------ AFTER INTERVAL ------------\\

	public Tone afterInterval(Interval interval) {
		return fromSemitonesValue(semitoneValue() + interval.semitoneValue(), alteration);
	}
	
	
	//------------ SEMITONE VALUE ------------\\

	public int semitoneValue() {
		return note.basicSemitoneValue() + alteration.semitoneAlteration();
	}

	
	//------------ STRING ------------\\

	public String toString() {
		return note.name() + alteration.toString();
	}

	
	//------------ EQUALITIES ------------\\

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result	+ ((alteration == null) ? 0 : alteration.hashCode());
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tone other = (Tone) obj;
		if (alteration != other.alteration)
			return false;
		if (note != other.note)
			return false;
		return true;
	}
}
