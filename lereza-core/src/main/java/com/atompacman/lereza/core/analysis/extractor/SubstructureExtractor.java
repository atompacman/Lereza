package com.atompacman.lereza.core.analysis.extractor;

import com.atompacman.lereza.core.analysis.AnalysisComponent;
import com.atompacman.lereza.core.analysis.structure.MusicalStructure;
import com.atompacman.lereza.core.analysis.study.Study;

public abstract class SubstructureExtractor<M1 extends MusicalStructure, 
                                            M2 extends MusicalStructure,
                                            S1 extends Study,
                                            S2 extends Study> implements AnalysisComponent {

    //
    //  ~  EXTRACT  ~  //
    //
    
    public abstract ExtractionResult<M2, S1, S2> extract(M1 structure);
}
