package com.atompacman.lereza.core.analysis.profile;

import com.atompacman.lereza.core.piece.Bar;
import com.atompacman.lereza.core.piece.MultiBar;
import com.atompacman.lereza.core.piece.MultiNoteStack;
import com.atompacman.lereza.core.piece.MultiPart;
import com.atompacman.lereza.core.piece.Note;
import com.atompacman.lereza.core.piece.NoteStack;
import com.atompacman.lereza.core.piece.Part;
import com.atompacman.lereza.core.piece.PieceComponent;
import com.google.common.collect.ImmutableMap;

public class PieceStructuralScope {

    //====================================== CONSTANTS ===========================================\\

    private static final ImmutableMap<Class<? extends PieceComponent>, PieceStructuralScope> 
        PIECE_COMPONENTS_STRUCTUAL_SCOPES = ImmutableMap.<Class<? extends PieceComponent>, 
                                                          PieceStructuralScope          > builder().
            put(Note          .class, of(PartSubstructure.NOTE,       PartGrouping.SINGLE)).
            put(NoteStack     .class, of(PartSubstructure.NOTE_STACK, PartGrouping.SINGLE)).
            put(Bar           .class, of(PartSubstructure.BAR,        PartGrouping.SINGLE)).
            put(Part          .class, of(PartSubstructure.PART,       PartGrouping.SINGLE)).

            put(MultiNoteStack.class, of(PartSubstructure.NOTE_STACK, PartGrouping.ALL   )).
            put(MultiBar      .class, of(PartSubstructure.BAR,        PartGrouping.ALL   )).
            put(MultiPart     .class, of(PartSubstructure.PART,       PartGrouping.ALL   )).build();
    
    
    
    //===================================== INNER TYPES ==========================================\\

    public enum PartGrouping { SINGLE, /*PART_GROUP,*/ ALL }

    public enum PartSubstructure { PART, //SECTION, SUB_SECTION, 
        /*SUB_SUB_SECTION, PHRASE,*/ BAR, /*MOTIF,*/ NOTE_STACK, NOTE }



    //======================================= FIELDS =============================================\\

    private final PartSubstructure substructure;
    private final PartGrouping     grouping;



    //======================================= METHODS ============================================\\

    //-------------------------------- STATIC INITIALIIZATION ------------------------------------\\

    public static PieceStructuralScope of(PartSubstructure substructure, PartGrouping grouping) {
        return new PieceStructuralScope(substructure, grouping);
    }
    
    public static PieceStructuralScope of(Class<? extends PieceComponent> componentClass) {
        PieceStructuralScope scope = PIECE_COMPONENTS_STRUCTUAL_SCOPES.get(componentClass);
        if (scope == null) {
            throw new IllegalArgumentException("Component class \"" + componentClass.getSimpleName() 
                                             + "\" piece structual scope is undefined");
        }
        return scope;
    }
    
    
    //------------------------------------- CONSTRUCTORS -----------------------------------------\\

    public PieceStructuralScope(PartSubstructure substructure, PartGrouping grouping) {
        this.substructure = substructure;
        this.grouping     = grouping;
    }

    
    //--------------------------------------- GETTERS --------------------------------------------\\

    public PartSubstructure partSubstructure() {
        return substructure;
    }

    public PartGrouping partGrouping() {
        return grouping;
    }


    //--------------------------------------- EQUALS ---------------------------------------------\\

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((substructure == null) ? 0 : substructure.hashCode());
        result = prime * result + ((grouping == null) ? 0 : grouping.hashCode());
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
        if (substructure != other.substructure)
            return false;
        if (grouping != other.grouping)
            return false;
        return true;
    }


    //-------------------------------------- TO STRING -------------------------------------------\\

    public String toString() {
        return new StringBuilder().append('[').append(substructure.name())
                .append('|').append(grouping.name()).append(']').toString();
    }
}
