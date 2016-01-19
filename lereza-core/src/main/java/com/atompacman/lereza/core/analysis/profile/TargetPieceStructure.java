package com.atompacman.lereza.core.analysis.profile;

import com.atompacman.lereza.core.analysis.profile.PieceStructuralScope.PartSubstructure;
import com.atompacman.lereza.core.analysis.profile.PieceStructuralScope.PartGrouping;

public class TargetPieceStructure {

    //====================================== CONSTANTS ===========================================\\

    public static final TargetPieceStructure WHOLE_PIECE = new TargetPieceStructure(
            new PieceStructuralScope(PartSubstructure.PART,  PartGrouping.ALL), 0, 0);
    
    
    
    //======================================= FIELDS =============================================\\

    private final PieceStructuralScope scope;
    private final int                  partGroupIdx;
    private final int                  substructureIdx;



    //======================================= METHODS ============================================\\

    //------------------------------------- CONSTRUCTORS -----------------------------------------\\

    public TargetPieceStructure(PieceStructuralScope scope, int partGroupIdx, int substructureIdx) {
        this.scope           = scope;
        this.partGroupIdx    = partGroupIdx;
        this.substructureIdx = substructureIdx;
    }
    
    
    //--------------------------------------- GETTERS --------------------------------------------\\

    public PieceStructuralScope getStructuralScope() {
        return scope;
    }

    public int getPartGroupIndex() {
        return partGroupIdx;
    }

    public int getSubstructureIndex() {
        return substructureIdx;
    }


    //--------------------------------------- EQUALS ---------------------------------------------\\

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + partGroupIdx;
        result = prime * result + ((scope == null) ? 0 : scope.hashCode());
        result = prime * result + substructureIdx;
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TargetPieceStructure other = (TargetPieceStructure) obj;
        if (partGroupIdx != other.partGroupIdx)
            return false;
        if (scope == null) {
            if (other.scope != null)
                return false;
        } else if (!scope.equals(other.scope))
            return false;
        if (substructureIdx != other.substructureIdx)
            return false;
        return true;
    }


    //------------------------------------ SERIALIZATION -----------------------------------------\\

    public String toString() {
        return new StringBuilder().append(scope).append(" (").append(partGroupIdx)
                .append(',').append(substructureIdx).append(")").toString();
    }
}
