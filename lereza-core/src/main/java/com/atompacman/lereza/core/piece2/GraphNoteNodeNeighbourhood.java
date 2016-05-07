package com.atompacman.lereza.core.piece2;

import java.util.EnumMap;
import java.util.Map;

import com.atompacman.lereza.core.piece2.PolyphonicNoteNode.TemporalRelationship;
import com.atompacman.toolkat.annotations.DerivableFrom;
import com.atompacman.toolkat.annotations.Implement;

final class GraphNoteNodeNeighbourhood implements PolyphonicNoteNodeNeighbourhood {

    //
    //  ~  FIELDS  ~  //
    //

    private final Map<TemporalRelationship, NoteNodeSet<PolyphonicNoteNode>> neighbouringNodes;

    @DerivableFrom("neighbouringNodes")
    private NoteNodeSet<PolyphonicNoteNode> nodesBeginningBefore;
    @DerivableFrom("neighbouringNodes")
    private NoteNodeSet<PolyphonicNoteNode> nodesEndingAfter;
    @DerivableFrom("neighbouringNodes")
    private NoteNodeSet<PolyphonicNoteNode> overlappingNotes;


    //
    //  ~  INIT  ~  //
    //

    GraphNoteNodeNeighbourhood() {
        this.neighbouringNodes = new EnumMap<>(TemporalRelationship.class);
        for (TemporalRelationship tr : TemporalRelationship.values()) {
            neighbouringNodes.put(tr, NoteNodeSet.of());
        }
        this.nodesBeginningBefore = NoteNodeSet.of();
        this.nodesEndingAfter     = NoteNodeSet.of();
        this.overlappingNotes     = NoteNodeSet.of();
    }


    //
    //  ~  CONNECT  ~  //
    //

    void connectTo(TemporalRelationship relationship, PolyphonicNoteNode node) {
        NoteNodeSet<PolyphonicNoteNode> set = neighbouringNodes.get(relationship);
        neighbouringNodes.put(relationship, NoteNodeSet.addNodeToSet(node, set));
        
        if (relationship.beginsBefore) {
            nodesBeginningBefore = NoteNodeSet.addNodeToSet(node, nodesBeginningBefore);
        }
        if (relationship.endsAfter) {
            nodesEndingAfter     = NoteNodeSet.addNodeToSet(node, nodesEndingAfter);
        }
        if (relationship.overlaps) {
            overlappingNotes     = NoteNodeSet.addNodeToSet(node, overlappingNotes);
        }
    }

    
    //
    //  ~  GETTERS  ~  //
    //
    
    @Implement
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public NoteNodeSet<PolyphonicNoteNode> getNodes(TemporalRelationship relationship) {
        return (NoteNodeSet<PolyphonicNoteNode>)(NoteNodeSet) neighbouringNodes.get(relationship);
    }

    @Implement
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public NoteNodeSet<PolyphonicNoteNode> getNodesBeginningBefore() {
        return (NoteNodeSet<PolyphonicNoteNode>)(NoteNodeSet) nodesBeginningBefore; 
    }

    @Implement
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public NoteNodeSet<PolyphonicNoteNode> getNodesEndingAfter() {
        return (NoteNodeSet<PolyphonicNoteNode>)(NoteNodeSet) nodesEndingAfter; 
    }

    @Implement
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public NoteNodeSet<PolyphonicNoteNode> getCompletelyOrPartiallyOverlappingNodes() {
        return (NoteNodeSet<PolyphonicNoteNode>)(NoteNodeSet) overlappingNotes; 
    }
}
