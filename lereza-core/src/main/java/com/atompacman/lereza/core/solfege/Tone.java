package com.atompacman.lereza.core.solfege;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.toolkat.misc.EnumRepresConstruc;

public class Tone {

	private static EnumRepresConstruc<Tone> enumRepresConstructor = 
			new EnumRepresConstruc<Tone>(Tone.class);

	private NoteLetter note;
	private Accidental alteration;


	//------------ PRIVATE CONSTRUCTOR ------------\\
	
	private Tone(NoteLetter note, Accidental alteration) {
		this.note = note;
		this.alteration = alteration;
	}


	//------------ STATIC CONSTRUCTORS ------------\\

	public static Tone valueOf(NoteLetter note) {
		return new Tone(note, Accidental.NONE);
	}
	
	public static Tone valueOf(NoteLetter note, Accidental alteration) {
		return new Tone(note, alteration);
	}
	
	public static Tone valueOf(String repres) {
		return enumRepresConstructor.newInstance(repres);
	}

	public static Tone thatIsMoreCommonForSemitoneValue(int semitoneValue) {
		List<Tone> possibleTones = withSemitoneValueOf(semitoneValue);
		if (possibleTones.size() == 1) {
			return possibleTones.get(0);
		}
		Tone a = possibleTones.get(0);
		Tone b = possibleTones.get(1);
		
		int posA = 0;
		int posB = 0;;
		
		switch (a.alteration) {
		case FLAT:  posA = CircleOfFifths.positionInOrderOfFlats(a);  break;
		case SHARP: posA = CircleOfFifths.positionInOrderOfSharps(a); break;
		case NONE:  posA = 0; 										  break;
		}
		switch (b.alteration) {
		case FLAT:  posB = CircleOfFifths.positionInOrderOfFlats(b);  break;
		case SHARP: posB = CircleOfFifths.positionInOrderOfSharps(b); break;
		case NONE:  posB = 0; 										  break;
		}

		if (Math.abs(posA) == Math.abs(posB)) {
			if (posA < posB) {
				return a;
			} else {
				return b;
			}
		} else if (Math.abs(posA) < Math.abs(posB)) {
			return a;
		} else {
			return b;
		}
	}

	public static List<Tone> withSemitoneValueOf(int semitoneValue) {
		semitoneValue = Semitones.normalize(semitoneValue);
		List<Tone> possibleTones = new ArrayList<Tone>();
		List<NoteLetter> possibleNotes = NoteLetter.withSemitoneValue(semitoneValue);

		for (NoteLetter possibleNote : possibleNotes) {
			possibleTones.add(fromNoteAndSemitoneValue(possibleNote, semitoneValue));
		}
		return possibleTones;
	}

	public static Tone valueOf(int semitoneValue, int diatonicToneValue) {
		semitoneValue = Semitones.normalize(semitoneValue);
		NoteLetter note = NoteLetter.fromDiatonicToneValue(diatonicToneValue);
		return fromNoteAndSemitoneValue(note, semitoneValue);
	}

	protected static Tone fromNoteAndSemitoneValue(NoteLetter note, int semitoneValue) {
		if (note.canBeAssignedFrom(semitoneValue)) {
			int semitoneAlteration = semitoneValue - note.basicSemitoneValue();
			if (semitoneAlteration == Semitones.IN_OCTAVE - 1) {
				semitoneAlteration -= Semitones.IN_OCTAVE;
			} else if (semitoneAlteration == - (Semitones.IN_OCTAVE - 1)) {
				semitoneAlteration += Semitones.IN_OCTAVE;
			}
			Accidental accidental = Accidental.fromSemitoneAlteration(semitoneAlteration);
			return new Tone(note, accidental);
		}
		throw new IllegalArgumentException("Note \"" + note + "\" can be assigned "
				+ "from semitone value \"" + semitoneValue + "\".");
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
		int finalSemitone = semitoneValue() + interval.semitoneValue();
		int finalDiatonicTone = diatonicToneValue() + interval.diatonicToneValue();
		return valueOf(finalSemitone, finalDiatonicTone);
	}


	//------------ TONE / SEMITONE VALUE ------------\\

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
