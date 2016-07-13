package com.atompacman.lereza.builtin.analysis.extractor;

import java.util.Set;

import com.atompacman.lereza.builtin.analysis.structure.Country;
import com.atompacman.lereza.builtin.analysis.structure.CountrySubregion;
import com.atompacman.lereza.core.analysis.extractor.SimpleSubstructureExtractor;

public final class CountrySubregionExtractor 
    extends SimpleSubstructureExtractor<Country, CountrySubregion> {

    //
    //  ~  EXTRACT  ~  //
    //
    
    @Override
    protected Set<CountrySubregion> extractImpl(Country structure) {
        return structure.getSubregions();
    }
}
