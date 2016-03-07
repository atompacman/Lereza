package com.atompacman.lereza.core.piece2;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Optional;

import com.atompacman.lereza.core.theory.Dynamic;
import com.atompacman.lereza.core.theory.Note;
import com.atompacman.lereza.core.theory.RythmnValue;
import com.atompacman.toolkat.annotations.Implement;

abstract class AbstractNoteNode<T extends AbstractNoteNode<T>> implements NoteNode {

    //
    //  ~  INNER TYPES  ~  //
    //

    public enum TemporalRelationship {

        // Non-overlapping (Mono / Homo / Poly)
        RIGHT_BEFORE                         (true,  false, false),
        RIGHT_AFTER                          (false, true,  false),

        // Completely overlapping (Homo / Poly)
        SIMULTANEOUS                         (false, false, true),

        // Partially overlapping (Poly)
        BEGINNED_BEFORE_ENDED_DURING         (true,  false, true),
        BEGINNED_BEFORE_ENDED_SIMULTANEOUSLY (true,  false, true),
        BEGINNED_BEFORE_ENDED_AFTER          (true,  true,  true),
        BEGINNED_SIMULTANEOUSLY_ENDED_DURING (false, false, true),
        BEGINNED_SIMULTANEOUSLY_ENDED_AFTER  (false, true,  true),
        BEGINNED_DURING_ENDED_DURING         (false, false, true),
        BEGINNED_DURING_ENDED_SIMULTANEOUSLY (false, false, true),
        BEGINNED_DURING_ENDED_AFTER          (false, true,  true);


        protected boolean beginsBefore;
        protected boolean endsAfter;
        protected boolean overlaps;

        private TemporalRelationship(boolean beginsBefore, boolean endsAfter, boolean overlaps) {
            this.beginsBefore = beginsBefore;
            this.endsAfter    = endsAfter;
            this.overlaps     = overlaps;
        }
    }


    //
    //  ~  FIELDS  ~  //
    //

    /** Rests have no attached note */
    private final Optional<Note> note;
    private final RythmnValue    value;

    protected Optional<T> prevTiedNote;
    protected Optional<T> nextTiedNote;


    //
    //  ~  INIT  ~  //
    //

    protected AbstractNoteNode(Note note) {
        this.note  = Optional.of(note);
        this.value = note.getRythmnValue();

        this.prevTiedNote = Optional.empty();
        this.nextTiedNote = Optional.empty();
    }


    //
    //  ~  SETTERS  ~  //
    //

    abstract void connectTo(TemporalRelationship relationship, T node);

    @SuppressWarnings("unchecked")
    void tieTo(T next) {
        checkArgument(note.isPresent());
        checkArgument(next.getNote().isPresent());
        checkArgument(note.get().equals(next.getNote().get()));

        nextTiedNote      = Optional.of(next);
        next.prevTiedNote = (Optional<T>) Optional.of(this);
    }

    
    //
    //  ~  GETTERS  ~  //
    //
    
    @Implement
    public Optional<Note> getNote() {
        return note;
    }

    @Implement
    public RythmnValue getRythmnValue() {
        return value;
    }

    @Implement
    @SuppressWarnings("unchecked")
    public Optional<NoteNode> getPreviousTiedNode() {
        return (Optional<NoteNode>) prevTiedNote;
    }

    @Implement
    @SuppressWarnings("unchecked")
    public Optional<NoteNode> getNextTiedNode() {
        return (Optional<NoteNode>) nextTiedNote;
    }


    //
    //  ~  STATE  ~  //
    //

    @Implement
    public int totalTiedNoteTimeunitLength() {
        int length = value.toTimeunit();
        Optional<T> other = prevTiedNote;
        while (other.isPresent()) {
            T node =  other.get();
            length += node.getRythmnValue().toTimeunit();
            other  =  node.prevTiedNote;
        }
        other = nextTiedNote;
        while (other.isPresent()) {
            T node =  other.get();
            length += node.getRythmnValue().toTimeunit();
            other  =  node.nextTiedNote;
        }
        return length;
    }

    @Implement
    public boolean isTied() {
        return prevTiedNote.isPresent();
    }

    @Implement
    public boolean isRest() {
        return !note.isPresent();
    }


    //
    //  ~  SERIALIZATION  ~  //
    //

    @Implement
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
