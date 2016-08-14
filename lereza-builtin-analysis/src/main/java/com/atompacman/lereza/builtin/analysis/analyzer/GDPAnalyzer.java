package com.atompacman.lereza.builtin.analysis.analyzer;

import org.apache.commons.lang3.RandomUtils;

import com.atompacman.lereza.builtin.analysis.structure.Country;
import com.atompacman.lereza.builtin.analysis.study.GDPStudy;
import com.atompacman.lereza.core.analysis.analyzer.Analyzer;
import com.atompacman.lereza.core.analysis.study.DependentStudySet;
import com.atompacman.toolkat.annotations.Implement;

public final class GDPAnalyzer extends Analyzer<Country, GDPStudy> {

    //
    //  ~  ANALYZE  ~  //
    //

    @Implement
    public GDPStudy analyze(Country structure, DependentStudySet dependencies) {
        return new GDPStudy(RandomUtils.nextDouble(100, 100000));
    }
}
