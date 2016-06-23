package com.atompacman.lereza.core.piece;

import java.util.Optional;

public interface MonophonicNoteNode extends HomophonicNoteNode {
    
    //
    //  ~  GETTERS  ~  //
    //
    
    Optional<MonophonicNoteNode> getPreviousNode();

    Optional<MonophonicNoteNode> getNextNode();
}
