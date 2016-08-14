package com.atompacman.lereza.builtin.analysis.extractor;

import java.util.Set;

import com.atompacman.lereza.builtin.analysis.structure.Continent;
import com.atompacman.lereza.builtin.analysis.structure.Earth;
import com.atompacman.lereza.core.analysis.extractor.SimpleSubstructureExtractor;

public final class ContinentExtractor extends SimpleSubstructureExtractor<Earth, Continent> {

    //
    //  ~  EXTRACT  ~  //
    //
    
    public Set<Continent> extractImpl(Earth structure) {
        return structure.getContinents();
    }
}
