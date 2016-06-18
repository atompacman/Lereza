package com.atompacman.lereza.core.piece2;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.atompacman.lereza.core.theory.Pitch;
import com.atompacman.toolkat.annotations.DerivableFrom;
import com.google.common.collect.ImmutableSet;

abstract class BarSlice<T extends NoteNode> {

    //
    //  ~  FIELDS  ~  //
    //

    @DerivableFrom("getBeginningNoteNodes()")
    private final boolean isRest;
    @DerivableFrom("getBeginningNoteNodes()")
    private final boolean hasBeginningNotes;


    //
    //  ~  INIT  ~  //
    //

    protected BarSlice() {
        ImmutableSet<T> beginningNotes = getBeginningNoteNodes();
        ImmutableSet<T> playingNotes   = getPlayingNoteNodes();

        checkArgument(playingNotes.containsAll(beginningNotes), 
                "Playing notes must contain all beginning notes");

        Set<Pitch> pitches = new HashSet<>();
        int numRests = 0;

        for (T node : beginningNotes) {
            if (node.isRest()) {
                ++numRests;
            } else {
                checkArgument(pitches.add(node.getNote().get().getPitch()), 
                        "Cannot contain multiple notes with the same pitch");
            }
        }
        for (T node : playingNotes) {
            if (node.isRest()) {
                ++numRests;
            } else {
                checkArgument(pitches.add(node.getNote().get().getPitch()), 
                        "Cannot contain multiple notes with the same pitch");
            }
        }

        checkArgument(numRests <= 1, "Cannot have multiple rest nodes");
        checkArgument(numRests == 0 || (beginningNotes.size() + playingNotes.size()) == 1,
                "Cannot have both a rest and notes");

        this.isRest            = numRests == 1;
        this.hasBeginningNotes = !beginningNotes.isEmpty();
    }


    //
    //  ~  GETTERS  ~  //
    //

    public abstract ImmutableSet<T> getBeginningNoteNodes();

    public abstract ImmutableSet<T> getPlayingNoteNodes();


    //
    //  ~  STATE  ~  //
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
            return "R" + getPlayingNoteNodes().iterator().next().getRythmnValue().toStaccato();
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
