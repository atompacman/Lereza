package com.atompacman.lereza.core.piece2;

public interface HomophonicBarSlice extends PolyphonicBarSlice {

    //
    //  ~  GETTERS  ~  //
    //
    
    NoteNodeSet<HomophonicNoteNode> getNoteNodes();
    
    
    //
    //  ~  STATUS  ~  //
    //

    boolean areNotesBeginning();
}
