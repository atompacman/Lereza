package com.atompacman.lereza.analysis.profile.target;

import com.atompacman.lereza.analysis.profile.target.PieceStructuralScope.HorizontalScope;
import com.atompacman.lereza.analysis.profile.target.PieceStructuralScope.VerticalScope;

public class TargetPieceStructure {

    //====================================== CONSTANTS ===========================================\\

    public static final TargetPieceStructure WHOLE_PIECE = new TargetPieceStructure(
            new PieceStructuralScope(HorizontalScope.PIECE,  VerticalScope.PIECE), 0, 0);
    
    
    
    //======================================= FIELDS =============================================\\

    private final PieceStructuralScope scope;
    private final int                  horiIdx;
    private final int                  vertIdx;



    //======================================= METHODS ============================================\\

    //--------------------------------- PUBLIC CONSTRUCTORS --------------------------------------\\

    public TargetPieceStructure(PieceStructuralScope scope, int horiIdx, int vertIdx) {
        this.scope   = scope;
        this.horiIdx = horiIdx;
        this.vertIdx = vertIdx;
    }
    
    
    //--------------------------------------- GETTERS --------------------------------------------\\

    public PieceStructuralScope getStructuralScope() {
        return scope;
    }

    public int getHorizontalIndex() {
        return horiIdx;
    }

    public int getVerticalIndex() {
        return vertIdx;
    }


    //--------------------------------------- EQUALS ---------------------------------------------\\

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + horiIdx;
        result = prime * result + ((scope == null) ? 0 : scope.hashCode());
        result = prime * result + vertIdx;
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
        if (horiIdx != other.horiIdx)
            return false;
        if (scope == null) {
            if (other.scope != null)
                return false;
        } else if (!scope.equals(other.scope))
            return false;
        if (vertIdx != other.vertIdx)
            return false;
        return true;
    }


    //-------------------------------------- TO STRING -------------------------------------------\\

    public String toString() {
        return new StringBuilder().append(scope).append(" (").append(horiIdx)
                .append(',').append(vertIdx).append(")").toString();
    }
}
