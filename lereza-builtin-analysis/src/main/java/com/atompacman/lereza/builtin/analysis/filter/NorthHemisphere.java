package com.atompacman.lereza.builtin.analysis.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.atompacman.lereza.builtin.analysis.filter.NorthHemisphere.IsInHemisphere;
import com.atompacman.lereza.builtin.analysis.structure.Country;
import com.atompacman.lereza.core.analysis.filter.PerStructureFilter;
import com.atompacman.lereza.core.analysis.study.StructureStudySet;
import com.atompacman.toolkat.annotations.Implement;

public final class NorthHemisphere extends PerStructureFilter<IsInHemisphere, Country> {

    //
    //  ~  INNER TYPES  ~  //
    //

    public enum Hemisphere {
        NORTH, SOUTH
    }
    
    @Target    (ElementType.TYPE)
    @Retention (RetentionPolicy.RUNTIME)
    public @interface IsInHemisphere {
        
        Hemisphere value();
    }

    //
    //  ~  APPLY  ~  //
    //
    
    @Implement
    protected boolean apply(Country           structure,
                            StructureStudySet studies,
                            IsInHemisphere    annotationData) {
        
        return (structure.getLatitude() > 0) == (annotationData.value() == Hemisphere.NORTH);
    }
}
