package com.atompacman.lereza.piece.container;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.atompacman.atomLog.Log;
import com.atompacman.lereza.common.solfege.Pitch;
import com.atompacman.lereza.common.solfege.RythmicSignature;
import com.atompacman.lereza.common.solfege.Value;

public class Bar {

	private List<Set<Integer>> notesLayout;
	private List<Note> notes;
	private RythmicSignature rythmicSignature;

	private int barNo;


	//------------ CONSTRUCTORS ------------\\

	public Bar(RythmicSignature rythmicSignature, int barNo) {
		this.notesLayout = new ArrayList<Set<Integer>>();
		for (int i = 0; i < rythmicSignature.timeunitsInABar(); ++i) {
			notesLayout.add(new HashSet<Integer>());
		}
		this.notes = new ArrayList<Note>();
		this.rythmicSignature = rythmicSignature;
		this.barNo = barNo;
	}


	//------------ ADD NOTE ------------\\

	public void add(Pitch pitch, int timeunitPos, int timeunitLength) {
		add(pitch, timeunitPos, timeunitLength, false);
	}

	public void addTiedNote(Pitch pitch, int timeunitPos, int timeunitLength) {
		add(pitch, timeunitPos, timeunitLength, true);
	}

	protected void add(Pitch pitch, int timeunitPos, int timeunitLength, boolean isTiedNote) {
		List<Value> values = splitIntoValues(timeunitPos, timeunitPos + timeunitLength);

		Value value = values.get(0);
		int noteStartPos = timeunitPos;
		add(new Note(pitch, value, isTiedNote), noteStartPos, value.toTimeunit());
		noteStartPos += value.toTimeunit();

		for (int i = 1; i < values.size(); ++i) {
			value = values.get(i);
			add(new Note(pitch, value, true), noteStartPos, value.toTimeunit());
			noteStartPos += value.toTimeunit();
		}
	}

	private void add(Note note, int notePos, int noteLength) {
		int noteIndex = notes.size() + 1;
		notes.add(note);
		notesLayout.get(notePos).add(noteIndex);

		for (int i = 1; i < noteLength; ++i) {
			notesLayout.get(notePos + i).add(-noteIndex);
		}

		if (Log.extra() && Log.print(String.format("Adding note %4s of length %2d at timeunit "
				+ "%4d of bar no.%d.", note.toString(), noteLength, notePos, barNo)));	
	}


	//------------ SPLIT INTO VALUES ------------\\

	protected List<Value> splitIntoValues(int noteStart, int noteEnd) {
		checkTimeunits(noteStart, noteEnd);

		List<Value> values = new ArrayList<Value>();
		int noteLength = noteEnd - noteStart;

		int nbValues = Value.values().length;

		for (int i = 0; i < nbValues; ++i) {
			Value value = Value.values()[nbValues - i - 1];
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

	private void checkTimeunits(int noteStart, int noteEnd) {
		if (noteStart < 0) {
			throw new IllegalArgumentException("Note timeunit position in bar cannot be negative.");
		}
		if (noteStart > rythmicSignature.timeunitsInABar()) {
			throw new IllegalArgumentException("Note timeunit position cannot exceed bar length.");
		}
		if (noteStart > noteEnd) {
			throw new IllegalArgumentException("Note timeunit length in bar cannot be negative.");
		}
		if (noteStart == noteEnd) {
			throw new IllegalArgumentException("Note timeunit length in bar cannot be zero.");
		}
		if (noteEnd > rythmicSignature.timeunitsInABar()) {
			throw new IllegalArgumentException("Note spans more than one bar.");
		}
	}


	//------------ GETTERS ------------\\

	public RythmicSignature getRythmicSignature() {
		return rythmicSignature;
	}

	public int getNo() {
		return barNo;
	}
	
	public Set<Note> getNotesAt(int timeunit) {
		if (timeunit < 0 || timeunit > rythmicSignature.timeunitsInABar()) {
			throw new IllegalArgumentException("Cannot access bar at timeunit \"" + timeunit 
					+ "\": Length of bar is " + rythmicSignature.timeunitsInABar() + ".");
		}
		Set<Note> notes = new HashSet<Note>();
		
		for (Integer index : notesLayout.get(timeunit)) {
			notes.add(this.notes.get(index - 1));
		}
		return notes;
	}
	

	//------------ OBSERVERS ------------\\

	public boolean isEmpty() {
		return notes.isEmpty();
	}
}
