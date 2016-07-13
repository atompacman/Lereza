package com.atompacman.lereza.builtin.analysis.analyzer;

import com.atompacman.lereza.builtin.analysis.filter.MinPopulationFilter.MinPopulation;
import com.atompacman.lereza.builtin.analysis.structure.Country;
import com.atompacman.lereza.builtin.analysis.study.OverpopulatedCountryStudy;
import com.atompacman.lereza.core.analysis.analyzer.Analyzer;
import com.atompacman.lereza.core.analysis.study.DependentStudySet;

@MinPopulation(80000000)
public final class OverpopulatedCountryAnalyzer extends Analyzer<Country,OverpopulatedCountryStudy>{

    //
    //  ~  ANALYZER  ~  //
    //

    @Override
    public OverpopulatedCountryStudy analyze(Country structure, DependentStudySet dependencies) {
        return new OverpopulatedCountryStudy();
    }
}
