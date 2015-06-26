package com.atompacman.lereza.core.piece;

import java.util.Map;

import com.atompacman.lereza.core.solfege.Articulation;
import com.atompacman.lereza.core.solfege.Dynamic;
import com.atompacman.lereza.core.solfege.Pitch;

public final class ArticulatedStack<T extends Note> extends Stack<T> {

    //======================================= FIELDS =============================================\\

    private final Articulation articulation;



    //======================================= METHODS ============================================\\

    //------------------------------ PACKAGE STATIC CONSTRUCTOR ----------------------------------\\

    static <T extends Note> ArticulatedStack<T> valueOf(Stack<T> stack, Articulation articulation) {
        return new ArticulatedStack<T>(stack.startingNotes, 
                                       stack.startedNotes, 
                                       stack.getDynamic(), 
                                       articulation);
    }


    //---------------------------------- PRIVATE CONSTRUCTOR -------------------------------------\\

    private ArticulatedStack(Map<Pitch, T> startingNotes, 
                             Map<Pitch, T> startedNotes, 
                             Dynamic       dynamic, 
                             Articulation  articulation) {

        super(startingNotes, startedNotes, dynamic);
        this.articulation = articulation;
    }


    //--------------------------------------- GETTERS --------------------------------------------\\

    public Articulation getArticulation() {
        return articulation;
    }
}
