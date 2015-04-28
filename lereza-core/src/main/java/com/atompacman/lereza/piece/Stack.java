package com.atompacman.lereza.piece;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.atompacman.lereza.solfege.Dynamic;
import com.atompacman.lereza.solfege.Pitch;

public class Stack<T extends Note> implements PieceComponent {

	//======================================= FIELDS =============================================\\

	private final Map<Pitch, T> startingNotes;
	private final Map<Pitch, T> startedNotes;

	private final Dynamic dynamic;


	//======================================= METHODS ============================================\\
	
	//--------------------------------- PROTECTED CONSTRUCTOR ------------------------------------\\

	protected Stack(Map<Pitch, T> startingNotes, Map<Pitch, T> startedNotes, Dynamic dynamic) {
		this.startingNotes = startingNotes;
		this.startedNotes = startedNotes;
		this.dynamic = dynamic;
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

	Map<Pitch, T> getStartingNoteMap() {
		return startingNotes;
	}
	
	Map<Pitch, T> getStartedNoteMap() {
		return startedNotes;
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
		return !(startingNotes.isEmpty() || startedNotes.isEmpty());
	}
}
