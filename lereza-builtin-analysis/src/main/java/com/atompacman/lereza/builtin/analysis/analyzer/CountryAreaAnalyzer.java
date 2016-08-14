package com.atompacman.lereza.builtin.analysis.analyzer;

import com.atompacman.lereza.builtin.analysis.structure.Country;
import com.atompacman.lereza.builtin.analysis.study.CountryAreaStudy;
import com.atompacman.lereza.core.analysis.analyzer.Analyzer;
import com.atompacman.lereza.core.analysis.study.DependentStudySet;
import com.atompacman.toolkat.annotations.Implement;

public final class CountryAreaAnalyzer extends Analyzer<Country, CountryAreaStudy> {

    //
    //  ~  ANALYSIS  ~  //
    //

    @Implement
    public CountryAreaStudy analyze(Country structure, DependentStudySet dependencies) {
        return new CountryAreaStudy(structure.getAreaSqrKm());
    }
}
