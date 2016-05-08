package com.atompacman.lereza.core.piece;

import javax.annotation.Nullable;

import com.atompacman.lereza.core.theory.Note;
import com.atompacman.lereza.core.theory.RythmnValue;
import com.atompacman.toolkat.annotations.Implement;

public class HomophonicNoteNode extends PolyphonicNoteNode {

    //
    //  ~  INNER TYPES  ~  //
    //
    
    public interface Neighbourhood extends PolyphonicNoteNode.Neighbourhood {
    
        //
        //  ~  INNER TYPES  ~  //
        //

        final static class Impl implements Neighbourhood {
        
            //
            //  ~  FIELDS  ~  //
            //
        
            private NoteNodeSet<HomophonicNoteNode> prevNodes; 
            private NoteNodeSet<HomophonicNoteNode> nextNodes;
            private NoteNodeSet<HomophonicNoteNode> simultaneousNodes;
        
        
            //
            //  ~  INIT  ~  //
            //
        
            private Impl() {               
                this.prevNodes         = NoteNodeSet.empty();
                this.nextNodes         = NoteNodeSet.empty();
                this.simultaneousNodes = NoteNodeSet.empty();
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
                    return NoteNodeSet.empty();
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
        
        
        //
        //  ~  GETTERS  ~  //
        //
    
        NoteNodeSet<HomophonicNoteNode> getNodesRightBefore();
        
        NoteNodeSet<HomophonicNoteNode> getNodesRightAfter();
        
        NoteNodeSet<HomophonicNoteNode> getSimultaneousNodes();
    }
    

    //
    //  ~  INIT  ~  //
    //

    protected HomophonicNoteNode(Note note) {
        super(note, new Neighbourhood.Impl(), new Neighbourhood.Impl());
    }
    
    protected HomophonicNoteNode(RythmnValue value) {
        super(value, new Neighbourhood.Impl(), new Neighbourhood.Impl());
    }
    
    protected HomophonicNoteNode(Note          note,
                                 Neighbourhood neighbourhoodSeparated,
                                 Neighbourhood neighbourhoodMerged) {
        super(note,
              neighbourhoodSeparated, 
              neighbourhoodMerged);
    }
    
    protected HomophonicNoteNode(RythmnValue   value,
                                 Neighbourhood neighbourhoodSeparated,
                                 Neighbourhood neighbourhoodMerged) {
        super(value, 
              neighbourhoodSeparated, 
              neighbourhoodMerged);
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //

    @Override
    public @Nullable HomophonicNoteNode getPreviousTiedNode() {
        return (HomophonicNoteNode) prevTiedNote.orElse(null);
    }

    @Override
    public @Nullable HomophonicNoteNode getNextTiedNode() {
        return (HomophonicNoteNode) nextTiedNote.orElse(null);
    }

    @Override
    public Neighbourhood getNoteNeighbourhood() { 
        return getNoteNeighbourhood(TiedNoteStatus.defaultStatus); 
    }
    
    @Override
    public Neighbourhood getNoteNeighbourhood(TiedNoteStatus status) {
        return (Neighbourhood) super.getNoteNeighbourhood(status);
    }
}
