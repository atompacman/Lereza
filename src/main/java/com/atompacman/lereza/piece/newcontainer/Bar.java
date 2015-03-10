package com.atompacman.lereza.piece.newcontainer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.atompacman.atomlog.Log;
import com.atompacman.lereza.solfege.Dynamic;
import com.atompacman.lereza.solfege.Pitch;
import com.atompacman.lereza.solfege.RythmicSignature;
import com.atompacman.lereza.solfege.Value;

public final class Bar<T extends NoteStack<? extends BarNote>> {

	//======================================= FIELDS =============================================\\

	private final List<NoteStack<? extends BarNote>> noteStacks;
	private final RythmicSignature rythmicSign;


	
	//======================================= METHODS ============================================\\

	//------------------------------ PACKAGE STATIC CONSTRUCTOR ----------------------------------\\

	static <T extends BarNote> Bar<T> valueOf(RythmicSignature rythmicSign, Class<T> noteClass) {
		return new Bar<T>(rythmicSign);
	}
	
	
	//---------------------------------- PRIVATE CONSTRUCTOR -------------------------------------\\

	private Bar(RythmicSignature rythmicSign) {
		this.noteStacks = new ArrayList<>();
		for (int i = 0; i < rythmicSign.timeunitsInABar(); ++i) {
			noteStacks.add(new HashSet<T>());
		}
		this.rythmicSign = rythmicSign;
	}


	//----------------------------------------- ADD ----------------------------------------------\\
	
	@SuppressWarnings("unchecked")
	void add(Pitch pitch, Dynamic dynamic, int begTU, int lenTU, boolean isTiedNote) {
		List<Value> values = splitIntoValues(begTU, begTU + lenTU);

		int noteStartPos = 0;
		
		for (int i = 0; i < values.size(); ++i) {
			Value value = values.get(0);
			T note = (T) BarNote.valueOf(pitch, value, dynamic, isTiedNote);
			for (int j = noteStartPos; j < value.toTimeunit(); ++j) {
				notes.get(j).add(note);
			}
			if (Log.extra() && Log.print(String.format("Adding note %4s of length %2d at timeunit "
					+ "%4d.", note.toCompleteString(), value.toTimeunit(), noteStartPos)));	
			
			noteStartPos += value.toTimeunit();
			isTiedNote = true;
		}
	}

	private static List<Value> splitIntoValues(int noteStart, int noteEnd) {
		List<Value> values = new ArrayList<>();
		int noteLength = noteEnd - noteStart;

		for (int i = Value.values().length - 1; i >= 0; --i) {
			Value value = Value.values()[i];
			int valueLength = value.toTimeunit();

			if (valueLength > noteLength) {
				continue;
			}
			int valueStart = 0;
			while (valueStart < noteStart) {
				valueStart += valueLength;
			}
			int valueEnd = valueStart + valueLength;

			if (valueEnd > noteEnd) {
				continue;
			}
			if (noteStart < valueStart) {
				values.addAll(splitIntoValues(noteStart, valueStart));
			}
			values.add(value);

			if (valueEnd < noteEnd) {
				values.addAll(splitIntoValues(valueEnd, noteEnd));
			}
			break;
		}

		return values;
	}


	//--------------------------------------- GETTERS --------------------------------------------\\

	public RythmicSignature getRythmicSignature() {
		return rythmicSign;
	}
	
	public Set<T> getNotesPlayingAt(int timeunit) {
		return getNotes(timeunit, false);
	}
	
	public Set<T> getNotesStartingAt(int timeunit) {
		return getNotes(timeunit, true);
	}
	
	private Set<T> getNotes(int timeunit, boolean startingOnly) {
		if (timeunit < 0 || timeunit > rythmicSign.timeunitsInABar()) {
			throw new IllegalArgumentException("Cannot access bar at timeunit \"" + timeunit 
					+ "\": Length of bar is " + rythmicSign.timeunitsInABar() + ".");
		}
		Set<T> n = new HashSet<>();
		
		for (T note : notes.get(timeunit)) {
			if (!note.isTied() || !startingOnly) {
				n.add(note);
			}
		}
		return n;
	}
	

	//---------------------------------------- STATE ---------------------------------------------\\

	public boolean isEmpty() {
		return notes.isEmpty();
	}

	public int getNumStartingNotes() {
		int num = 0;
		for (int i = 0; i < notes.size(); ++i) {
			num += getNotes(i, true).size();
		}
		return num;
	}
}
