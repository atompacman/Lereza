package com.atompacman.lereza.builtin.analysis.extractor;

import java.util.Set;

import com.atompacman.lereza.builtin.analysis.structure.Earth;
import com.atompacman.lereza.core.analysis.extractor.SimpleSubstructureExtractor;
import com.atompacman.lereza.core.piece.Piece;
import com.google.common.collect.Sets;

public final class WorldExtractor extends SimpleSubstructureExtractor<Piece, Earth> {

    //
    //  ~  EXTRACT  ~  //
    //
    
    @Override
    protected Set<Earth> extractImpl(Piece structure) {
        return Sets.newHashSet(Earth.INSTANCE);
    }
}
