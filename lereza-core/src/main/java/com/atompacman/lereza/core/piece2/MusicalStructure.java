package com.atompacman.lereza.core.piece2;

import com.atompacman.toolkat.annotations.Implement;
import com.google.common.base.CaseFormat;

abstract interface MusicalStructure {

    //
    //  ~  STATUS  ~  //
    //
    
    @Implement
    default ComplexityHierarchyRank getComplexityHierarchyRank() {
        String name = getClass().getName();
        for (ComplexityHierarchyRank rank : ComplexityHierarchyRank.values()) {
            String subStr = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, rank.toString());
            String altStr = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, 
                            rank.getAlternativeName());
            if (name.startsWith(subStr) || name.startsWith(altStr)) {
                return rank;
            }
        }
        throw new RuntimeException("Musical structure \"" + name + "\" has a class "
                + "names that doesn't start with a complexity hierarchy rank name");
    }

    @Implement
    default StructuralHierarchyRank getStructuralHierarchyRank() {
        String name = getClass().getName();
        for (StructuralHierarchyRank rank : StructuralHierarchyRank.values()) {
            String subStr = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, rank.toString());
            if (name.endsWith(subStr)) {
                return rank;
            }
        }
        throw new RuntimeException("Musical structure \"" + name + "\" has a class "
                + "names that doesn't end with a structural hierarchy rank name");
    }
}
