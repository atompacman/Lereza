package com.atompacman.lereza.core.piece2;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Optional;

import javax.annotation.Nullable;

import com.atompacman.lereza.core.theory.Dynamic;
import com.atompacman.lereza.core.theory.Note;
import com.atompacman.lereza.core.theory.RythmnValue;

public class PolyphonicNoteNode implements MusicalStructure {

    //
    //  ~  CONSTANTS  ~  //
    //

    public static final Dynamic.Marker DEFAULT_DYNAMIC_MARKER = Dynamic.Marker.FORTE;
    public static final RythmnValue    DEFAULT_VALUE          = RythmnValue.QUARTER;

    
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
    
    
    //
    //  ~  FIELDS  ~  //
    //

    /** Rests have no attached note */
    private final Optional<Note> note;
    
    /** Note or rest duration */
    private final RythmnValue value;

    protected Optional<PolyphonicNoteNode> prevTiedNote;
    protected Optional<PolyphonicNoteNode> nextTiedNote;

    protected PolyphonicNoteNodeNeighbourhood neighbourhoodSeparated;
    protected PolyphonicNoteNodeNeighbourhood neighbourhoodMerged;
    

    //
    //  ~  INIT  ~  //
    //

    protected PolyphonicNoteNode(Note note) {
        this(Optional.of(note), 
             note.getRythmnValue(), 
             new GraphNoteNodeNeighbourhood(), 
             new GraphNoteNodeNeighbourhood());
    }
    
    protected PolyphonicNoteNode(RythmnValue value) {
        this(Optional.empty(), 
             value, 
             new GraphNoteNodeNeighbourhood(), 
             new GraphNoteNodeNeighbourhood());
    }
    
    protected PolyphonicNoteNode(Note                            note,
                                 PolyphonicNoteNodeNeighbourhood neighbourhoodSeparated,
                                 PolyphonicNoteNodeNeighbourhood neighbourhoodMerged) {
        this(Optional.of(note), 
             note.getRythmnValue(), 
             neighbourhoodSeparated, 
             neighbourhoodMerged);
    }
    
    protected PolyphonicNoteNode(RythmnValue                     value,
                                 PolyphonicNoteNodeNeighbourhood neighbourhoodSeparated,
                                 PolyphonicNoteNodeNeighbourhood neighbourhoodMerged) {
        this(Optional.empty(), 
             value, 
             neighbourhoodSeparated, 
             neighbourhoodMerged);
    }

    private PolyphonicNoteNode(Optional<Note>                  note, 
                               RythmnValue                     value, 
                               PolyphonicNoteNodeNeighbourhood neighbourhoodSeparated,
                               PolyphonicNoteNodeNeighbourhood neighbourhoodMerged) {
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    void tieToNextNote() {
        checkArgument(!isRest());
        Optional<PolyphonicNoteNode> node = getNoteNeighbourhood(TiedNoteStatus.AS_SEPARATE_NOTES)
                                              .getNodesEndingAfter().getNode(note.get().getPitch());
        checkArgument(node.isPresent());
        Optional<PolyphonicNoteNode> nextNode = (Optional<PolyphonicNoteNode>)(Optional) node;
        
        nextTiedNote = nextNode;
        nextNode.get().prevTiedNote = Optional.of(this);
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //
    
    public Optional<Note> getNote() {
        return note;
    }

    public RythmnValue getRythmnValue() {
        return value;
    }

    public @Nullable PolyphonicNoteNode getPreviousTiedNode() {
        return prevTiedNote.orElse(null);
    }

    public @Nullable PolyphonicNoteNode getNextTiedNode() {
        return nextTiedNote.orElse(null);
    }

    public PolyphonicNoteNodeNeighbourhood getNoteNeighbourhood() { 
        return getNoteNeighbourhood(TiedNoteStatus.defaultStatus); 
    }
    
    public PolyphonicNoteNodeNeighbourhood getNoteNeighbourhood(TiedNoteStatus status) {
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
            PolyphonicNoteNode node =  other.get();
            length += node.getRythmnValue().toTimeunit();
            other = Optional.ofNullable(node.getPreviousTiedNode());
        }
        other = nextTiedNote;
        while (other.isPresent()) {
            PolyphonicNoteNode node =  other.get();
            length += node.getRythmnValue().toTimeunit();
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
            sb.append(note.get().toStaccato());
        } else {
            sb.append('R');
        }
        if (prevTiedNote.isPresent()) {
            sb.append('-');
        }
        sb.append(value.toStaccato());
        if (nextTiedNote.isPresent()) {
            sb.append('-');
        }
        if (note.isPresent()) {
            Optional<Dynamic> dynamic = note.get().getDynamic();
            if (dynamic.isPresent()) {
                sb.append('a').append(dynamic.get().getVelocity());
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return toStaccato();
    }
}
