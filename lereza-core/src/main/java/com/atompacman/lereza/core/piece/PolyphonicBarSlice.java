package com.atompacman.lereza.core.piece;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashSet;
import java.util.Set;

import com.atompacman.toolkat.annotations.Implement;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;

@AutoValue
public abstract class PolyphonicBarSlice extends BarSlice<NoteNode> {

    //
    //  ~  FIELDS  ~  //
    //

    @Implement
    public abstract ImmutableSet<NoteNode> getBeginningNoteNodes();
    @Implement
    public abstract ImmutableSet<NoteNode> getPlayingNoteNodes();


    //
    //  ~  INIT  ~  //
    //

    static PolyphonicBarSlice of(Set<GraphNoteNode> beginningNodes,
            Set<GraphNoteNode> begunNodes) {

        Set<GraphNoteNode> playingNotes = new HashSet<>();
        playingNotes.addAll(beginningNodes);
        for (GraphNoteNode node : begunNodes) {
            checkArgument(playingNotes.add(node));
        }
        return new AutoValue_PolyphonicBarSlice(ImmutableSet.copyOf(beginningNodes), 
                                                ImmutableSet.copyOf(playingNotes));
    }
}
