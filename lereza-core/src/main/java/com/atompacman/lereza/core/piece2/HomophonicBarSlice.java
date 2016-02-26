package com.atompacman.lereza.core.piece2;

import java.util.Set;

import com.atompacman.toolkat.annotations.Implement;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;

@AutoValue
public abstract class HomophonicBarSlice extends BarSlice<HomophonicNoteNode> {

    //
    //  ~  FIELDS  ~  //
    //

    public    abstract ImmutableSet<HomophonicNoteNode> getNoteNodes();
    protected abstract boolean                          areBeginning();
    

    //
    //  ~  INIT  ~  //
    //

    public static HomophonicBarSlice of(HomophonicNoteNode node, boolean isBeginning) {
        return new AutoValue_HomophonicBarSlice(ImmutableSet.of(node), isBeginning);

    }
    
    public static HomophonicBarSlice of(Set<HomophonicNoteNode> nodes, boolean areBeginning) {
        return new AutoValue_HomophonicBarSlice(ImmutableSet.copyOf(nodes), areBeginning);
    }


    //
    //  ~  GETTERS  ~  //
    //
    
    @Implement
    public ImmutableSet<HomophonicNoteNode> getBeginningNoteNodes() {
        return areBeginning() ? getNoteNodes() : ImmutableSet.of();
    }
    
    @Implement
    public ImmutableSet<HomophonicNoteNode> getPlayingNoteNodes() {
        return isRest() ? ImmutableSet.of() : getNoteNodes();
    }
}
