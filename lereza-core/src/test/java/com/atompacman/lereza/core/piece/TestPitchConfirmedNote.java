package com.atompacman.lereza.core.piece;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.atompacman.lereza.core.solfege.Pitch;
import com.atompacman.lereza.core.solfege.Value;

public class TestPitchConfirmedNote {

    //====================================== UNIT TESTS ==========================================\\

    @Test
    public void pitchConfirmedNoteIsNotEqualToNote() {
        // Note valueOf(Pitch pitch, Value value)
        Note a = Note.valueOf(Pitch.valueOf("B3"), Value.QUARTER);

        // PitchConfirmedNote valueOf(Note note, Pitch confirmedPitch)
        Note b = PitchConfirmedNote.valueOf(a, Pitch.valueOf("B3"));

        assertTrue(!a.equals(b));
    }
}
