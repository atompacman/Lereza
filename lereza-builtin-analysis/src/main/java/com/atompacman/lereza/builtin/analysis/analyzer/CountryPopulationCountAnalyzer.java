package com.atompacman.lereza.builtin.analysis.analyzer;

import org.apache.commons.lang3.RandomUtils;

import com.atompacman.lereza.builtin.analysis.structure.Country;
import com.atompacman.lereza.builtin.analysis.study.CountryPopulationCountStudy;
import com.atompacman.lereza.core.analysis.analyzer.Analyzer;
import com.atompacman.lereza.core.analysis.study.DependentStudySet;
import com.atompacman.toolkat.annotations.Implement;

public final class CountryPopulationCountAnalyzer 
    extends Analyzer<Country, CountryPopulationCountStudy> {

    //
    //  ~  ANALYZE  ~  //
    //

    @Implement
    public CountryPopulationCountStudy analyze(Country structure, DependentStudySet dependencies) {
        return new CountryPopulationCountStudy(RandomUtils.nextInt(10000, 1000000000));
    }
}
