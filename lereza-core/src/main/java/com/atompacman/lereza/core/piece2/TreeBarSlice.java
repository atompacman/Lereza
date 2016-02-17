package com.atompacman.lereza.core.piece2;

import java.util.List;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
public abstract class TreeBarSlice extends BarSlice<TreeNoteNode> {

    //
    //  ~  FIELDS  ~  //
    //

    public    abstract ImmutableList<TreeNoteNode> getNoteNodes();
    protected abstract boolean                     areBeginning();
    

    //
    //  ~  INIT  ~  //
    //

    public static TreeBarSlice of(TreeNoteNode node, boolean isBeginning) {
        return new AutoValue_TreeBarSlice(ImmutableList.of(node), isBeginning);

    }
    
    public static TreeBarSlice of(List<TreeNoteNode> nodes, boolean areBeginning) {
        return new AutoValue_TreeBarSlice(ImmutableList.copyOf(nodes), areBeginning);
    }


    //
    //  ~  GETTERS  ~  //
    //
    
    @Override
    public ImmutableList<TreeNoteNode> getBeginningNoteNodes() {
        return areBeginning() ? getNoteNodes() : ImmutableList.of();
    }
    
    @Override
    public ImmutableList<TreeNoteNode> getPlayingNoteNodes() {
        return isRest() ? ImmutableList.of() : getNoteNodes();
    }
}
