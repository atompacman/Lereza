package com.atompacman.lereza.core.piece2;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.atompacman.lereza.core.theory.Note;
import com.atompacman.lereza.core.theory.Pitch;
import com.atompacman.lereza.core.theory.RythmnValue;
import com.atompacman.toolkat.annotations.DerivableFrom;
import com.atompacman.toolkat.annotations.Implement;
import com.google.common.collect.ImmutableSet;

public final class PolyphonicNoteNode extends NoteNode<PolyphonicNoteNode> {

    //
    //  ~  FIELDS  ~  //
    //

    private final Map<TemporalRelationship, Set<PolyphonicNoteNode>> neighbouringNodes;
    
    @DerivableFrom("neighbouringNodes")
    private final Set<PolyphonicNoteNode> nodesBeginningBefore;
    @DerivableFrom("neighbouringNodes")
    private final Set<PolyphonicNoteNode> nodesEndingAfter;
    @DerivableFrom("neighbouringNodes")
    private final Set<PolyphonicNoteNode> overlappingNotes;
    
    
    //
    //  ~  INIT  ~  //
    //

    PolyphonicNoteNode(Pitch pitch, RythmnValue value) {
        super(Note.of(pitch, value));
        this.neighbouringNodes = new EnumMap<>(TemporalRelationship.class);
        for (TemporalRelationship tr : TemporalRelationship.values()) {
            neighbouringNodes.put(tr, new HashSet<>());
        }
        this.nodesBeginningBefore = new HashSet<>();
        this.nodesEndingAfter     = new HashSet<>();
        this.overlappingNotes     = new HashSet<>();

    }


    //
    //  ~  SETTERS  ~  //
    //
    
    @Implement
    void connectTo(TemporalRelationship relationship, PolyphonicNoteNode node) {
        checkArgument(neighbouringNodes.get(relationship).add(node));
        
        if (relationship.beginsBefore) {
            checkArgument(nodesBeginningBefore.add(node));
        }
        if (relationship.endsAfter) {
            checkArgument(nodesEndingAfter.add(node));
        }
        if (relationship.overlaps) {
            checkArgument(overlappingNotes.add(node));
        }
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //
    
    @Implement
    public ImmutableSet<PolyphonicNoteNode> getNodes(TemporalRelationship relationship) {
        return ImmutableSet.copyOf(neighbouringNodes.get(relationship));
    }
    
    @Implement
    public ImmutableSet<PolyphonicNoteNode> getNodesBeginningBefore() {
        return ImmutableSet.copyOf(nodesBeginningBefore); 
    }
    
    @Implement
    public ImmutableSet<PolyphonicNoteNode> getNodesEndingAfter() {
        return ImmutableSet.copyOf(nodesEndingAfter); 
    }

    @Implement
    public ImmutableSet<PolyphonicNoteNode> getCompletelyOrPartiallyOverlappingNodes() {
        return ImmutableSet.copyOf(overlappingNotes); 
    }
}
