package com.atompacman.lereza.core.analysis.extractor;

import java.util.Set;

import com.atompacman.lereza.core.analysis.MusicalStructure;
import com.atompacman.lereza.core.analysis.study.StudyStructurePair;
import com.atompacman.lereza.core.analysis.study.Study;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ExtractionResult<M  extends MusicalStructure, 
                                       S1 extends Study, 
                                       S2 extends Study> {

    //
    //  ~  FIELDS  ~  //
    //
    
    public abstract Set<StudyStructurePair<M, S1>> getExtractedSubstructures();
    public abstract S2                             getExtractionStudy();
    
    
    //
    //  ~  INIT  ~  //
    //
    
    public static <M extends MusicalStructure, S1 extends Study, S2 extends Study> 
    ExtractionResult<M, S1, S2> of(Set<StudyStructurePair<M, S1>> extractionSubstructures, 
                                   S2                             extractionStudy) {
        
        return new AutoValue_ExtractionResult<M,S1,S2>(extractionSubstructures, extractionStudy);
    }
}
