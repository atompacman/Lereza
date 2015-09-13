package com.atompacman.lereza.core.analysis.profile;

public class PieceStructuralScope {

    //===================================== INNER TYPES ==========================================\\

    public enum VerticalScope { PIECE, /*PART_GROUP,*/ PART }

    public enum HorizontalScope { PIECE, //SECTION, SUB_SECTION, 
        /*SUB_SUB_SECTION, PHRASE,*/ BAR, /*MOTIF,*/ NOTE_STACK, NOTE }



    //======================================= FIELDS =============================================\\

    private final HorizontalScope horiScope;
    private final VerticalScope   vertScope;



    //======================================= METHODS ============================================\\

    //--------------------------------- PUBLIC CONSTRUCTORS --------------------------------------\\

    public PieceStructuralScope(HorizontalScope horiScope, VerticalScope vertScope) {
        this.horiScope = horiScope;
        this.vertScope = vertScope;
    }


    //--------------------------------------- GETTERS --------------------------------------------\\

    public HorizontalScope getHoriScope() {
        return horiScope;
    }

    public VerticalScope getVertScope() {
        return vertScope;
    }


    //--------------------------------------- EQUALS ---------------------------------------------\\

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((horiScope == null) ? 0 : horiScope.hashCode());
        result = prime * result + ((vertScope == null) ? 0 : vertScope.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PieceStructuralScope other = (PieceStructuralScope) obj;
        if (horiScope != other.horiScope)
            return false;
        if (vertScope != other.vertScope)
            return false;
        return true;
    }


    //-------------------------------------- TO STRING -------------------------------------------\\

    public String toString() {
        return new StringBuilder().append( "H: ").append(horiScope.name())
                .append(" V: ").append(vertScope.name()).toString();
    }
}
