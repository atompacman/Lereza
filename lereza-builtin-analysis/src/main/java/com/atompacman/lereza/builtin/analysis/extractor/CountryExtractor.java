package com.atompacman.lereza.builtin.analysis.extractor;

import java.util.Set;

import com.atompacman.lereza.builtin.analysis.structure.ContinentSubregion;
import com.atompacman.lereza.builtin.analysis.structure.Country;
import com.atompacman.lereza.core.analysis.extractor.SimpleSubstructureExtractor;

public final class CountryExtractor 
    extends SimpleSubstructureExtractor<ContinentSubregion, Country> {

    //
    //  ~  EXTRACT  ~  //
    //
    
    public Set<Country> extractImpl(ContinentSubregion structure) {
        return structure.getCountries();
    }
}
