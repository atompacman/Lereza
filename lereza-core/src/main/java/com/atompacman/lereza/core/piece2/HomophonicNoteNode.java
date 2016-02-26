package com.atompacman.lereza.core.piece2;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashSet;
import java.util.Set;

import com.atompacman.lereza.core.theory.Note;
import com.atompacman.lereza.core.theory.Pitch;
import com.atompacman.lereza.core.theory.RythmnValue;
import com.atompacman.toolkat.annotations.Implement;
import com.google.common.collect.ImmutableSet;

public final class HomophonicNoteNode extends NoteNode<HomophonicNoteNode> {
    
    //
    //  ~  FIELDS  ~  //
    //

    private final Set<HomophonicNoteNode> prevNodes; 
    private final Set<HomophonicNoteNode> nextNodes;
    private final Set<HomophonicNoteNode> simultaneousNodes;

    
    //
    //  ~  INIT  ~  //
    //

    HomophonicNoteNode(Pitch pitch, RythmnValue value) {
        super(Note.of(pitch, value));
        this.prevNodes         = new HashSet<>();
        this.nextNodes         = new HashSet<>();
        this.simultaneousNodes = new HashSet<>();
    }


    //
    //  ~  SETTERS  ~  //
    //
    
    @Implement
    void connectTo(TemporalRelationship relationship, HomophonicNoteNode node) {
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
    public ImmutableSet<HomophonicNoteNode> getNodes(TemporalRelationship relationship) {
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
    public ImmutableSet<HomophonicNoteNode> getNodesBeginningBefore() {
        return ImmutableSet.copyOf(prevNodes); 
    }
    
    @Implement
    public ImmutableSet<HomophonicNoteNode> getNodesEndingAfter() {
        return ImmutableSet.copyOf(nextNodes); 
    }

    @Implement
    public ImmutableSet<HomophonicNoteNode> getCompletelyOrPartiallyOverlappingNodes() {
        return ImmutableSet.copyOf(simultaneousNodes); 
    }
}
