package com.atompacman.lereza.builtin.analysis.extractor;

import java.util.Set;

import com.atompacman.lereza.builtin.analysis.testPiece.Continent;
import com.atompacman.lereza.builtin.analysis.testPiece.World;
import com.atompacman.lereza.core.analysis.extractor.SimpleSubstructureExtractor;

public final class ContinentExtractor
    extends SimpleSubstructureExtractor<World, Continent> {

    //
    //  ~  EXTRACT  ~  //
    //
    
    public Set<Continent> extractImpl(World structure) {
        return structure.getContinents();
    }
}
