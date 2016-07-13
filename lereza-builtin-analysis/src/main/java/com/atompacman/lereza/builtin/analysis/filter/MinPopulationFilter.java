package com.atompacman.lereza.builtin.analysis.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.atompacman.lereza.builtin.analysis.filter.MinPopulationFilter.MinPopulation;
import com.atompacman.lereza.builtin.analysis.structure.Country;
import com.atompacman.lereza.builtin.analysis.study.CountryPopulationCountStudy;
import com.atompacman.lereza.core.analysis.filter.PerStructureFilter;
import com.atompacman.lereza.core.analysis.study.StructureStudySet;
import com.atompacman.lereza.core.analysis.study.StudyDependencies;
import com.atompacman.toolkat.annotations.Implement;

@StudyDependencies(CountryPopulationCountStudy.class)
public final class MinPopulationFilter extends PerStructureFilter<MinPopulation, Country> {

    //
    //  ~  INNER TYPES  ~  //
    //
    
    @Target    (ElementType.TYPE)
    @Retention (RetentionPolicy.RUNTIME)
    public @interface MinPopulation {
        
        int value();
    }


    //
    //  ~  APPLY  ~  //
    //

    @Implement
    protected boolean apply(Country           structure, 
                            StructureStudySet studies,
                            MinPopulation     annotationData) {

        return studies.get(CountryPopulationCountStudy.class).getPopulation() > 800000000;
    }
}
