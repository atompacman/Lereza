package com.atompacman.lereza.core.piece2;

import javax.annotation.Nullable;

import com.atompacman.lereza.core.theory.Pitch;
import com.atompacman.lereza.core.theory.Value;

public final class SequenceNoteNode extends NoteNode<SequenceNoteNode> {
    
    //
    //  ~  FIELDS  ~  //
    //

    private @Nullable SequenceNoteNode       prevNote; 
    private @Nullable SequenceNoteNode       nextNote; 
    private @Nullable SequentialRestBarSlice nextRestSlice; 
    
    
    //
    //  ~  INIT  ~  //
    //

    SequenceNoteNode(Pitch pitch, Value value, boolean hasPrevTiedNote, boolean hasNextTiedNote) {
        super(note);
    }
}
