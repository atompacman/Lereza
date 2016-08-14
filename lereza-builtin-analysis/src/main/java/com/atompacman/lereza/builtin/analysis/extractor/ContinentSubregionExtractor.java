package com.atompacman.lereza.builtin.analysis.extractor;

import java.util.Set;

import com.atompacman.lereza.builtin.analysis.structure.Continent;
import com.atompacman.lereza.builtin.analysis.structure.ContinentSubregion;
import com.atompacman.lereza.core.analysis.extractor.SimpleSubstructureExtractor;

public final class ContinentSubregionExtractor 
    extends SimpleSubstructureExtractor<Continent, ContinentSubregion> {

    //
    //  ~  EXTRACT  ~  //
    //

    @Override
    protected Set<ContinentSubregion> extractImpl(Continent structure) {
        return structure.getSubregions();
    }
}
