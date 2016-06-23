package com.atompacman.lereza.core.piece;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Optional;

import com.atompacman.lereza.core.theory.Note;
import com.atompacman.toolkat.annotations.Implement;
import com.google.common.collect.ImmutableSet;

final class LinkedListNoteNode extends AbstractNoteNode<LinkedListNoteNode> implements MonophonicNoteNode {

    //
    //  ~  FIELDS  ~  //
    //

    private Optional<LinkedListNoteNode> prevNode; 
    private Optional<LinkedListNoteNode> nextNode;


    //
    //  ~  INIT  ~  //
    //

    LinkedListNoteNode(Note note) {
        super(note);
        this.prevNode = Optional.empty();
        this.nextNode = Optional.empty();
    }


    //
    //  ~  SETTERS  ~  //
    //

    void connectTo(TemporalRelationship relationship, LinkedListNoteNode node) {
        if        (relationship == TemporalRelationship.RIGHT_AFTER) {
            checkArgument(!nextNode.isPresent());
            nextNode = Optional.of(node);
        } else if (relationship == TemporalRelationship.RIGHT_BEFORE) {
            checkArgument(!prevNode.isPresent());
            prevNode = Optional.of(node);
        } else {
            throw new IllegalArgumentException();
        }
    }


    //
    //  ~  GETTERS  ~  //
    //

    @Implement
    @SuppressWarnings("unchecked")
    public ImmutableSet<LinkedListNoteNode> getNodes(TemporalRelationship relationship) {
        if        (relationship == TemporalRelationship.RIGHT_AFTER && nextNode.isPresent()) {
            return ImmutableSet.of(nextNode.get());
        } else if (relationship == TemporalRelationship.RIGHT_BEFORE && prevNode.isPresent()) {
            return ImmutableSet.of(prevNode.get());
        } else {
            return ImmutableSet.of();
        }
    }

    @Implement
    @SuppressWarnings("unchecked")
    public ImmutableSet<LinkedListNoteNode> getNodesBeginningBefore() {
        return prevNode.isPresent() ? ImmutableSet.of(prevNode.get()) : ImmutableSet.of();
    }

    @Implement
    @SuppressWarnings("unchecked")
    public ImmutableSet<LinkedListNoteNode> getNodesEndingAfter() {
        return nextNode.isPresent() ? ImmutableSet.of(prevNode.get()) : ImmutableSet.of();
    }

    @Implement
    @SuppressWarnings("unchecked")
    public ImmutableSet<LinkedListNoteNode> getCompletelyOrPartiallyOverlappingNodes() {
        return ImmutableSet.of();
    }

    @Implement
    @SuppressWarnings("unchecked")
    public Optional<LinkedListNoteNode> getPreviousNode() {
        return prevNode;
    }

    @Implement
    @SuppressWarnings("unchecked")
    public Optional<LinkedListNoteNode> getNextNode() {
        return prevNode;
    }
}
