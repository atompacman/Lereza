package com.atompacman.lereza.common.solfege;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.lereza.common.helper.EnumRepresConstructor;

public class Tone {

	private static EnumRepresConstructor<Tone> enumRepresConstructor = new EnumRepresConstructor<Tone>(Tone.class);

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
		return enumRepresConstructor.newInstance(repres);
	}

	public static Tone thatIsMoreCommonForSemitoneValue(int semitoneValue) {
		List<Tone> possibleTones = fromSemitoneValue(semitoneValue);
		if (possibleTones.size() == 1) {
			return possibleTones.get(0);
		}
		Tone a = possibleTones.get(0);
		Tone b = possibleTones.get(1);
		int distanceA = CircleOfFifths.distanceToMiddleOfCircle(a);
		int distanceB = CircleOfFifths.distanceToMiddleOfCircle(b);

		if (Math.abs(distanceA) == Math.abs(distanceB)) {
			if (distanceA < distanceB) {
				return a;
			} else {
				return b;
			}
		} else if (Math.abs(distanceA) < Math.abs(distanceB)) {
			return a;
		} else {
			return b;
		}
	}

	public static List<Tone> fromSemitoneValue(int semitoneValue) {
		semitoneValue = Semitones.normalize(semitoneValue);

		List<Tone> possibleTones = new ArrayList<Tone>();
		List<NoteLetter> possibleNotes = NoteLetter.fromSemitoneValue(semitoneValue);
		
		for (NoteLetter possibleNote : possibleNotes) {
			int semitoneAlteration = semitoneValue - possibleNote.basicSemitoneValue();
			Accidental accidental = Accidental.fromSemitoneAlteration(semitoneAlteration);
			possibleTones.add(new Tone(possibleNote, accidental));
		}
		return possibleTones;
	}
	
	public static Tone fromSemitoneValue(int semitoneValue, int diatonicToneValue) {
		semitoneValue = Semitones.normalize(semitoneValue);
		diatonicToneValue = DiatonicTones.normalize(diatonicToneValue);
		
		NoteLetter note = NoteLetter.fromDiatonicToneValue(diatonicToneValue);
		if (note.canBeAssignedFrom(semitoneValue)) {
			int semitoneAlteration = semitoneValue - note.basicSemitoneValue();
			Accidental accidental = Accidental.fromSemitoneAlteration(semitoneAlteration);
			return new Tone(note, accidental);
		}
		throw new IllegalArgumentException("Note tone can be assigned from semitone value \"" 
				+ semitoneValue + "\" and diatonic tone value \"" + diatonicToneValue + "\".");
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
		if (alteration != accidental) {
			switchAlteration();
		}
	}


	//------------ AFTER INTERVAL ------------\\

	public Tone afterInterval(Interval interval) {
		int semitoneDelta = semitoneValue() + interval.semitoneValue();
		int diatonicToneDelta = diatonicToneValue() + interval.diatonicToneValue();
		return fromSemitoneValue(semitoneDelta, diatonicToneDelta);
	}


	//------------ TONE / SEMITONE ------------\\

	public int diatonicToneValue() {
		return note.ordinal();
	}
	
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
