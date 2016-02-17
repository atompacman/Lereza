package com.atompacman.lereza.core.piece2;

import java.util.LinkedList;
import java.util.List;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
public abstract class GraphBarSlice extends BarSlice<GraphNoteNode> {

    //
    //  ~  FIELDS  ~  //
    //

    @Override
    public abstract ImmutableList<GraphNoteNode> getBeginningNoteNodes();
    @Override
    public abstract ImmutableList<GraphNoteNode> getPlayingNoteNodes();
    

    //
    //  ~  INIT  ~  //
    //

    public static GraphBarSlice of(List<GraphNoteNode> beginningNodes,
                                   List<GraphNoteNode> begunNodes) {
        List<GraphNoteNode> playingNotes = new LinkedList<>();
        playingNotes.addAll(beginningNodes);
        playingNotes.addAll(begunNodes);
        return new AutoValue_GraphBarSlice(ImmutableList.copyOf(beginningNodes), 
                                           ImmutableList.copyOf(playingNotes));

    }
}
