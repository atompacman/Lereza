package com.atompacman.lereza.builtin.analysis.analyzer;

import org.apache.commons.lang3.RandomUtils;

import com.atompacman.lereza.builtin.analysis.structure.City;
import com.atompacman.lereza.builtin.analysis.study.MulticulturalCityStudy;
import com.atompacman.lereza.core.analysis.analyzer.Analyzer;
import com.atompacman.lereza.core.analysis.study.DependentStudySet;
import com.atompacman.toolkat.annotations.Implement;

public final class MulticulturalCityAnalyzer extends Analyzer<City, MulticulturalCityStudy> {

    //
    //  ~  ANALYZE  ~  //
    //
    
    @Implement
    public MulticulturalCityStudy analyze(City structure, DependentStudySet dependencies) {
        return new MulticulturalCityStudy(RandomUtils.nextInt(0, 2) == 0);
    }
}
