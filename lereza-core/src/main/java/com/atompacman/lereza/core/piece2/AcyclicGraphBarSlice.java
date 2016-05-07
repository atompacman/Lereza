package com.atompacman.lereza.core.piece2;

import java.util.Set;

import com.atompacman.toolkat.annotations.Implement;

final class AcyclicGraphBarSlice extends AbstractBarSlice implements HomophonicBarSlice {

    //
    //  ~  FIELDS  ~  //
    //

    private final NoteNodeSet<HomophonicNoteNode> nodes;
    private final boolean                         areBeginning;


    //
    //  ~  INIT  ~  //
    //

    AcyclicGraphBarSlice(Set<HomophonicNoteNode> node, boolean isBeginning) {
        this.nodes        = NoteNodeSet.of(node);
        this.areBeginning = isBeginning;
        
        initStatus();
    }
    

    //
    //  ~  GETTERS  ~  //
    //

    @Implement
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public NoteNodeSet<PolyphonicNoteNode> getBeginningNoteNodes() {
        return areBeginning ? (NoteNodeSet<PolyphonicNoteNode>)(NoteNodeSet) nodes:NoteNodeSet.of();
    }

    @Implement
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public NoteNodeSet<PolyphonicNoteNode> getPlayingNoteNodes() {
        return isRest() ? NoteNodeSet.of() : (NoteNodeSet<PolyphonicNoteNode>)(NoteNodeSet) nodes;
    }
    
    @Implement
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public NoteNodeSet<HomophonicNoteNode> getNoteNodes() {
        return (NoteNodeSet<HomophonicNoteNode>)(NoteNodeSet) nodes;
    }
    
    
    //
    //  ~  STATUs  ~  //
    //

    @Implement
    public boolean areNotesBeginning() {
        return areBeginning;
    }
}
