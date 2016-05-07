package com.atompacman.lereza.core.piece2;

import javax.annotation.Nullable;

import com.atompacman.lereza.core.theory.Note;
import com.atompacman.lereza.core.theory.RythmnValue;

public final class MonophonicNoteNode extends HomophonicNoteNode {
    
    //
    //  ~  INIT  ~  //
    //

    protected MonophonicNoteNode(Note note) {
        super(note, new LinkedListNoteNodeNeighbourhood(), new LinkedListNoteNodeNeighbourhood());
    }
    
    protected MonophonicNoteNode(RythmnValue value) {
        super(value, new LinkedListNoteNodeNeighbourhood(), new LinkedListNoteNodeNeighbourhood());
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //

    @Override
    public @Nullable MonophonicNoteNode getPreviousTiedNode() {
        return (MonophonicNoteNode) prevTiedNote.orElse(null);
    }

    @Override
    public @Nullable MonophonicNoteNode getNextTiedNode() {
        return (MonophonicNoteNode) nextTiedNote.orElse(null);
    }

    @Override
    public MonophonicNoteNodeNeighbourhood getNoteNeighbourhood() { 
        return getNoteNeighbourhood(TiedNoteStatus.defaultStatus); 
    }
    
    @Override
    public MonophonicNoteNodeNeighbourhood getNoteNeighbourhood(TiedNoteStatus status) {
        return (MonophonicNoteNodeNeighbourhood) super.getNoteNeighbourhood(status);
    }
}
