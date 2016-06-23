package com.atompacman.lereza.core.piece;

import java.util.Optional;

import com.google.common.collect.ImmutableSet;

public interface HomophonicNoteNode extends PolyphonicNoteNode, NoteNode<HomophonicNoteNode> {

    //
    //  ~  GETTERS  ~  //
    //
    Optional    <HomophonicNoteNode> getPreviousTiedNode();
    ImmutableSet<HomophonicNoteNode> getNodesRightBefore();

    ImmutableSet<HomophonicNoteNode> getNodesRightAfter();

    ImmutableSet<HomophonicNoteNode> getSimultaneousNodes();
}
