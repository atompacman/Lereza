package com.atompacman.lereza.core.piece;

import java.util.Optional;

import javax.annotation.Nullable;

import com.atompacman.lereza.core.theory.Note;
import com.atompacman.lereza.core.theory.RhythmValue;
import com.atompacman.toolkat.annotations.Implement;

public final class MonophonicNoteNode extends HomophonicNoteNode {
    
    //
    //  ~  INNER TYPES  ~  //
    //

    public interface Neighbourhood extends HomophonicNoteNode.Neighbourhood {
            
        //
        //  ~  INNER TYPES  ~  //
        //

        final static class Impl implements Neighbourhood {
        
            //
            //  ~  FIELDS  ~  //
            //
        
            Optional<MonophonicNoteNode> prevNode; 
            Optional<MonophonicNoteNode> nextNode;
        
        
            //
            //  ~  INIT  ~  //
            //
        
            private Impl() {
                this.prevNode = Optional.empty();
                this.nextNode = Optional.empty();
            }

        
            //
            //  ~  GETTERS  ~  //
            //
            
            @Implement
            public NoteNodeSet<PolyphonicNoteNode> getNodes(TemporalRelationship relationship) {
                if (relationship == TemporalRelationship.RIGHT_AFTER && nextNode.isPresent()) {
                    return NoteNodeSet.of(nextNode.get());
                }else if(relationship == TemporalRelationship.RIGHT_BEFORE && prevNode.isPresent()){
                    return NoteNodeSet.of(prevNode.get());
                } else {
                    return NoteNodeSet.empty();
                }
            }
        
            @Implement
            public NoteNodeSet<PolyphonicNoteNode> getNodesBeginningBefore() {
                return prevNode.isPresent() ? NoteNodeSet.of(prevNode.get()) : NoteNodeSet.empty();
            }
        
            @Implement
            public NoteNodeSet<PolyphonicNoteNode> getNodesEndingAfter() {
                return nextNode.isPresent() ? NoteNodeSet.of(nextNode.get()) : NoteNodeSet.empty();
            }
        
            @Implement
            public NoteNodeSet<PolyphonicNoteNode> getCompletelyOrPartiallyOverlappingNodes() {
                return NoteNodeSet.empty();
            }
        
            @Implement
            public NoteNodeSet<HomophonicNoteNode> getNodesRightBefore() {
                return prevNode.isPresent() ? NoteNodeSet.of(prevNode.get()) : NoteNodeSet.empty();
            }
        
            @Implement
            public NoteNodeSet<HomophonicNoteNode> getNodesRightAfter() {
                return nextNode.isPresent() ? NoteNodeSet.of(nextNode.get()) : NoteNodeSet.empty();
            }
        
            @Implement
            public NoteNodeSet<HomophonicNoteNode> getSimultaneousNodes() {
                return NoteNodeSet.empty();
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
        
        
        //
        //  ~  GETTERS  ~  //
        //

        Optional<MonophonicNoteNode> getPreviousNode();

        Optional<MonophonicNoteNode> getNextNode();
    }

    
    //
    //  ~  INIT  ~  //
    //

    protected MonophonicNoteNode(Note note) {
        super(note, new Neighbourhood.Impl(), new Neighbourhood.Impl());
    }
    
    protected MonophonicNoteNode(RhythmValue value) {
        super(value, new Neighbourhood.Impl(), new Neighbourhood.Impl());
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //

    @Override
    public @Nullable MonophonicNoteNode getPreviousTiedNode() {
        return (MonophonicNoteNode) prevTiedNote.orElse(null);
    }

    @Override
    public @Nullable MonophonicNoteNode getNextTiedNode() {
        return (MonophonicNoteNode) nextTiedNote.orElse(null);
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
