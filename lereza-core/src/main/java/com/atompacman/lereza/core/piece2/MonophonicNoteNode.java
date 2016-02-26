package com.atompacman.lereza.core.piece2;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Optional;

import com.atompacman.lereza.core.theory.Note;
import com.atompacman.lereza.core.theory.Pitch;
import com.atompacman.lereza.core.theory.RythmnValue;
import com.atompacman.toolkat.annotations.Implement;
import com.google.common.collect.ImmutableSet;

public final class MonophonicNoteNode extends NoteNode<MonophonicNoteNode> {
    
    //
    //  ~  FIELDS  ~  //
    //

    private Optional<MonophonicNoteNode> prevNode; 
    private Optional<MonophonicNoteNode> nextNode;
    
    
    //
    //  ~  INIT  ~  //
    //

    MonophonicNoteNode(Pitch pitch, RythmnValue value) {
        super(Note.of(pitch, value));
        this.prevNode = Optional.empty();
        this.nextNode = Optional.empty();
    }


    //
    //  ~  SETTERS  ~  //
    //
    
    void connectTo(TemporalRelationship relationship, MonophonicNoteNode node) {
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
    public ImmutableSet<MonophonicNoteNode> getNodes(TemporalRelationship relationship) {
        if        (relationship == TemporalRelationship.RIGHT_AFTER && nextNode.isPresent()) {
            return ImmutableSet.of(nextNode.get());
        } else if (relationship == TemporalRelationship.RIGHT_BEFORE && prevNode.isPresent()) {
            return ImmutableSet.of(prevNode.get());
        } else {
            return ImmutableSet.of();
        }
    }

    @Implement
    public ImmutableSet<MonophonicNoteNode> getNodesBeginningBefore() {
        return prevNode.isPresent() ? ImmutableSet.of(prevNode.get()) : ImmutableSet.of();
    }
    
    @Implement
    public ImmutableSet<MonophonicNoteNode> getNodesEndingAfter() {
        return nextNode.isPresent() ? ImmutableSet.of(prevNode.get()) : ImmutableSet.of();
    }
    
    @Implement
    public ImmutableSet<MonophonicNoteNode> getCompletelyOrPartiallyOverlappingNodes() {
        return ImmutableSet.of();
    }
    
    public Optional<MonophonicNoteNode> getPreviousNode() {
        return prevNode;
    }
    
    public Optional<MonophonicNoteNode> getNextNode() {
        return prevNode;
    }
}
