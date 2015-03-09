package com.atompacman.lereza.piece.newcontainer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.atompacman.lereza.solfege.Dynamic;
import com.atompacman.lereza.solfege.Pitch;

public class NoteStack<T extends BarNote> {

	//======================================= FIELDS =============================================\\

	private final Map<Pitch, T> startingNotes;
	private final Map<Pitch, T> startedNotes;

	private final Dynamic dynamic;


	//======================================= METHODS ============================================\\

	//---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\

	NoteStack(Dynamic dynamic) {
		this.startingNotes = new HashMap<>();
		this.startedNotes = new HashMap<>();
		
		this.dynamic = dynamic;
	}


	//----------------------------------------- ADD ----------------------------------------------\\

	void addStartingNote(T note) {
		addNoteTo(note, startingNotes, "starting");
	}

	void addStartedNote(T note) {
		addNoteTo(note, startedNotes, "started");
	}

	private void addNoteTo(T note, Map<Pitch, T> map, String mapName) {
		Pitch pitch = note.getPitch();
		if (containsNoteOfPitch(note.getPitch())) {
			throw new IllegalArgumentException("A note of pitch " + pitch.toString() + 
					" was already added to " + mapName + " note stack.");
		}
		map.put(pitch, note);
	}


	//--------------------------------------- GETTERS --------------------------------------------\\

	public Set<T> getStartingNotes() {
		return new HashSet<>(startingNotes.values());
	}

	public Set<T> getStartedNotes() {
		return new HashSet<>(startedNotes.values());
	}

	public Set<T> getPlayingNotes() {
		Set<T> notes = getStartingNotes();
		notes.addAll(getStartedNotes());
		return notes;
	}

	public T getNote(Pitch pitch) {
		T note = startingNotes.get(pitch);
		if (note != null) {
			return note;
		}
		note = startedNotes.get(pitch);
		if (note != null) {
			return note;
		}
		throw new IllegalArgumentException("Does not contain a "
				+ "note of pitch \"" + pitch.toString() + "\".");

	}

	public int getNumStartingNotes() {
		return startingNotes.size();
	}

	public int getNumStartedNotes() {
		return startedNotes.size();
	}
	
	public Dynamic getDynamic() {
		return dynamic;
	}


	//---------------------------------------- STATE ---------------------------------------------\\

	public boolean containsNoteOfPitch(Pitch pitch) {
		return startingNotes.containsKey(pitch) || startedNotes.containsKey(pitch);
	}

	public boolean hasStartingNotes() {
		return startingNotes.isEmpty();
	}

	public boolean hasStartedNotes() {
		return startedNotes.isEmpty();
	}
	
	public boolean hasPlayingNotes() {
		return !(startingNotes.isEmpty() && !startedNotes.isEmpty());
	}
}
