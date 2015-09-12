package com.atompacman.lereza.core.piece;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.atompacman.lereza.core.solfege.Dynamic;
import com.atompacman.lereza.core.solfege.Note;
import com.atompacman.lereza.core.solfege.Pitch;

public class Stack<T extends Note> implements PieceComponent {

    //===================================== INNER TYPES ==========================================\\

    public enum NoteStatus {
        STARTING_AND_TIED,
        STARTING_AND_UNTIED,
        STARTED_AND_TIED,
        STARTED_AND_UNTIED,
    }
    
    
    
    //======================================= FIELDS =============================================\\

    protected final Map<NoteStatus, Set<T>> notesByStatus;
    protected final Map<Pitch, T>           notesByPitch;
    protected final Dynamic                 dynamic;



    //======================================= METHODS ============================================\\

    //--------------------------------- PROTECTED CONSTRUCTOR ------------------------------------\\

    protected Stack(Map<NoteStatus, Set<T>> notesByStatus, Dynamic dynamic) {
        this.notesByStatus = notesByStatus;
        this.notesByPitch  = new HashMap<>();
        this.dynamic       = dynamic;
        
        for (Set<T> noteSet : notesByStatus.values()) {
            for (T note : noteSet) {
                if (notesByPitch.put(note.getPitch(), note) != null) {
                    throw new IllegalArgumentException("Cannot build a note "
                            + "stack with many notes of the same pitch");
                }
            }
        }
    }


    //--------------------------------------- GETTERS --------------------------------------------\\

    public Set<T> getStartingUntiedNotes() {
        return getNotesOfStatus(NoteStatus.STARTING_AND_UNTIED);
    }
    
    public Set<T> getStartingNotes() {
        return getNotesOfStatus(NoteStatus.STARTING_AND_UNTIED, NoteStatus.STARTING_AND_TIED);
    }

    public Set<T> getStartedNotes() {
        return getNotesOfStatus(NoteStatus.STARTED_AND_UNTIED, NoteStatus.STARTED_AND_TIED);
    }

    public Set<T> getPlayingNotes() {
        return getNotesOfStatus(NoteStatus.STARTED_AND_TIED,  NoteStatus.STARTED_AND_UNTIED, 
                                NoteStatus.STARTING_AND_TIED, NoteStatus.STARTING_AND_UNTIED);
    }

    private Set<T> getNotesOfStatus(NoteStatus...statusList) {
        Set<T> notes = new LinkedHashSet<>();
        for (NoteStatus status : statusList) {
            notes.addAll(notesByStatus.get(status));
        }
        return notes;
    }
    
    public T getNote(Pitch pitch) {
        T note = notesByPitch.get(pitch);
        if (note == null) {
            throw new IllegalArgumentException("Stack does not contain "
                    + "a note of pitch \"" + pitch.toString() + "\".");
        }
        return note;
    }

    public Dynamic getDynamic() {
        if (dynamic == null) {
            throw new IllegalStateException("Cannot get dynamic of a stack with no starting notes");
        }
        return dynamic;
    }


    //---------------------------------------- COUNT ---------------------------------------------\\

    public int countStartingUntiedNotes() {
        return getStartingUntiedNotes().size();
    }
    
    public int countStartingNotes() {
        return getStartingNotes().size();
    }

    public int countStartedNotes() {
        return getStartedNotes().size();
    }

    public int countPlayingNotes() {
        return getPlayingNotes().size();
    }

    
    //---------------------------------------- STATE ---------------------------------------------\\

    public boolean containsNoteOfPitch(Pitch pitch) {
        return notesByPitch.containsKey(pitch);
    }

    public boolean hasStartingUntiedNotes() {
        return getStartingUntiedNotes().size() != 0;
    }
    
    public boolean hasStartingNotes() {
        return getStartingNotes().size() != 0;
    }

    public boolean hasStartedNotes() {
        return getStartedNotes().size() != 0;
    }

    public boolean hasPlayingNotes() {
        return getPlayingNotes().size() != 0;
    }
    
    
    //-------------------------------------- TO STRING -------------------------------------------\\

    public String toStaccato() {
        Set<T> startingNotes = getStartingNotes();
        if (startingNotes.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Iterator<T> it = startingNotes.iterator();
        sb.append(it.next().toStaccato((byte) dynamic.getVelocity()));
        while (it.hasNext()) {
            sb.append('+').append(it.next().toStaccato((byte) dynamic.getVelocity()));
        }
        return sb.toString();
    }
}
