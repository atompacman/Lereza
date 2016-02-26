package com.atompacman.lereza.core.piece2;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashSet;
import java.util.Set;

import com.atompacman.toolkat.annotations.Implement;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;

@AutoValue
public abstract class PolyphonicBarSlice extends BarSlice<PolyphonicNoteNode> {

    //
    //  ~  FIELDS  ~  //
    //

    @Implement
    public abstract ImmutableSet<PolyphonicNoteNode> getBeginningNoteNodes();
    @Implement
    public abstract ImmutableSet<PolyphonicNoteNode> getPlayingNoteNodes();
    

    //
    //  ~  INIT  ~  //
    //

    public static PolyphonicBarSlice of(Set<PolyphonicNoteNode> beginningNodes,
                                        Set<PolyphonicNoteNode> begunNodes) {
        Set<PolyphonicNoteNode> playingNotes = new HashSet<>();
        playingNotes.addAll(beginningNodes);
        for (PolyphonicNoteNode node : begunNodes) {
            checkArgument(playingNotes.add(node));
        }
        return new AutoValue_PolyphonicBarSlice(ImmutableSet.copyOf(beginningNodes), 
                                                ImmutableSet.copyOf(playingNotes));
    }
}
