package com.atompacman.lereza.core.analysis.filter;

import java.lang.annotation.Annotation;

import com.atompacman.lereza.core.analysis.structure.MusicalStructure;

public abstract class PerStructureGenericFilter<A extends Annotation> 
    extends PerStructureFilter<A, MusicalStructure> {

}