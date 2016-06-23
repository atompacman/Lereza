package com.atompacman.lereza.core.analysis.extractor;

import java.util.HashSet;
import java.util.Set;

import com.atompacman.lereza.core.analysis.study.NoStudy;
import com.atompacman.lereza.core.analysis.study.StudyStructurePair;
import com.atompacman.lereza.core.analysis.MusicalStructure;

public abstract class SimpleSubstructureExtractor<M1 extends MusicalStructure, 
                                                  M2 extends MusicalStructure>   
    implements SubstructureExtractor<M1, M2, NoStudy, NoStudy>{

    //
    //  ~  EXTRACT  ~  //
    //
    
    public ExtractionResult<M2, NoStudy, NoStudy> extract(M1 structure) {
        Set<StudyStructurePair<M2, NoStudy>> set = new HashSet<>();
        extractImpl(structure).forEach(n -> set.add(StudyStructurePair.of(n, NoStudy.INSTANCE)));
        return ExtractionResult.of(set, NoStudy.INSTANCE);
    }
    
    protected abstract Set<M2> extractImpl(M1 structure);
}
