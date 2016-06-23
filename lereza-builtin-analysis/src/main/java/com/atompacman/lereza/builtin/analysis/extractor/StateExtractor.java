package com.atompacman.lereza.builtin.analysis.extractor;

import java.util.HashSet;
import java.util.Set;

import com.atompacman.lereza.builtin.analysis.testPiece.Continent;
import com.atompacman.lereza.builtin.analysis.testPiece.State;
import com.atompacman.lereza.core.analysis.extractor.SimpleSubstructureExtractor;

public final class StateExtractor
    extends SimpleSubstructureExtractor<Continent, State> {

    //
    //  ~  EXTRACT  ~  //
    //
    
    public Set<State> extractImpl(Continent structure) {
        Set<State> states = new HashSet<>();
        return states;
    }
}
