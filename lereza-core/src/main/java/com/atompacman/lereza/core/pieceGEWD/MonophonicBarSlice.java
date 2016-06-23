package com.atompacman.lereza.core.pieceGEWD;

import com.atompacman.toolkat.annotations.Implement;

public interface MonophonicBarSlice extends HomophonicBarSlice {

    //
    //  ~  INNER TYPES  ~  //
    //
    
    final class Impl extends AbstractBarSlice implements MonophonicBarSlice {
    
        //
        //  ~  FIELDS  ~  //
        //
    
        private final MonophonicNoteNode node;
        private final boolean            isBeginning;
    
    
        //
        //  ~  INIT  ~  //
        //
    
        Impl(MonophonicNoteNode node, boolean isBeginning) {
            this.node        = node;
            this.isBeginning = isBeginning;
            
            postInit();
        }
    
    
        //
        //  ~  GETTERS  ~  //
        //
    
        @Implement
        public NoteNodeSet<PolyphonicNoteNode> getBeginningNodes() {
            return isBeginning ? NoteNodeSet.of(node) : NoteNodeSet.empty();
        }
    
        @Implement
        public NoteNodeSet<PolyphonicNoteNode> getPlayingNodes() {
            return NoteNodeSet.of(node);
        }
        
        @Implement
        public NoteNodeSet<HomophonicNoteNode> getNodes() {
            return NoteNodeSet.of(node);
        }
        
        @Implement
        public MonophonicNoteNode getNode() {
            return node;
        }
        
        
        //
        //  ~  STATE  ~  //
        //
    
        @Implement
        public boolean areNodesBeginning() {
            return isBeginning;
        }
    }

    
    //
    //  ~  GETTERS  ~  //
    //
    
    MonophonicNoteNode getNode();
}
