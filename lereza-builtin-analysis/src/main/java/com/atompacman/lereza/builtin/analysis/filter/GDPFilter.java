package com.atompacman.lereza.builtin.analysis.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Comparator;

import com.atompacman.lereza.builtin.analysis.filter.GDPFilter.GDPTop;
import com.atompacman.lereza.builtin.analysis.structure.Country;
import com.atompacman.lereza.builtin.analysis.study.GDPStudy;
import com.atompacman.lereza.core.analysis.filter.NumberOfEntryLimiterFilter;
import com.atompacman.lereza.core.analysis.study.StructureTypeStudySet;
import com.atompacman.lereza.core.analysis.study.StudyDependencies;
import com.atompacman.toolkat.annotations.Implement;
import com.atompacman.toolkat.annotations.SubMethodOf;

@StudyDependencies(GDPStudy.class)
public final class GDPFilter extends NumberOfEntryLimiterFilter<GDPTop, Country> {

    //
    //  ~  INNER TYPES  ~  //
    //
    
    @Target    (ElementType.TYPE)
    @Retention (RetentionPolicy.RUNTIME)
    public @interface GDPTop {
        
        int value();
    }
    

    //
    //  ~  APPLY  ~  //
    //

    @Implement
    protected int getMaxNumberOfEntries(GDPTop annotationData) {
        return annotationData.value();
    }

    @Override
    protected Comparator<Country> createComparator(StructureTypeStudySet<Country> studies,
                                                   GDPTop                         annotationData) {

        return new Comparator<Country>() {
            
            @Implement
            public int compare(Country o1, Country o2) {
                return Double.compare(getGDPOf(o1), getGDPOf(o2));
            }
            
            @SubMethodOf("compare")
            private double getGDPOf(Country country) {
                return studies.getStudySet(country).get(GDPStudy.class).getGDP();
            }
        };
    }
}
