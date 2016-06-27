package com.atompacman.lereza.builtin.analysis.extractor;

import java.util.HashSet;
import java.util.Set;

import com.atompacman.lereza.builtin.analysis.structure.Continent;
import com.atompacman.lereza.builtin.analysis.structure.Country;
import com.atompacman.lereza.core.analysis.extractor.SimpleSubstructureExtractor;

public final class CountryExtractor extends SimpleSubstructureExtractor<Continent, Country> {

    //
    //  ~  EXTRACT  ~  //
    //
    
    public Set<Country> extractImpl(Continent structure) {
        return new HashSet<>();
    }
}
