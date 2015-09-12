package com.atompacman.lereza.generator;

import java.util.Map;

import com.atompacman.lereza.core.piece.Note;
import com.atompacman.lereza.core.piece.Piece;
import com.atompacman.lereza.core.piece.Stack;
import com.atompacman.lereza.generator.advisor.Council;

public class Generator {

    //======================================= FIELDS =============================================\\

    public Map<GenerationLayer, Council<?>> councils;

    
    
    //======================================= METHODS ============================================\\

    //--------------------------------- PUBLIC CONSTRUCTORS --------------------------------------\\

    public Generator() {
        try {
            for (GenerationLayer layer : GenerationLayer.values()) {
                councils.put(layer, layer.createAssociatedCouncil());
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    
    
    //-------------------------------------- GENERATE --------------------------------------------\\
    
    public Piece<Stack<Note>> generate() {
        
    }
}   
