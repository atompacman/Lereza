package com.atompacman.lereza.core.piece2;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Set;

import com.atompacman.toolkat.annotations.Implement;

final class GraphBarSlice extends AbstractBarSlice {

    //
    //  ~  FIELDS  ~  //
    //

    private NoteNodeSet<PolyphonicNoteNode> beginningNoteNodes;
    private NoteNodeSet<PolyphonicNoteNode> playingNoteNodes;


    //
    //  ~  INIT  ~  //
    //

    GraphBarSlice(Set<PolyphonicNoteNode> beginningNoteNodes, 
                  Set<PolyphonicNoteNode> playingNoteNodes) {

        checkArgument(playingNoteNodes.containsAll(beginningNoteNodes));
        
        this.beginningNoteNodes = NoteNodeSet.of(beginningNoteNodes);
        this.playingNoteNodes   = NoteNodeSet.of(playingNoteNodes);
        
        initStatus();
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //

    @Implement
    public NoteNodeSet<PolyphonicNoteNode> getBeginningNoteNodes() {
        return beginningNoteNodes;
    }
    
    @Implement
    public NoteNodeSet<PolyphonicNoteNode> getPlayingNoteNodes() {
        return playingNoteNodes;
    }
}
