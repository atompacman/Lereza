package com.atompacman.lereza.core.profile.drum;

import java.nio.ByteBuffer;

public class PercussionPattern {

    //======================================= FIELDS =============================================\\

    private final PercussionElement elem;
    private final boolean[]         hits;



    //======================================= METHODS ============================================\\

    //------------------------------------- CONSTRUCTORS -----------------------------------------\\

    public PercussionPattern(PercussionElement elem, int length) {
        this(elem, new boolean[length]);
    }

    public PercussionPattern(PercussionElement elem, boolean[] hits) {
        this.elem = elem;
        this.hits = hits;
    }

    //- - - - - - - - - - - - - - - - - - - FROM BINARY - - - - - - - - - - - - - - - - - - - - - \\

    public PercussionPattern(ByteBuffer buffer) {
        this.elem = PercussionElement.withHexNote(buffer.get());
        this.hits = new boolean[buffer.get()];
        int numHits = buffer.get();
        for (int i = 0; i < numHits; ++i) {
            hits[buffer.get()] = true;
        }
    }
    
    
    //--------------------------------------- SETTERS --------------------------------------------\\

    public void addHit(Integer hit) {
        if (hit < 0) {
            throw new IllegalArgumentException("Hit position cannot be negative");
        }
        if (hit >= hits.length) {
            throw new IllegalArgumentException("Hit position cannot exceed pattern length");
        }
        hits[hit] = true;
    }


    //--------------------------------------- GETTERS --------------------------------------------\\

    public PercussionElement getPercussionElement() {
        return elem;
    }

    public int getLengthTU() {
        return hits.length;
    }

    public boolean hasAHit(int tu) {
        return hits[tu];
    }
}
