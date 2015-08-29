package com.atompacman.lereza.core.piece;

import java.util.List;

import com.atompacman.lereza.core.solfege.Pitch;
import com.atompacman.lereza.core.solfege.Value;

public class PitchConfirmedNote extends Note {

    //======================================= METHODS ============================================\\

    //------------------------------ PUBLIC STATIC CONSTRUCTOR -----------------------------------\\

    public static PitchConfirmedNote valueOf(Note note, Pitch confirmedPitch) {
        return new PitchConfirmedNote(confirmedPitch, note.getValues());
    }


    //--------------------------------- PROTECTED CONSTRUCTOR ------------------------------------\\

    private PitchConfirmedNote(Pitch pitch, List<Value> values) {
        super(pitch, values);
    }
}
