package com.atompacman.lereza.core.analysis.study;

import com.atompacman.lereza.core.analysis.MusicalStructure;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class StudyStructurePair<M extends MusicalStructure, S extends Study> {

    //
    //  ~  FIELDS  ~  //
    //
    
    public abstract M getStructure();
    public abstract S getStudy();
    
    
    //
    //  ~  INIT  ~  //
    //
    
    public static <M extends MusicalStructure, S extends Study> 
    StudyStructurePair<M, S> of(M structure, S study) {
        return new AutoValue_StudyStructurePair<M, S>(structure, study);
    }
}
