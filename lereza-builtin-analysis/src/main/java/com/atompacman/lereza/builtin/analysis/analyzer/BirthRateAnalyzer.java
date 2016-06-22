package com.atompacman.lereza.builtin.analysis.analyzer;

import com.atompacman.lereza.builtin.analysis.study.BirthRateStudy;
import com.atompacman.lereza.builtin.analysis.study.DemographicStudy;
import com.atompacman.lereza.builtin.analysis.testPiece.Country;
import com.atompacman.lereza.core.analysis.analyzer.Analyzer;
import com.atompacman.lereza.core.analysis.filter.ScoreBasedNumberOfEntryFilter.LimitNumberOfEntriesBasedOnScore;
import com.atompacman.lereza.core.analysis.filter.ScoredStudyFilter.MinimumScore;
import com.atompacman.lereza.core.analysis.study.DependentStudySet;
import com.atompacman.lereza.core.analysis.study.StudyDependencies;

@StudyDependencies(DemographicStudy.class)
@MinimumScore(study = DemographicStudy.class, score = 0.5)
@LimitNumberOfEntriesBasedOnScore(max = 5, orderedCriterias = DemographicStudy.class)
public final class BirthRateAnalyzer extends Analyzer<Country, BirthRateStudy>{

    @Override
    public BirthRateStudy analyze(Country structure, DependentStudySet dependencies) {
        return new BirthRateStudy(3.43);
    }
}
