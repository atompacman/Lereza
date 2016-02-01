package com.atompacman.lereza.core.piece;

import java.util.Iterator;
import java.util.List;

import com.atompacman.lereza.core.solfege.Dynamic;
import com.atompacman.lereza.core.solfege.Pitch;

public abstract class AbstractNoteStack implements PieceComponent {

    //===================================== INNER TYPES ==========================================\\

    public enum NoteStatus {
        STARTING_AND_TIED,
        STARTING_AND_UNTIED,
        STARTED_AND_TIED,
        STARTED_AND_UNTIED,
    }
    
    
    
    //======================================= FIELDS =============================================\\
    
    protected final Dynamic dynamic;
    

    
    //=================================== ABSTRACT METHODS =======================================\\

    //--------------------------------------- GETTERS --------------------------------------------\\

    public abstract List<Note> getNotesOfStatus(NoteStatus...statusList);
    
    
    //---------------------------------------- STATE ---------------------------------------------\\

    public abstract boolean hasNoteOfPitch(Pitch...pitches);
    public abstract boolean hasNotesOfStatus(NoteStatus...statusList);

    
    
    //======================================= METHODS ============================================\\

    //------------------------------------- CONSTRUCTORS -----------------------------------------\\

    protected AbstractNoteStack(Dynamic dynamic) {
        this.dynamic = dynamic;
    }
    
    
    //--------------------------------------- GETTERS --------------------------------------------\\

    public List<Note> getPlayingNotes() {
        return getNotesOfStatus(NoteStatus.STARTED_AND_TIED,  NoteStatus.STARTED_AND_UNTIED, 
                                NoteStatus.STARTING_AND_TIED, NoteStatus.STARTING_AND_UNTIED);
    }

    public List<Note> getStartingNotes() {
        return getNotesOfStatus(NoteStatus.STARTING_AND_UNTIED, NoteStatus.STARTING_AND_TIED);
    }
    
    public Dynamic getDynamic() {
        if (dynamic == null) {
            throw new IllegalStateException("Cannot get dynamic of a stack with no starting notes");
        }
        return dynamic;
    }
    
    
    //---------------------------------------- STATE ---------------------------------------------\\

    public boolean hasStartingNotes() {
        return hasNotesOfStatus(NoteStatus.STARTING_AND_TIED, NoteStatus.STARTING_AND_UNTIED);
    }

    public boolean hasStartedNotes() {
        return hasNotesOfStatus(NoteStatus.STARTED_AND_TIED, NoteStatus.STARTED_AND_UNTIED);
    }

    public boolean isEmpty() {
        return dynamic == null;
    }

    
    //------------------------------------ SERIALIZATION -----------------------------------------\\

    public String toStaccato() {
        List<Note> startingNotes = getStartingNotes();
        if (startingNotes.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Iterator<Note> it = startingNotes.iterator();
        sb.append(it.next().toStaccato((byte) dynamic.getVelocity()));
        while (it.hasNext()) {
            sb.append('+').append(it.next().toStaccato((byte) dynamic.getVelocity()));
        }
        return sb.toString();
    }
}
