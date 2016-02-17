package com.atompacman.lereza.core.piece2;

import java.util.Optional;

import com.atompacman.lereza.core.theory.Dynamic;
import com.atompacman.lereza.core.theory.Note;
import com.atompacman.lereza.core.theory.Value;

import autovalue.shaded.com.google.common.common.collect.ImmutableList;

public abstract class NoteNode<T extends NoteNode<T>> {

    //
    //  ~  INNER TYPES  ~  //
    //

    public enum TemporalRelationship {

        COMPLETELY_BEFORE,
        BEGINNED_BEFORE_ENDED_DURING,
        BEGINNED_BEFORE_ENDED_AFTER,
        BEGINNED_DURING_ENDED_DURING,
        BEGINNED_DURING_ENDED_AFTER,
        COMPLETELY_AFTER;
    }

    
    //
    //  ~  FIELDS  ~  //
    //

    private final Optional<Note> note;

    // Pre-computed
    protected       Optional<T> prevTiedNote;
    protected       Optional<T> nextTiedNote;
    protected final Value       value;
    
    
    //
    //  ~  INIT  ~  //
    //
    
    protected NoteNode(Note note) {
        this.note         = Optional.of(note);
        
        // Pre-computed
        this.prevTiedNote = Optional.empty();
        this.nextTiedNote = Optional.empty();
        this.value        = note.getValue();
    }

    void connectTo() {
        
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //

    public Optional<Note> getNote() {
        return note;
    }
    
    public abstract ImmutableList<T> getOverlappingNoteNodes();
    
    public abstract ImmutableList<T> getNextOverlappingNoteNodes();
    
    
    public Optional<T> getPreviousTiedNote() {
        return prevTiedNote;
    }
    
    public Optional<T> getNextTiedNote() {
        return nextTiedNote;
    }
    
    public Value getValue() {
        return value;
    }
    

    //
    //  ~  SETTERS  ~  //
    //
    
    @SuppressWarnings("unchecked")
    void tieTo(T next) {
        nextTiedNote      = Optional.of(next);
        next.nextTiedNote = (Optional<T>) Optional.of(this);
    }
    
    
    //
    //  ~  STATE  ~  //
    //

    public int totalTiedNoteTimeunitLength() {
        int length = value.toTimeunit();
        Optional<T> other = prevTiedNote;
        while (other.isPresent()) {
            T node = other.get();
            length += node.value.toTimeunit();
            other = node.prevTiedNote;
        }
        other = nextTiedNote;
        while (other.isPresent()) {
            T node = other.get();
            length += node.value.toTimeunit();
            other = node.nextTiedNote;
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
    
    @Override
    public String toString() {
        return toStaccato();
    }
    
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
}
