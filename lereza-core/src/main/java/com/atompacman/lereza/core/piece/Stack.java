package com.atompacman.lereza.core.piece;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.atompacman.lereza.core.solfege.Dynamic;
import com.atompacman.lereza.core.solfege.Pitch;

public class Stack<T extends Note> implements PieceComponent {

    //======================================= FIELDS =============================================\\

    protected final Map<Pitch, T> startingNotes;
    protected final Map<Pitch, T> startedNotes;

    protected final Dynamic dynamic;


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

    public int getNumPlayingNotes() {
        return getNumStartingNotes() + getNumStartedNotes();
    }

    public Dynamic getDynamic() {
        if (dynamic == null) {
            throw new IllegalStateException("Cannot get dynamic of a stack with no starting notes");
        }
        return dynamic;
    }


    //---------------------------------------- STATE ---------------------------------------------\\

    public boolean containsNoteOfPitch(Pitch pitch) {
        return startingNotes.containsKey(pitch) || startedNotes.containsKey(pitch);
    }

    public boolean hasStartingNotes() {
        return !startingNotes.isEmpty();
    }

    public boolean hasStartedNotes() {
        return !startedNotes.isEmpty();
    }

    public boolean hasPlayingNotes() {
        return hasStartingNotes() || hasStartedNotes();
    }
}
