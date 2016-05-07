package com.atompacman.lereza.core.piece2;

import java.util.Optional;

public interface MonophonicNoteNodeNeighbourhood extends HomophonicNoteNodeNeighbourhood {

    //
    //  ~  GETTERS  ~  //
    //

    Optional<MonophonicNoteNode> getPreviousNode();

    Optional<MonophonicNoteNode> getNextNode();
}
