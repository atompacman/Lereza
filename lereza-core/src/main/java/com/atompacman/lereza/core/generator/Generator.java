package com.atompacman.lereza.core.generator;

import java.util.Map;

import com.atompacman.lereza.core.generator.advisor.Council;
import com.atompacman.lereza.core.piece.AbstractPiece;

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
    
    public AbstractPiece generate() {
        return null;
    }
}   
