package com.atompacman.lereza.piece.newcontainer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.atompacman.lereza.solfege.Dynamic;
import com.atompacman.lereza.solfege.Pitch;

public class NoteStackBuilder<T extends BarNote> {

	//======================================= FIELDS =============================================\\

	private final Map<Pitch, T> startingNotes;
	private final Map<Pitch, T> startedNotes;
	


	//======================================= METHODS ============================================\\

	//---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\

	NoteStackBuilder() {
		this.startingNotes = new HashMap<>();
		this.startedNotes = new HashMap<>();
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
	
	
	//---------------------------------------- BUILD ---------------------------------------------\\

	public NoteStack<T> build() {
		return null;
	}
}
