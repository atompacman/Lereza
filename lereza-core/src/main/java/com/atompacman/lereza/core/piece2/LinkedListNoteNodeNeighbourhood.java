package com.atompacman.lereza.core.piece2;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Optional;

import com.atompacman.lereza.core.piece2.PolyphonicNoteNode.TemporalRelationship;
import com.atompacman.toolkat.annotations.Implement;

final class LinkedListNoteNodeNeighbourhood implements MonophonicNoteNodeNeighbourhood {

    //
    //  ~  FIELDS  ~  //
    //

    private Optional<MonophonicNoteNode> prevNode; 
    private Optional<MonophonicNoteNode> nextNode;


    //
    //  ~  INIT  ~  //
    //

    LinkedListNoteNodeNeighbourhood() {
        this.prevNode = Optional.empty();
        this.nextNode = Optional.empty();
    }


    //
    //  ~  CONNECT  ~  //
    //

    void connectToPrev(MonophonicNoteNode prev) {
        checkArgument(!prevNode.isPresent());
        prevNode = Optional.of(prev);
    }
    
    void connectToNext(MonophonicNoteNode next) {
        checkArgument(!nextNode.isPresent());
        nextNode = Optional.of(next);
    }


    //
    //  ~  GETTERS  ~  //
    //
    
    @Implement
    public NoteNodeSet<PolyphonicNoteNode> getNodes(TemporalRelationship relationship) {
        if        (relationship == TemporalRelationship.RIGHT_AFTER && nextNode.isPresent()) {
            return NoteNodeSet.of(nextNode.get());
        } else if (relationship == TemporalRelationship.RIGHT_BEFORE && prevNode.isPresent()) {
            return NoteNodeSet.of(prevNode.get());
        } else {
            return NoteNodeSet.of();
        }
    }

    @Implement
    public NoteNodeSet<PolyphonicNoteNode> getNodesBeginningBefore() {
        return prevNode.isPresent() ? NoteNodeSet.of(prevNode.get()) : NoteNodeSet.of();
    }

    @Implement
    public NoteNodeSet<PolyphonicNoteNode> getNodesEndingAfter() {
        return nextNode.isPresent() ? NoteNodeSet.of(nextNode.get()) : NoteNodeSet.of();
    }

    @Implement
    public NoteNodeSet<PolyphonicNoteNode> getCompletelyOrPartiallyOverlappingNodes() {
        return NoteNodeSet.of();
    }

    @Implement
    public NoteNodeSet<HomophonicNoteNode> getNodesRightBefore() {
        return prevNode.isPresent() ? NoteNodeSet.of(prevNode.get()) : NoteNodeSet.of();
    }

    @Implement
    public NoteNodeSet<HomophonicNoteNode> getNodesRightAfter() {
        return nextNode.isPresent() ? NoteNodeSet.of(nextNode.get()) : NoteNodeSet.of();
    }

    @Implement
    public NoteNodeSet<HomophonicNoteNode> getSimultaneousNodes() {
        return NoteNodeSet.of();
    }
    
    @Implement
    public Optional<MonophonicNoteNode> getPreviousNode() {
        return prevNode;
    }

    @Implement
    public Optional<MonophonicNoteNode> getNextNode() {
        return nextNode;
    }
}
