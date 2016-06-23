package com.atompacman.lereza.core.piece;

import java.util.Optional;

import com.atompacman.lereza.core.piece.AbstractNoteNode.TemporalRelationship;
import com.atompacman.lereza.core.theory.Note;
import com.atompacman.lereza.core.theory.RythmnValue;
import com.google.common.collect.ImmutableSet;

public interface NoteNode<T extends NoteNode<T>> {

    //
    //  ~  GETTERS  ~  //
    //
    
    Optional    <Note>               getNote();

    RythmnValue                      getRythmnValue();
    
    Optional    <T> getPreviousTiedNode();
    
    Optional    <T> getNextTiedNode();
    
    ImmutableSet<T> getNodes(TemporalRelationship relationship);

    ImmutableSet<T> getNodesBeginningBefore();

    ImmutableSet<T> getNodesEndingAfter();

    ImmutableSet<T> getCompletelyOrPartiallyOverlappingNodes();

    
    //
    //  ~  STATE  ~  //
    //

    int     totalTiedNoteTimeunitLength();
    
    boolean isTied();
    
    boolean isRest();
    

    //
    //  ~  SERIALIZATION  ~  //
    //
    
    String toStaccato();
}
