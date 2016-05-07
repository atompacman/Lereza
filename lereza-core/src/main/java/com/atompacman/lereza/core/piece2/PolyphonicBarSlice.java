package com.atompacman.lereza.core.piece2;

public interface PolyphonicBarSlice extends MusicalStructure {

    //
    //  ~  GETTERS  ~  //
    //
    
    NoteNodeSet<PolyphonicNoteNode> getBeginningNoteNodes();

    NoteNodeSet<PolyphonicNoteNode> getPlayingNoteNodes();
    
    
    //
    //  ~  STATUS  ~  //
    //

    boolean hasBeginningNote();

    boolean isRest();
    
    
    //
    //  ~  SERIALIZATION  ~  //
    //
    
    String toStaccato();
}
