package com.atompacman.lereza.core.piece2;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
public abstract class SequenceBarSlice extends BarSlice<SequenceNoteNode> {

    //
    //  ~  FIELDS  ~  //
    //

    public    abstract SequenceNoteNode getNoteNode();
    protected abstract boolean          isBeginning();
    

    //
    //  ~  INIT  ~  //
    //

    public static SequenceBarSlice of(SequenceNoteNode node, boolean isBeginning) {
        return new AutoValue_SequenceBarSlice(node, isBeginning);
    }


    //
    //  ~  GETTERS  ~  //
    //
    
    @Override
    public ImmutableList<SequenceNoteNode> getBeginningNoteNodes() {
        return isBeginning() ? ImmutableList.of(getNoteNode()) : ImmutableList.of();
    }
    
    @Override
    public ImmutableList<SequenceNoteNode> getPlayingNoteNodes() {
        return isRest() ? ImmutableList.of() : ImmutableList.of(getNoteNode());
    }
}
