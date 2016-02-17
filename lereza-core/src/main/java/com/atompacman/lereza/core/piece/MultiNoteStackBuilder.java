package com.atompacman.lereza.core.piece;

import java.util.LinkedList;
import java.util.List;

import com.atompacman.lereza.core.piece.AbstractNoteStack.NoteStatus;
import com.atompacman.lereza.core.theory.Dynamic;
import com.atompacman.lereza.core.theory.Pitch;
import com.atompacman.toolkat.collections.BiDoubleHashMap;
import com.atompacman.toolkat.collections.BiDoubleMap;
import com.atompacman.toolkat.module.BaseModule;

public class MultiNoteStackBuilder extends AbstractNoteStackBuilder<MultiNoteStack> {

    //======================================= FIELDS =============================================\\

    private final BiDoubleMap<NoteStatus, Pitch, List<Note>> notesByStatusByPitch;




    //======================================= METHODS ============================================\\

    //------------------------------------- CONSTRUCTORS -----------------------------------------\\

    protected MultiNoteStackBuilder(BaseModule parentModule) {
        super(parentModule);
        this.notesByStatusByPitch = new BiDoubleHashMap<>();
    }


    //----------------------------------------- ADD ----------------------------------------------\\

    protected void addNote(Note note, NoteStatus status) {
        List<Note> notes = notesByStatusByPitch.get(status, note.getPitch());
        if (notes == null) {
            notes = new LinkedList<>();
            notesByStatusByPitch.put(status, note.getPitch(), notes);
        }
        notes.add(note);
    }


    //---------------------------------------- BUILD ---------------------------------------------\\

    protected MultiNoteStack buildComponent(Dynamic dynamic) {
        return new MultiNoteStack(notesByStatusByPitch, dynamic);
    }


    //---------------------------------------- RESET ---------------------------------------------\\

    protected void resetChild() {
        notesByStatusByPitch.clear();
    }
}
