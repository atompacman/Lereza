package com.atompacman.lereza.core.piece;

import com.atompacman.lereza.core.piece.AbstractNoteStack.NoteStatus;
import com.atompacman.lereza.core.solfege.Dynamic;
import com.atompacman.lereza.core.solfege.Pitch;
import com.atompacman.toolkat.collections.BiDoubleHashMap;
import com.atompacman.toolkat.collections.BiDoubleMap;
import com.atompacman.toolkat.module.AnomalyDescription;
import com.atompacman.toolkat.module.BaseModule;
import com.atompacman.toolkat.module.AnomalyDescription.Severity;

public class NoteStackBuilder extends AbstractNoteStackBuilder<NoteStack> {

    //===================================== INNER TYPES ==========================================\\

    private enum Anomaly {

        @AnomalyDescription (
                name            = "Multiple note entry for same pitch",
                detailsFormat   = "Multiple notes with same pitch were added to the note stack",
                description     = "More than one note has been registered for a specific pitch",
                consequences    = "Ignoring potentially important data", 
                severity        = Severity.MINIMAL)
        MULTIPLE_NOTE_ENTRY_FOR_SAME_PITCH,
    }



    //======================================= FIELDS =============================================\\

    private final BiDoubleMap<NoteStatus, Pitch, Note> notesByStatusByPitch;




    //======================================= METHODS ============================================\\

    //---------------------------------- PUBLIC CONSTRUCTOR --------------------------------------\\

    public NoteStackBuilder() {
        this(null);
    }
    
    public NoteStackBuilder(BaseModule parentModule) {
        super(parentModule);
        this.notesByStatusByPitch = new BiDoubleHashMap<>();
    }


    //----------------------------------------- ADD ----------------------------------------------\\

    protected void addNote(Note note, NoteStatus status) {
        notesByStatusByPitch.put(status, note.getPitch(), note);
    }


    //---------------------------------------- BUILD ---------------------------------------------\\

    protected NoteStack buildComponent(Dynamic dynamic) {
        try {
            return new NoteStack(notesByStatusByPitch, dynamic);
        } catch (IllegalArgumentException e) {
            signal(Anomaly.MULTIPLE_NOTE_ENTRY_FOR_SAME_PITCH);
        }
        return new NoteStack(new BiDoubleHashMap<>(), null);
    }


    //---------------------------------------- RESET ---------------------------------------------\\

    protected void resetChild() {
        notesByStatusByPitch.clear();
    }
}
