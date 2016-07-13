package com.atompacman.lereza.builtin.analysis.analyzer;

import com.atompacman.lereza.builtin.analysis.filter.GDPFilter.GDPTop;
import com.atompacman.lereza.builtin.analysis.structure.Country;
import com.atompacman.lereza.builtin.analysis.study.HighGDPCountryStudy;
import com.atompacman.lereza.core.analysis.analyzer.Analyzer;
import com.atompacman.lereza.core.analysis.study.DependentStudySet;
import com.atompacman.toolkat.annotations.Implement;

@GDPTop(5)
public final class HighGDPCountryAnalyzer extends Analyzer<Country, HighGDPCountryStudy> {

    //
    //  ~  ANALYZE  ~  //
    //

    @Implement
    public HighGDPCountryStudy analyze(Country structure, DependentStudySet dependencies) {
        return new HighGDPCountryStudy();
    }
}
