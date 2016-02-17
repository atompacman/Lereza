package com.atompacman.lereza.core.piece2;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.atompacman.lereza.core.theory.Pitch;
import com.google.common.collect.ImmutableList;

public abstract class BarSlice<T extends NoteNode<T>> {

    //
    //  ~  FIELDS  ~  //
    //
    
    // Pre-computed
    private final boolean isRest;
    private final boolean hasBeginningNotes;

    
    //
    //  ~  INIT  ~  //
    //

    protected BarSlice() {
        ImmutableList<T> beginningNotes = getBeginningNoteNodes();
        ImmutableList<T> playingNotes   = getPlayingNoteNodes();

        checkArgument(playingNotes.size() > 0, "Must contain at least one note");
        checkArgument(playingNotes.containsAll(beginningNotes), 
                "Playing notes must contain all beginning notes");

        Set<Pitch> pitches = new HashSet<>();
        int numRests = 0;
        
        for (T node : playingNotes) {
            if (node.isRest()) {
                ++numRests;
            } else {
                checkArgument(pitches.add(node.getNote().get().getPitch()), 
                        "Cannot contain multiple notes with the same pitch");
            }
        }
        
        checkArgument(numRests <= 1, "Cannot have multiple rest nodes");
        checkArgument(numRests != 1 || playingNotes.size() == 1,"Cannot have both rests and notes");
        
        // Pre-computed
        this.isRest            = numRests == 1;
        this.hasBeginningNotes = !beginningNotes.isEmpty();
    }

    
    //
    //  ~  GETTERS  ~  //
    //

    public abstract ImmutableList<T> getBeginningNoteNodes();
    
    public abstract ImmutableList<T> getPlayingNoteNodes();
    
    
    //
    //  ~  STATUS  ~  //
    //

    public boolean hasBeginningNote() {
        return hasBeginningNotes;
    }

    public boolean isRest() {
        return isRest;
    }
    
    
    //
    //  ~  SERIALIZATION  ~  //
    //

    @Override
    public String toString() {
        return toStaccato();
    }
    
    public String toStaccato() {
        if (!hasBeginningNote()) {
            return "";
        }
        
        if (isRest()) {
            return "R" + getPlayingNoteNodes().get(0).getValue().toStaccato();
        }

        StringBuilder sb = new StringBuilder();
        Iterator<T> it = getBeginningNoteNodes().iterator();
        sb.append(it.next().toStaccato());
        while (it.hasNext()) {
            sb.append('+').append(it.next().toStaccato());
        }
        return sb.toString();
    }
}
