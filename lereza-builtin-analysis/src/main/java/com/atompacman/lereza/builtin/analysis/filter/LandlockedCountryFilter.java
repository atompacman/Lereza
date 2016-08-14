package com.atompacman.lereza.builtin.analysis.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.atompacman.lereza.builtin.analysis.filter.LandlockedCountryFilter.IsCountryLandlocked;
import com.atompacman.lereza.builtin.analysis.structure.Country;
import com.atompacman.lereza.core.analysis.filter.PerStructureFilter;
import com.atompacman.lereza.core.analysis.study.StructureStudySet;

public final class LandlockedCountryFilter extends PerStructureFilter<IsCountryLandlocked, Country>{

    //
    //  ~  INNER TYPES  ~  //
    //
    
    @Target    (ElementType.TYPE)
    @Retention (RetentionPolicy.RUNTIME)
    public @interface IsCountryLandlocked {
        
        boolean value() default true;
    }

    
    //
    //  ~  APPLY  ~  //
    //

    @Override
    protected boolean apply(Country             structure, 
                            StructureStudySet   studies, 
                            IsCountryLandlocked annotationData) {
        
        return structure.isLandlocked() == annotationData.value();
    }
}
