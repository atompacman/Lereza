package com.atompacman.lereza.core.piece2;

public enum ComplexityHierarchyRank {

    POLYPHONIC ("Graph"),
    HOMOPHONIC ("AcyclicGraph"),
    MONOPHONIC ("LinkedList");
    
    
    //
    //  ~  FIELDS  ~  //
    //

    private final String alternativeName;
    
    
    //
    //  ~  INIT  ~  //
    //

    private ComplexityHierarchyRank(String alternativeName) {
        this.alternativeName = alternativeName;
    }
    
    
    //
    //  ~  SERIALIZATION ~  //
    //

    public String getAlternativeName() {
        return alternativeName;
    }
}
