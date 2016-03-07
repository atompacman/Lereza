package com.atompacman.lereza.core.piece2;

import java.util.Optional;

public interface MonophonicNoteNode extends HomophonicNoteNode {
    
    //
    //  ~  GETTERS  ~  //
    //
    
    Optional<MonophonicNoteNode> getPreviousNode();

    Optional<MonophonicNoteNode> getNextNode();
}
