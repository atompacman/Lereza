package com.atompacman.lereza.core.piece2;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.atompacman.lereza.core.theory.Pitch;
import com.atompacman.toolkat.annotations.DerivableFrom;
import com.atompacman.toolkat.annotations.Implement;

abstract class AbstractBarSlice implements PolyphonicBarSlice {

    //
    //  ~  FIELDS  ~  //
    //

    @DerivableFrom("getBeginningNoteNodes()")
    private boolean isRest;
    @DerivableFrom("getBeginningNoteNodes()")
    private boolean hasBeginningNotes;


    //
    //  ~  INIT  ~  //
    //

    @SuppressWarnings("unchecked")
    protected <T extends PolyphonicNoteNode> void initStatus() {
        NoteNodeSet<T> beginningNotes = (NoteNodeSet<T>) getBeginningNoteNodes();
        NoteNodeSet<T> playingNotes   = (NoteNodeSet<T>) getPlayingNoteNodes();
        
        checkArgument(playingNotes.nodes.values().containsAll(beginningNotes.nodes.values()), 
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
        checkArgument(numRests == 0 || (beginningNotes.numNodes() + playingNotes.numNodes()) == 1,
                "Cannot have both a rest and notes");

        this.isRest            = numRests == 1;
        this.hasBeginningNotes = !beginningNotes.isEmpty();
    }
    

    //
    //  ~  STATE  ~  //
    //

    @Implement
    public boolean hasBeginningNote() {
        return hasBeginningNotes;
    }

    @Implement
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

    @Implement
    public String toStaccato() {
        if (!hasBeginningNote()) {
            return "";
        }
        if (isRest()) {
            return "R" + getPlayingNoteNodes().iterator().next().getRythmnValue().toStaccato();
        }
        StringBuilder sb = new StringBuilder();
        Iterator<? extends PolyphonicNoteNode> it = getBeginningNoteNodes().iterator();
        sb.append(it.next().toStaccato());
        while (it.hasNext()) {
            sb.append('+').append(it.next().toStaccato());
        }
        return sb.toString();
    }
}
