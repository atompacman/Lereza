package com.atompacman.lereza.core.piece;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nullable;

import com.atompacman.lereza.core.theory.Dynamic;
import com.atompacman.lereza.core.theory.Note;
import com.atompacman.lereza.core.theory.RhythmValue;
import com.atompacman.toolkat.annotations.DerivableFrom;
import com.atompacman.toolkat.annotations.Implement;

public class PolyphonicNoteNode implements MusicalStructure {

    //
    //  ~  CONSTANTS  ~  //
    //

    public static final Dynamic.Marker DEFAULT_DYNAMIC_MARKER = Dynamic.Marker.FORTE;
    public static final RhythmValue    DEFAULT_VALUE          = RhythmValue.QUARTER;

    
    //
    //  ~  INNER TYPES  ~  //
    //

    public enum TemporalRelationship {

        // Non-overlapping (Mono / Homo / Poly)
        RIGHT_BEFORE                         (true,  false, false),
        RIGHT_AFTER                          (false, true,  false),

        // Completely overlapping (Homo / Poly)
        COMPLETELY_OVERLAPPING               (false, false, true),
        
        // Partially overlapping (Poly)
        BEGINNED_BEFORE_ENDED_DURING         (true,  false, true),
        BEGINNED_BEFORE_ENDED_SIMULTANEOUSLY (true,  false, true),
        BEGINNED_BEFORE_ENDED_AFTER          (true,  true,  true),
        BEGINNED_SIMULTANEOUSLY_ENDED_DURING (false, false, true),
        BEGINNED_SIMULTANEOUSLY_ENDED_AFTER  (false, true,  true),
        BEGINNED_DURING_ENDED_DURING         (false, false, true),
        BEGINNED_DURING_ENDED_SIMULTANEOUSLY (false, false, true),
        BEGINNED_DURING_ENDED_AFTER          (false, true,  true);


        //
        //  ~  FIELDS  ~  //
        //
        
        protected boolean beginsBefore;
        protected boolean endsAfter;
        protected boolean overlaps;

        
        //
        //  ~  INIT  ~  //
        //
        
        private TemporalRelationship(boolean beginsBefore, boolean endsAfter, boolean overlaps) {
            this.beginsBefore = beginsBefore;
            this.endsAfter    = endsAfter;
            this.overlaps     = overlaps;
        }
    }
    
    public enum TiedNoteStatus {
        
        MERGE_TOGETHER,
        AS_SEPARATE_NOTES;
        
        
        //
        //  ~  STATIC FIELDS  ~  //
        //
        
        public static TiedNoteStatus defaultStatus = TiedNoteStatus.AS_SEPARATE_NOTES;
    }
    
    public interface Neighbourhood {

        //
        //  ~  INNER TYPES  ~  //
        //

        final static class Impl implements Neighbourhood {
        
            //
            //  ~  FIELDS  ~  //
            //
            
            private final Map<TemporalRelationship, NoteNodeSet<PolyphonicNoteNode>> 
                                                                                  neighbouringNodes;
        
            @DerivableFrom("neighbouringNodes")
            private NoteNodeSet<PolyphonicNoteNode> nodesBeginningBefore;
            @DerivableFrom("neighbouringNodes")
            private NoteNodeSet<PolyphonicNoteNode> nodesEndingAfter;
            @DerivableFrom("neighbouringNodes")
            private NoteNodeSet<PolyphonicNoteNode> overlappingNotes;
        
        
            //
            //  ~  INIT  ~  //
            //
        
            private Impl() {
                this.neighbouringNodes = new EnumMap<>(TemporalRelationship.class);
                for (TemporalRelationship tr : TemporalRelationship.values()) {
                    neighbouringNodes.put(tr, NoteNodeSet.empty());
                }
                this.nodesBeginningBefore = NoteNodeSet.empty();
                this.nodesEndingAfter     = NoteNodeSet.empty();
                this.overlappingNotes     = NoteNodeSet.empty();
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
            public NoteNodeSet<PolyphonicNoteNode> getNodes(TemporalRelationship relationship) {
                return neighbouringNodes.get(relationship);
            }
        
            @Implement
            public NoteNodeSet<PolyphonicNoteNode> getNodesBeginningBefore() {
                return nodesBeginningBefore; 
            }
        
            @Implement
            public NoteNodeSet<PolyphonicNoteNode> getNodesEndingAfter() {
                return nodesEndingAfter; 
            }
        
            @Implement
            public NoteNodeSet<PolyphonicNoteNode> getCompletelyOrPartiallyOverlappingNodes() {
                return overlappingNotes; 
            }
        }

        
        //
        // ~ GETTERS ~ //
        //
        
        NoteNodeSet<PolyphonicNoteNode> getNodes(TemporalRelationship relationship);

        NoteNodeSet<PolyphonicNoteNode> getNodesBeginningBefore();

        NoteNodeSet<PolyphonicNoteNode> getNodesEndingAfter();

        NoteNodeSet<PolyphonicNoteNode> getCompletelyOrPartiallyOverlappingNodes();
    }
        
    
    //
    //  ~  FIELDS  ~  //
    //

