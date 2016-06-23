package com.atompacman.lereza.core.analysis.study;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.LinkedHashMap;
import java.util.Map;

public final class StructureStudySet {

    //
    //  ~  FIELDS  ~  //
    //
    
    private final Map<Class<? extends Study>, Study> studies;
    
    
    //
    //  ~  INIT  ~  //
    //
    
    StructureStudySet() {
        this.studies = new LinkedHashMap<>();
    }
    
    
    //
    //  ~  ADD  ~  //
    //
    
    void add(Study study) {
        if (studies.put(study.getClass(), study) != null) {
            throw new IllegalStateException("A study of class \"" + study.getClass() + 
                    "\" was already registered to this structure study set");
        }
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //
    
    @SuppressWarnings("unchecked")
    public <S extends Study> S get(Class<S> studyClass) {
        Study study = studies.get(studyClass);
        checkArgument(study != null);
        return (S) study;
    }
    
    
    //
    //  ~  STATE  ~  //
    //
    
    public boolean contains(Class<? extends Study> studyClass) {
        return studies.containsKey(studyClass);
    }
}
