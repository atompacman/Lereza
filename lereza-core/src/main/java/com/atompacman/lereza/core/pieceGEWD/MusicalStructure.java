package com.atompacman.lereza.core.pieceGEWD;

import com.atompacman.toolkat.annotations.Implement;
import com.google.common.base.CaseFormat;

abstract interface MusicalStructure {

    //
    //  ~  STATE  ~  //
    //
    
    @Implement
    default ComplexityHierarchyRank getComplexityHierarchyRank() {
        String name = getClass().getName();
        for (ComplexityHierarchyRank rank : ComplexityHierarchyRank.values()) {
            String subStr = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, rank.toString());
            if (name.contains(subStr)) {
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
            if (name.contains(subStr)) {
                return rank;
            }
        }
        throw new RuntimeException("Musical structure \"" + name + "\" has a class "
                + "names that doesn't end with a structural hierarchy rank name");
    }
}
