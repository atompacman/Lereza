package com.atompacman.lereza.pluggin.builtin.drum;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.lereza.core.piece.Note;
import com.atompacman.lereza.core.piece.NoteStack;

public class PercussionHits {

    public final List<PercussionElement> elems;
    
    PercussionHits(NoteStack stack) {
        this.elems = new ArrayList<>();
        for (Note note : stack.getStartingNotes()) {
            elems.add(PercussionElement.withHexNote((byte) note.getPitch().semitoneValue()));
        }
    }
}
