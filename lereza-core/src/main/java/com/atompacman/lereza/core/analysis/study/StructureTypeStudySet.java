package com.atompacman.lereza.core.analysis.study;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashMap;
import java.util.Map;

import com.atompacman.lereza.core.analysis.MusicalStructure;

import autovalue.shaded.com.google.common.common.collect.ImmutableSet;

public final class StructureTypeStudySet<M extends MusicalStructure> {

    //
    //  ~  FIELDS  ~  //
    //
    
    private final Map<M, StructureStudySet> studies;
    
    
    //
    //  ~  INIT  ~  //
    //
    
    StructureTypeStudySet() {
        this.studies = new HashMap<>();
    }
    
    
    //
    //  ~  ADD  ~  //
    //
    
    void add(M structure, Study study) {
        StructureStudySet set = studies.get(structure);
        if (set == null) {
            set = new StructureStudySet();
            studies.put(structure, set);
        }
        set.add(study);
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //
    
    public StructureStudySet getStudySet(M structure) {
        StructureStudySet set = studies.get(structure);
        checkArgument(set != null);
        return set;
    }
    
    public ImmutableSet<M> getStructures() {
        return ImmutableSet.copyOf(studies.keySet());
    }
    
    
    //
    //  ~  STATE  ~  //
    //
    
    public boolean contains(M structure) {
        return studies.containsKey(structure);
    }
}
