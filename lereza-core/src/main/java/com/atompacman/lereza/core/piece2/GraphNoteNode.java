package com.atompacman.lereza.core.piece2;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.atompacman.lereza.core.theory.Note;
import com.atompacman.toolkat.annotations.DerivableFrom;
import com.atompacman.toolkat.annotations.Implement;
import com.google.common.collect.ImmutableSet;

final class GraphNoteNode extends AbstractNoteNode<GraphNoteNode> {

    //
    //  ~  FIELDS  ~  //
    //

    private final Map<TemporalRelationship, Set<GraphNoteNode>> neighbouringNodes;

    @DerivableFrom("neighbouringNodes")
    private final Set<GraphNoteNode> nodesBeginningBefore;
    @DerivableFrom("neighbouringNodes")
    private final Set<GraphNoteNode> nodesEndingAfter;
    @DerivableFrom("neighbouringNodes")
    private final Set<GraphNoteNode> overlappingNotes;


    //
    //  ~  INIT  ~  //
    //

    GraphNoteNode(Note note) {
        super(note);
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
    void connectTo(TemporalRelationship relationship, GraphNoteNode node) {
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
    public ImmutableSet<NoteNode> getNodes(TemporalRelationship relationship) {
        return ImmutableSet.copyOf(neighbouringNodes.get(relationship));
    }

    @Implement
    public ImmutableSet<NoteNode> getNodesBeginningBefore() {
        return ImmutableSet.copyOf(nodesBeginningBefore); 
    }

    @Implement
    public ImmutableSet<NoteNode> getNodesEndingAfter() {
        return ImmutableSet.copyOf(nodesEndingAfter); 
    }

    @Implement
    public ImmutableSet<NoteNode> getCompletelyOrPartiallyOverlappingNodes() {
        return ImmutableSet.copyOf(overlappingNotes); 
    }
}
