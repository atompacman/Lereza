package com.atompacman.lereza.core.piece2;

import com.atompacman.lereza.core.piece2.PolyphonicNoteNode.TemporalRelationship;
import com.atompacman.toolkat.annotations.Implement;

final class AcyclicGraphNoteNodeNeighbourhood implements HomophonicNoteNodeNeighbourhood {

    //
    //  ~  FIELDS  ~  //
    //

    private NoteNodeSet<HomophonicNoteNode> prevNodes; 
    private NoteNodeSet<HomophonicNoteNode> nextNodes;
    private NoteNodeSet<HomophonicNoteNode> simultaneousNodes;


    //
    //  ~  INIT  ~  //
    //

    AcyclicGraphNoteNodeNeighbourhood() {
        this.prevNodes         = NoteNodeSet.of();
        this.nextNodes         = NoteNodeSet.of();
        this.simultaneousNodes = NoteNodeSet.of();
    }


    //
    //  ~  CONNECT  ~  //
    //

    void connectToPrev(HomophonicNoteNode prev) {
        prevNodes = NoteNodeSet.addNodeToSet(prev, prevNodes);
    }
    
    void connectToNext(HomophonicNoteNode next) {
        nextNodes = NoteNodeSet.addNodeToSet(next, nextNodes);
    }
    
    void connectToSimultaneous(HomophonicNoteNode simult) {
        simultaneousNodes = NoteNodeSet.addNodeToSet(simult, simultaneousNodes);
    }
    

    //
    //  ~  GETTERS  ~  //
    //

    @Implement
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public NoteNodeSet<PolyphonicNoteNode> getNodes(TemporalRelationship relationship) {
        if        (relationship == TemporalRelationship.RIGHT_AFTER) {
            return (NoteNodeSet<PolyphonicNoteNode>) (NoteNodeSet) nextNodes;
        } else if (relationship == TemporalRelationship.RIGHT_BEFORE) {
            return (NoteNodeSet<PolyphonicNoteNode>) (NoteNodeSet) prevNodes;
        } else if (relationship == TemporalRelationship.COMPLETELY_OVERLAPPING){
            return (NoteNodeSet<PolyphonicNoteNode>) (NoteNodeSet) simultaneousNodes;
        } else {
            return NoteNodeSet.of();
        }
    }
    
    @Implement
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public NoteNodeSet<PolyphonicNoteNode> getNodesBeginningBefore() {
        return (NoteNodeSet<PolyphonicNoteNode>) (NoteNodeSet) prevNodes;
    }
    
    @Implement
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public NoteNodeSet<PolyphonicNoteNode> getNodesEndingAfter() {
        return (NoteNodeSet<PolyphonicNoteNode>) (NoteNodeSet) nextNodes;
    }
    
    @Implement
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public NoteNodeSet<PolyphonicNoteNode> getCompletelyOrPartiallyOverlappingNodes() {
        return (NoteNodeSet<PolyphonicNoteNode>) (NoteNodeSet) simultaneousNodes;
    }

    @Implement
    public NoteNodeSet<HomophonicNoteNode> getNodesRightBefore() {
        return prevNodes;
    }

    @Implement
    public NoteNodeSet<HomophonicNoteNode> getNodesRightAfter() {
        return nextNodes;
    }

    @Implement
    public NoteNodeSet<HomophonicNoteNode> getSimultaneousNodes() {
        return simultaneousNodes;
    }
}
