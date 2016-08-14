package com.atompacman.lereza.builtin.analysis.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Comparator;

import com.atompacman.lereza.builtin.analysis.filter.TopPopulationFilter.PopulationTop;
import com.atompacman.lereza.builtin.analysis.structure.Country;
import com.atompacman.lereza.builtin.analysis.study.CountryPopulationCountStudy;
import com.atompacman.lereza.core.analysis.filter.NumberOfEntryLimiterFilter;
import com.atompacman.lereza.core.analysis.study.StructureTypeStudySet;
import com.atompacman.lereza.core.analysis.study.StudyDependencies;
import com.atompacman.toolkat.annotations.Implement;
import com.atompacman.toolkat.annotations.SubMethodOf;

@StudyDependencies(CountryPopulationCountStudy.class)
public final class TopPopulationFilter extends NumberOfEntryLimiterFilter<PopulationTop, Country> {

    //
    //  ~  INNER TYPES  ~  //
    //
    
    @Target    (ElementType.TYPE)
    @Retention (RetentionPolicy.RUNTIME)
    public @interface PopulationTop {
        
        int value();
    }
    

    //
    //  ~  APPLY  ~  //
    //

    @Implement
    protected int getMaxNumberOfEntries(PopulationTop annotationData) {
        return annotationData.value();
    }

    @Override
    protected Comparator<Country> createComparator(StructureTypeStudySet<Country> studies,
                                                   PopulationTop                  annotationData) {

        return new Comparator<Country>() {
            
            @Implement
            public int compare(Country o1, Country o2) {
                return Integer.compare(getPopulationOf(o1), getPopulationOf(o2));
            }
            
            @SubMethodOf("compare")
            private int getPopulationOf(Country country) {
                return studies.getStudySet(country)
                              .get(CountryPopulationCountStudy.class)
                              .getPopulation();
            }
        };
    }
}
