package com.atompacman.lereza.core.piece;

import java.util.List;

public class MultiBar extends AbstractBar<MultiNoteStack> {

    //======================================= METHODS ============================================\\

    //------------------------------------- CONSTRUCTORS -----------------------------------------\\

    MultiBar(List<MultiNoteStack> noteStacks) {
        super(noteStacks);
    }
}
