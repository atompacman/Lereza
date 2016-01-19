package com.atompacman.lereza.core.piece;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.atompacman.lereza.core.solfege.Dynamic;
import com.atompacman.lereza.core.solfege.Pitch;
import com.atompacman.toolkat.collections.BiDoubleMap;

public final class MultiNoteStack extends AbstractNoteStack {

    //======================================= FIELDS =============================================\\
    
    protected final Map<Pitch, List<Note>>                     notesByPitch;
    protected final Map<NoteStatus, List<Note>>                notesByStatus;
    protected final BiDoubleMap<NoteStatus, Pitch, List<Note>> notesByStatusByPitch;
    
    
    
    //======================================= METHODS ============================================\\

    //------------------------------------- CONSTRUCTORS -----------------------------------------\\

    protected MultiNoteStack(BiDoubleMap<NoteStatus, Pitch, List<Note>> notesByStatusByPitch,
                             Dynamic                                    dynamic) {
        
        super(dynamic);
        
        this.notesByPitch         = new LinkedHashMap<>();
        this.notesByStatus        = new LinkedHashMap<>();
        this.notesByStatusByPitch = notesByStatusByPitch;
        
        for (Entry<NoteStatus, Map<Pitch, List<Note>>> entry : notesByStatusByPitch.entrySet()) {
            List<Note> list = new LinkedList<>();
            for (List<Note> noteList : entry.getValue().values()) {
                list.addAll(noteList);
            }
            notesByStatus.put(entry.getKey(), list);
            notesByPitch.putAll(entry.getValue());
        }
    }
    
    
    //--------------------------------------- GETTERS --------------------------------------------\\

    public List<Note> getNotesOfPitch(Pitch...pitches) {
        List<Note> notes = new LinkedList<>();
        for (Pitch pitch : pitches) {
            notes.addAll(notesByPitch.get(pitch));
        }
        return notes;   
    }

    public List<Note> getNotesOfStatus(NoteStatus...statusList) {
        List<Note> notes = new LinkedList<>();
        for (NoteStatus status : statusList) {
            notes.addAll(notesByStatus.get(status));
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
            if (!notesByStatus.get(status).isEmpty()) {
                return true;
            }
        }
        return false;
    }
}