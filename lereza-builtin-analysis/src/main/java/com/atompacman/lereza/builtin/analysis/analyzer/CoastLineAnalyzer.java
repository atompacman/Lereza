package com.atompacman.lereza.builtin.analysis.analyzer;

import org.apache.commons.lang3.RandomUtils;

import com.atompacman.lereza.builtin.analysis.filter.LandlockedCountryFilter.IsCountryLandlocked;
import com.atompacman.lereza.builtin.analysis.structure.Country;
import com.atompacman.lereza.builtin.analysis.study.CoastLineStudy;
import com.atompacman.lereza.core.analysis.analyzer.Analyzer;
import com.atompacman.lereza.core.analysis.study.DependentStudySet;
import com.atompacman.toolkat.annotations.Implement;

@IsCountryLandlocked
public final class CoastLineAnalyzer extends Analyzer<Country, CoastLineStudy> {

    //
    //  ~  ANALYZE  ~  //
    //
    
    @Implement
    public CoastLineStudy analyze(Country structure, DependentStudySet dependencies) {
        return new CoastLineStudy(RandomUtils.nextDouble(0, 100000));
    }
}
