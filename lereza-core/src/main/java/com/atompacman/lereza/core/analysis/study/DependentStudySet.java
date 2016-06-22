package com.atompacman.lereza.core.analysis.study;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class DependentStudySet {

    //
    //  ~  FIELDS  ~  //
    //
    
    private final Map<Class<? extends Study>, Study> studies;
    
    
    //
    //  ~  INIT  ~  //
    //
    
    public DependentStudySet(Set<Study> studies) {
        this.studies = new HashMap<>();
        studies.stream().forEach(s -> { 
            if (this.studies.put(s.getClass(), s) != null) {
                throw new IllegalArgumentException("Can't have multiple studies of the same class");
            }
        });
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //
    
    public <T extends Study> T get(Class<T> studyClass) {
        @SuppressWarnings("unchecked")
        T study = (T) studies.get(studyClass);
        if (study == null) {
            throw new IllegalArgumentException("Dependency set does not contain "
                    + "a study of type \"" + studyClass.getName() + "\"");
        }
        return study;
    }
}
