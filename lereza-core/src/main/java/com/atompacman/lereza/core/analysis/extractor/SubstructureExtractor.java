package com.atompacman.lereza.core.analysis.extractor;

import com.atompacman.lereza.core.analysis.Analysis;
import com.atompacman.lereza.core.analysis.MusicalStructure;

public interface SubstructureExtractor<M extends MusicalStructure, 
                                       N extends MusicalStructure,
                                       A extends Analysis,
                                       B extends Analysis> {

    ExtractionResult<N, A, B> extract(M structure);
}
