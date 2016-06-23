package com.atompacman.lereza.core.piece;

import com.atompacman.lereza.core.piece2.AutoValue_MonophonicBarSlice;
import com.atompacman.toolkat.annotations.Implement;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;

@AutoValue
public abstract class MonophonicBarSlice extends BarSlice<MonophonicNoteNode> {

    //
    //  ~  FIELDS  ~  //
    //

    public    abstract MonophonicNoteNode getNoteNode();
    protected abstract boolean            isBeginning();


    //
    //  ~  INIT  ~  //
    //

    static MonophonicBarSlice of(MonophonicNoteNode node, boolean isBeginning) {
        return new AutoValue_MonophonicBarSlice(node, isBeginning);
    }


    //
    //  ~  GETTERS  ~  //
    //

    @Implement
    public ImmutableSet<MonophonicNoteNode> getBeginningNoteNodes() {
        return isBeginning() ? ImmutableSet.of(getNoteNode()) : ImmutableSet.of();
    }

    @Implement
    public ImmutableSet<MonophonicNoteNode> getPlayingNoteNodes() {
        return isRest() ? ImmutableSet.of() : ImmutableSet.of(getNoteNode());
    }
}
