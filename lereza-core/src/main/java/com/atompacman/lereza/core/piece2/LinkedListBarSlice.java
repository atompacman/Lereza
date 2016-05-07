package com.atompacman.lereza.core.piece2;

import com.atompacman.toolkat.annotations.Implement;

final class LinkedListBarSlice extends AbstractBarSlice implements MonophonicBarSlice {

    //
    //  ~  FIELDS  ~  //
    //

    private final MonophonicNoteNode node;
    private final boolean            isBeginning;


    //
    //  ~  INIT  ~  //
    //

    LinkedListBarSlice(MonophonicNoteNode node, boolean isBeginning) {
        this.node        = node;
        this.isBeginning = isBeginning;
        
        initStatus();
    }


    //
    //  ~  GETTERS  ~  //
    //

    @Implement
    public NoteNodeSet<PolyphonicNoteNode> getBeginningNoteNodes() {
        return isBeginning ? NoteNodeSet.of(node) : NoteNodeSet.of();
    }

    @Implement
    public NoteNodeSet<PolyphonicNoteNode> getPlayingNoteNodes() {
        return isRest() ? NoteNodeSet.of() : NoteNodeSet.of(node);
    }
    
    @Implement
    public NoteNodeSet<HomophonicNoteNode> getNoteNodes() {
        return NoteNodeSet.of(node);
    }
    
    @Implement
    public MonophonicNoteNode getNoteNode() {
        return node;
    }
    
    
    //
    //  ~  STATUS  ~  //
    //

    @Implement
    public boolean areNotesBeginning() {
        return isBeginning;
    }
}
