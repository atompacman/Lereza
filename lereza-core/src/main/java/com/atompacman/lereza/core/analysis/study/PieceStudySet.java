package com.atompacman.lereza.core.analysis.study;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashMap;
import java.util.Map;

import com.atompacman.lereza.core.analysis.structure.MusicalStructure;

public final class PieceStudySet {

    //
    //  ~  FIELDS  ~  //
    //
    
    private final Map<Class<? extends MusicalStructure>, StructureTypeStudySet<?>> studies;
    
    
    //
    //  ~  INIT  ~  //
    //
    
    public PieceStudySet() {
        this.studies = new HashMap<>();
    }
    
    
    //
    //  ~  ADD  ~  //
    //
    
    @SuppressWarnings("unchecked")
    <M extends MusicalStructure> void add(M structure, Study study) {
        Class<M> clazz = (Class<M>) structure.getClass();
        StructureTypeStudySet<M> typeSet = (StructureTypeStudySet<M>) studies.get(clazz);
        if (typeSet == null) {
            typeSet = new StructureTypeStudySet<M>();
            studies.put(clazz, typeSet);
        }
        typeSet.add(structure, study);
    }
    
    
    //
    //  ~  GETTERS  ~  //
    //
    
    @SuppressWarnings("unchecked")
    public <M extends MusicalStructure> StructureTypeStudySet<M> getTypeStudySet(Class<M> type) {
        StructureTypeStudySet<M> typeSet = (StructureTypeStudySet<M>) studies.get(type);
        checkArgument(typeSet != null);
        return typeSet;
    }
    
    @SuppressWarnings("unchecked")
    public <M extends MusicalStructure> StructureStudySet getStudySet(M structure) {
        Class<M> clazz = (Class<M>) structure.getClass();
        return getTypeStudySet(clazz).getStudySet(structure);
    }
    
    
    //
    //  ~  STATE  ~  //
    //
    
    public boolean contains(Class<? extends MusicalStructure> type) {
        return studies.containsKey(type);
    }
    
    public <M extends MusicalStructure> boolean contains(M structure) {
        @SuppressWarnings("unchecked")
        Class<M> clazz = (Class<M>) structure.getClass();
        return contains(clazz) && getTypeStudySet(clazz).contains(structure);
    }
}
