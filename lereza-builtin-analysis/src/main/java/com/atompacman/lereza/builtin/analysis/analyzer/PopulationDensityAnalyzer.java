package com.atompacman.lereza.builtin.analysis.analyzer;

import com.atompacman.lereza.builtin.analysis.structure.Country;
import com.atompacman.lereza.builtin.analysis.study.CountryAreaStudy;
import com.atompacman.lereza.builtin.analysis.study.CountryPopulationCountStudy;
import com.atompacman.lereza.builtin.analysis.study.PopulationDensityStudy;
import com.atompacman.lereza.core.analysis.analyzer.Analyzer;
import com.atompacman.lereza.core.analysis.study.DependentStudySet;
import com.atompacman.lereza.core.analysis.study.StudyDependencies;
import com.atompacman.toolkat.annotations.Implement;

@StudyDependencies({ CountryPopulationCountStudy.class, CountryAreaStudy.class })
public final class PopulationDensityAnalyzer extends Analyzer<Country, PopulationDensityStudy> {

    //
    //  ~  ANALYSIS  ~  //
    //

    @Implement
    public PopulationDensityStudy analyze(Country structure, DependentStudySet dependencies) {
        return new PopulationDensityStudy(
                (double) dependencies.get(CountryPopulationCountStudy.class).getPopulation() 
                / dependencies.get(CountryAreaStudy.class).getArea());
    }
}
