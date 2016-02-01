package com.atompacman.lereza.core.piece;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.atompacman.lereza.core.solfege.Dynamic;
import com.atompacman.lereza.core.solfege.Pitch;
import com.atompacman.toolkat.collections.BiDoubleMap;

public final class NoteStack extends AbstractNoteStack {

    //======================================= FIELDS =============================================\\
    
    protected final Map<Pitch, Note>                     notesByPitch;
    protected final BiDoubleMap<NoteStatus, Pitch, Note> notesByStatusByPitch;
    
    
    
    //======================================= METHODS ============================================\\

    //------------------------------------- CONSTRUCTORS -----------------------------------------\\

    protected NoteStack(BiDoubleMap<NoteStatus,Pitch,Note> notesByStatusByPitch, Dynamic dynamic) {
        super(dynamic);
        
        this.notesByPitch         = new LinkedHashMap<>();
        this.notesByStatusByPitch = notesByStatusByPitch;
        
        for (Map<Pitch, Note> submap : notesByStatusByPitch.getSubMaps()) {
            for (Note note : submap.values()) {
                if (notesByPitch.put(note.getPitch(), note) != null) {
                    throw new IllegalArgumentException("Cannot have multiple note for same pitch");
                }
            }
        }
    }
    
    
    //--------------------------------------- GETTERS --------------------------------------------\\
    
    public Note getNoteOfPitch(Pitch pitch) {
        return notesByPitch.get(pitch);
    }

    public List<Note> getNotesOfStatus(NoteStatus...statusList) {
        List<Note> notes = new LinkedList<>(); 
        for (NoteStatus status : statusList) {
            notes.addAll(notesByStatusByPitch.getSubMap(status).values());
        }
        return notes;
    }
    
    
    //---------------------------------------- STATE ---------------------------------------------\\

    public boolean hasNoteOfPitch(Pitch...pitches) {
        for (Pitch pitch : pitches) {
            if (notesByPitch.containsKey(pitch)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasNotesOfStatus(NoteStatus...statusList) {
        for (NoteStatus status : statusList) {
            if (!notesByStatusByPitch.getSubMap(status).isEmpty()) {
                return true;
            }
        }
        return false;
    }
}