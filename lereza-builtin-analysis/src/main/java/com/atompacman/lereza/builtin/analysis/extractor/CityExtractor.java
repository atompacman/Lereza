package com.atompacman.lereza.builtin.analysis.extractor;

import java.util.Set;

import com.atompacman.lereza.builtin.analysis.structure.City;
import com.atompacman.lereza.builtin.analysis.structure.CountrySubregion;
import com.atompacman.lereza.core.analysis.extractor.SimpleSubstructureExtractor;

public final class CityExtractor extends SimpleSubstructureExtractor<CountrySubregion, City>{

    //
    //  ~  EXTRACT  ~  //
    //

    @Override
    protected Set<City> extractImpl(CountrySubregion structure) {
        return structure.getCities();
    }
}
