package com.atompacman.lereza.core.analysis.filter;

import com.atompacman.lereza.core.analysis.MusicalStructure;

public interface Filter<M extends MusicalStructure> {

    //
    //  ~  APPLY  ~  //
    //

    boolean apply(M structure);
}
