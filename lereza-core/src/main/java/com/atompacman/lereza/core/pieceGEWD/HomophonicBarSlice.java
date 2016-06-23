package com.atompacman.lereza.core.pieceGEWD;

import java.util.Set;

import com.atompacman.toolkat.annotations.Implement;

public interface HomophonicBarSlice extends PolyphonicBarSlice {

    //
    //  ~  INNER TYPES  ~  //
    //
    
    final class Impl extends AbstractBarSlice implements HomophonicBarSlice {
    
        //
        //  ~  FIELDS  ~  //
        //
    
        private final NoteNodeSet<HomophonicNoteNode> nodes;
        private final boolean                         areBeginning;
    
    
        //
        //  ~  INIT  ~  //
        //
    
        Impl(Set<HomophonicNoteNode> node, boolean isBeginning) {
            this.nodes        = NoteNodeSet.of(node);
            this.areBeginning = isBeginning;
            
            postInit();
        }
        
    
        //
        //  ~  GETTERS  ~  //
        //
    
        @Implement
        public NoteNodeSet<PolyphonicNoteNode> getBeginningNodes() {
            return areBeginning ? getPlayingNodes() : NoteNodeSet.empty();
        }
    
        @Implement
        @SuppressWarnings({ "unchecked", "rawtypes" })
        public NoteNodeSet<PolyphonicNoteNode> getPlayingNodes() {
            return (NoteNodeSet<PolyphonicNoteNode>) (NoteNodeSet) nodes;
        }
        
        @Implement
        public NoteNodeSet<HomophonicNoteNode> getNodes() {
            return nodes;
        }
        
        
        //
        //  ~  STATUS  ~  //
        //
    
        @Implement
        public boolean areNodesBeginning() {
            return areBeginning;
        }
    }


    //
    //  ~  GETTERS  ~  //
    //
    
    NoteNodeSet<HomophonicNoteNode> getNodes();
    
    
    //
    //  ~  STATE  ~  //
    //

    boolean areNodesBeginning();
}
