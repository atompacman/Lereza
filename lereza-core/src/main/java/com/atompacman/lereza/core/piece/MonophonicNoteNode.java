package com.atompacman.lereza.core.piece;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Optional;

import javax.annotation.Nullable;

import com.atompacman.lereza.core.theory.Note;
import com.atompacman.lereza.core.theory.RythmnValue;
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
        
            private Optional<MonophonicNoteNode> prevNode; 
            private Optional<MonophonicNoteNode> nextNode;
        
        
            //
            //  ~  INIT  ~  //
            //
        
            private Impl() {
                this.prevNode = Optional.empty();
                this.nextNode = Optional.empty();
            }
        
        
            //
            //  ~  CONNECT  ~  //
            //
        
            static void connect(MonophonicNoteNode from, 
                                MonophonicNoteNode to,
                                TiedNoteStatus     status,
                                boolean            areTied) {
                
                // Get neighbourhoods
                Neighbourhood.Impl nbhFrom = (Neighbourhood.Impl) from.getNoteNeighbourhood(status);
                Neighbourhood.Impl nbhTo   = (Neighbourhood.Impl) to.getNoteNeighbourhood(status);
                
                // Must not already be connected
                checkArgument(!nbhFrom.nextNode.isPresent() && !nbhTo.prevNode.isPresent());
                
                // Connect nodes
                nbhFrom.nextNode = Optional.of(to);
                nbhFrom.prevNode = Optional.of(from);
                if (areTied) {
                    from.tieToNextNote();
                }
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
    
    protected MonophonicNoteNode(RythmnValue value) {
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
