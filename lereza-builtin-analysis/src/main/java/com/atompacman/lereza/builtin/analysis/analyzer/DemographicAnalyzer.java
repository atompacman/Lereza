package com.atompacman.lereza.builtin.analysis.analyzer;

import com.atompacman.lereza.builtin.analysis.structure.State;
import com.atompacman.lereza.builtin.analysis.study.DemographicStudy;
import com.atompacman.lereza.core.analysis.analyzer.Analyzer;
import com.atompacman.lereza.core.analysis.study.DependentStudySet;

public final class DemographicAnalyzer extends Analyzer<State, DemographicStudy>{

    @Override
    public DemographicStudy analyze(State structure, DependentStudySet dependencies) {
        return new DemographicStudy(0.2);
    }
}
