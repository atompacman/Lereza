package com.atompacman.lereza.core.piece2;

public interface HomophonicNoteNodeNeighbourhood extends PolyphonicNoteNodeNeighbourhood {

    //
    //  ~  GETTERS  ~  //
    //

    NoteNodeSet<HomophonicNoteNode> getNodesRightBefore();
    
    NoteNodeSet<HomophonicNoteNode> getNodesRightAfter();
    
    NoteNodeSet<HomophonicNoteNode> getSimultaneousNodes();
}
