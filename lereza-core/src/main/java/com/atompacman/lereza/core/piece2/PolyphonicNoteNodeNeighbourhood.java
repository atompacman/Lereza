package com.atompacman.lereza.core.piece2;

import com.atompacman.lereza.core.piece2.PolyphonicNoteNode.TemporalRelationship;

public interface PolyphonicNoteNodeNeighbourhood {

    //
    // ~ GETTERS ~ //
    //

    NoteNodeSet<PolyphonicNoteNode> getNodes(TemporalRelationship relationship);

    NoteNodeSet<PolyphonicNoteNode> getNodesBeginningBefore();

    NoteNodeSet<PolyphonicNoteNode> getNodesEndingAfter();

    NoteNodeSet<PolyphonicNoteNode> getCompletelyOrPartiallyOverlappingNodes();
}
