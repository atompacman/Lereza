package com.atompacman.lereza.core.piece;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.atompacman.lereza.core.theory.Pitch;
import com.atompacman.toolkat.annotations.DerivableFrom;
import com.atompacman.toolkat.annotations.Implement;

public interface PolyphonicBarSlice extends MusicalStructure {

    //
    //  ~  INNER TYPES  ~  //
    //
    
    abstract class AbstractBarSlice implements PolyphonicBarSlice {
    
        //
        //  ~  FIELDS  ~  //
        //
    
        @DerivableFrom("getBeginningNoteNodes()")
        private boolean isRest;
        @DerivableFrom("getBeginningNoteNodes()")
        private boolean hasBeginningNotes;
    
    
        //
        //  ~  INIT  ~  //
        //
    
        protected void postInit() {
            checkArgument(getPlayingNodes()  .notes.values().containsAll(
                          getBeginningNodes().notes.values()), 
                    "Playing notes must contain all beginning notes");
    
            Set<Pitch> pitches = new HashSet<>();
            int numRests = 0;
    
            for (PolyphonicNoteNode node : getPlayingNodes()) {
                if (node.isRest()) {
                    ++numRests;
                } else {
                    checkArgument(pitches.add(node.getNote().get().getPitch()), 
                            "Cannot contain multiple notes with the same pitch");
                }
            }
    
            checkArgument(numRests <= 1, "Stacks cannot have multiple rest nodes");

            this.isRest = numRests == 1;

            if (isRest) {
                checkArgument(getPlayingNodes().numNodes()==1, "Cannot have both a rest and notes");
            }
    
            this.hasBeginningNotes = !getBeginningNodes().isEmpty();
        }
        
    
        //
        //  ~  STATE  ~  //
        //
    
        @Implement
        public boolean hasBeginningNodes() {
            return hasBeginningNotes;
        }
    
        @Implement
        public boolean isRest() {
            return isRest;
        }
    
    
        //
        //  ~  SERIALIZATION  ~  //
        //
    
        @Override
        public String toString() {
            return toStaccato();
        }
    
        @Implement
        public String toStaccato() {
            if (!hasBeginningNodes()) {
                return "";
            }
            if (isRest()) {
                return "R" + getPlayingNodes().iterator().next().getRythmnValue().toStaccato();
            }
            StringBuilder sb = new StringBuilder();
            Iterator<? extends PolyphonicNoteNode> it = getBeginningNodes().iterator();
            sb.append(it.next().toStaccato());
            while (it.hasNext()) {
                sb.append('+').append(it.next().toStaccato());
            }
            return sb.toString();
        }
    }

    final class Impl extends AbstractBarSlice {
    
        //
        //  ~  FIELDS  ~  //
        //
    
        private NoteNodeSet<PolyphonicNoteNode> beginningNodes;
        private NoteNodeSet<PolyphonicNoteNode> playingNodes;
    
    
        //
        //  ~  INIT  ~  //
        //
    
        Impl(Set<PolyphonicNoteNode> beginningNodes, Set<PolyphonicNoteNode> playingNodes) {
            this.beginningNodes = NoteNodeSet.of(beginningNodes);
            this.playingNodes   = NoteNodeSet.of(playingNodes);
            
            postInit();
        }
        
        
        //
        //  ~  GETTERS  ~  //
        //
    
        @Implement
        public NoteNodeSet<PolyphonicNoteNode> getBeginningNodes() {
            return beginningNodes;
        }
        
        @Implement
        public NoteNodeSet<PolyphonicNoteNode> getPlayingNodes() {
            return playingNodes;
        }
    }
    

    //
    //  ~  GETTERS  ~  //
    //
    
    NoteNodeSet<PolyphonicNoteNode> getBeginningNodes();

    NoteNodeSet<PolyphonicNoteNode> getPlayingNodes();
    
    
    //
    //  ~  STATE  ~  //
    //
    
    boolean hasBeginningNodes();

    boolean isRest();
    
    
    //
    //  ~  SERIALIZATION  ~  //
    //
    
    String toStaccato();
}