    /** Rests have no attached note */
    private final Optional<Note> note;
    
    /** Note or rest duration */
    private final RhythmValue value;

    protected Optional<PolyphonicNoteNode> prevTiedNote;
    protected Optional<PolyphonicNoteNode> nextTiedNote;

    protected Neighbourhood neighbourhoodSeparated;
    protected Neighbourhood neighbourhoodMerged;
    

    //
    //  ~  INIT  ~  //
    //

    protected PolyphonicNoteNode(Note note) {
        this(Optional.of(note), 
             note.getRhythmValue(), 
             new Neighbourhood.Impl(), 
             new Neighbourhood.Impl());
    }
    
    protected PolyphonicNoteNode(RhythmValue value) {
        this(Optional.empty(), 
             value, 
             new Neighbourhood.Impl(), 
             new Neighbourhood.Impl());
    }
    
    protected PolyphonicNoteNode(Note          note,
                                 Neighbourhood neighbourhoodSeparated,
                                 Neighbourhood neighbourhoodMerged) {
        
        this(Optional.of(note), 
             note.getRhythmValue(), 
             neighbourhoodSeparated, 
             neighbourhoodMerged);
    }
    
    protected PolyphonicNoteNode(RhythmValue   value,
                                 Neighbourhood neighbourhoodSeparated,
                                 Neighbourhood neighbourhoodMerged) {
        
        this(Optional.empty(), 
             value, 
             neighbourhoodSeparated, 
             neighbourhoodMerged);
    }

    private PolyphonicNoteNode(Optional<Note> note, 
                               RhythmValue    value, 
                               Neighbourhood  neighbourhoodSeparated,
                               Neighbourhood  neighbourhoodMerged) {
        this.note  = note;
        this.value = value;

        this.prevTiedNote = Optional.empty();
        this.nextTiedNote = Optional.empty();
        
        this.neighbourhoodSeparated = neighbourhoodSeparated;
        this.neighbourhoodMerged    = neighbourhoodMerged;
    }
    

    //
    //  ~  SETTERS  ~  //
    //

    void tieToNextNote() {
        // Set of notes right after
        NoteNodeSet<PolyphonicNoteNode> set = getNoteNeighbourhood(TiedNoteStatus.AS_SEPARATE_NOTES)
                                                .getNodes(TemporalRelationship.RIGHT_AFTER);
        // Get note to tie to
        Optional<PolyphonicNoteNode> node = isRest() ? set.getRest() 
                                                     : set.getNode(note.get().getPitch());
        @SuppressWarnings({ "unchecked", "rawtypes" })
        Optional<PolyphonicNoteNode> nextNode = (Optional) node;
        checkArgument(nextNode.isPresent());

        // Must not be already connected
        checkArgument(!nextTiedNote.isPresent());
        checkArgument(!nextNode.get().prevTiedNote.isPresent());
        
        // Connect
        nextTiedNote = nextNode;
        nextNode.get().prevTiedNote = Optional.of(this);
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //
    
    public Optional<Note> getNote() {
        return note;
    }

    public RhythmValue getRhythmValue() {
        return value;
    }

    public @Nullable PolyphonicNoteNode getPreviousTiedNode() {
        return prevTiedNote.orElse(null);
    }

    public @Nullable PolyphonicNoteNode getNextTiedNode() {
        return nextTiedNote.orElse(null);
    }

    public Neighbourhood getNoteNeighbourhood() { 
        return getNoteNeighbourhood(TiedNoteStatus.defaultStatus); 
    }
    
    public Neighbourhood getNoteNeighbourhood(TiedNoteStatus status) {
        return status == TiedNoteStatus.AS_SEPARATE_NOTES ? neighbourhoodSeparated 
                                                          : neighbourhoodMerged;
    }


    //
    //  ~  STATE  ~  //
    //

    public int totalTiedNoteTimeunitLength() {
        int length = value.toTimeunit();
        Optional<PolyphonicNoteNode> other=prevTiedNote;
        while (other.isPresent()) {
            PolyphonicNoteNode node = other.get();
            length += node.getRhythmValue().toTimeunit();
            other = Optional.ofNullable(node.getPreviousTiedNode());
        }
        other = nextTiedNote;
        while (other.isPresent()) {
            PolyphonicNoteNode node = other.get();
            length += node.getRhythmValue().toTimeunit();
            other = Optional.ofNullable(node.getNextTiedNode());
        }
        return length;
    }

    public boolean isTied() {
        return prevTiedNote.isPresent();
    }

    public boolean isRest() {
        return !note.isPresent();
    }


    //
    //  ~  SERIALIZATION  ~  //
    //

    public String toStaccato() {
        StringBuilder sb = new StringBuilder();
        if (note.isPresent()) {
            if (isTied()) {
                sb.append('(');
            }
            sb.append(note.get().toStaccato());
            if (isTied()) {
                sb.append(')');
            }
        } else {
            sb.append('R').append(value.toStaccato());;
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return toStaccato();
    }
}
