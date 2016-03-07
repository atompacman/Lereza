package com.atompacman.lereza.core.piece2;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashSet;
import java.util.Set;

import com.atompacman.lereza.core.theory.Note;
import com.atompacman.toolkat.annotations.Implement;
import com.google.common.collect.ImmutableSet;

final class AcyclicGraphNoteNode 
    extends AbstractNoteNode<AcyclicGraphNoteNode> implements HomophonicNoteNode {

    //
    //  ~  FIELDS  ~  //
    //

    private final Set<AcyclicGraphNoteNode> prevNodes; 
    private final Set<AcyclicGraphNoteNode> nextNodes;
    private final Set<AcyclicGraphNoteNode> simultaneousNodes;


    //
    //  ~  INIT  ~  //
    //

    AcyclicGraphNoteNode(Note note) {
        super(note);
        this.prevNodes         = new HashSet<>();
        this.nextNodes         = new HashSet<>();
        this.simultaneousNodes = new HashSet<>();
    }


    //
    //  ~  SETTERS  ~  //
    //

    @Implement
    void connectTo(TemporalRelationship relationship, AcyclicGraphNoteNode node) {
        if        (relationship == TemporalRelationship.RIGHT_AFTER) {
            checkArgument(nextNodes.add(node));
        } else if (relationship == TemporalRelationship.RIGHT_BEFORE) {
            checkArgument(prevNodes.add(node));
        } else if (relationship==TemporalRelationship.SIMULTANEOUS){
            checkArgument(simultaneousNodes.add(node));
        } else {
            throw new IllegalArgumentException();
        }
    }


    //
    //  ~  GETTERS  ~  //
    //

    @Implement
    public ImmutableSet<NoteNode> getNodes(TemporalRelationship relationship) {
        if        (relationship == TemporalRelationship.RIGHT_AFTER) {
            return ImmutableSet.copyOf(nextNodes);
        } else if (relationship == TemporalRelationship.RIGHT_BEFORE) {
            return ImmutableSet.copyOf(prevNodes);
        } else if (relationship == TemporalRelationship.SIMULTANEOUS){
            return ImmutableSet.copyOf(simultaneousNodes);
        } else {
            return ImmutableSet.of();
        }
    }

    @Implement
    public ImmutableSet<NoteNode> getNodesBeginningBefore() {
        return ImmutableSet.copyOf(prevNodes); 
    }

    @Implement
    public ImmutableSet<NoteNode> getNodesEndingAfter() {
        return ImmutableSet.copyOf(nextNodes); 
    }

    @Implement
    public ImmutableSet<NoteNode> getCompletelyOrPartiallyOverlappingNodes() {
        return ImmutableSet.copyOf(simultaneousNodes); 
    }
}
