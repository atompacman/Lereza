package com.atompacman.lereza.core.piece2;

import javax.annotation.Nullable;

import com.atompacman.lereza.core.theory.Note;
import com.atompacman.lereza.core.theory.RythmnValue;

public class HomophonicNoteNode extends PolyphonicNoteNode {

    //
    //  ~  INIT  ~  //
    //

    protected HomophonicNoteNode(Note note) {
        super(note,
              new AcyclicGraphNoteNodeNeighbourhood(),
              new AcyclicGraphNoteNodeNeighbourhood());
    }
    
    protected HomophonicNoteNode(RythmnValue value) {
        super(value, 
              new AcyclicGraphNoteNodeNeighbourhood(),
              new AcyclicGraphNoteNodeNeighbourhood());
    }
    
    protected HomophonicNoteNode(Note                            note,
                                 HomophonicNoteNodeNeighbourhood neighbourhoodSeparated,
                                 HomophonicNoteNodeNeighbourhood neighbourhoodMerged) {
        super(note,
              neighbourhoodSeparated, 
              neighbourhoodMerged);
    }
    
    protected HomophonicNoteNode(RythmnValue                     value,
                                 HomophonicNoteNodeNeighbourhood neighbourhoodSeparated,
                                 HomophonicNoteNodeNeighbourhood neighbourhoodMerged) {
        super(value, 
              neighbourhoodSeparated, 
              neighbourhoodMerged);
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //

    @Override
    public @Nullable HomophonicNoteNode getPreviousTiedNode() {
        return (HomophonicNoteNode) prevTiedNote.orElse(null);
    }

    @Override
    public @Nullable HomophonicNoteNode getNextTiedNode() {
        return (HomophonicNoteNode) nextTiedNote.orElse(null);
    }

    @Override
    public HomophonicNoteNodeNeighbourhood getNoteNeighbourhood() { 
        return getNoteNeighbourhood(TiedNoteStatus.defaultStatus); 
    }
    
    @Override
    public HomophonicNoteNodeNeighbourhood getNoteNeighbourhood(TiedNoteStatus status) {
        return (HomophonicNoteNodeNeighbourhood) super.getNoteNeighbourhood(status);
    }
}
