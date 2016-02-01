package com.atompacman.lereza.core.generator;

import com.atompacman.lereza.core.generator.advisor.Council;
import com.atompacman.lereza.core.generator.advisor.PhraseLayerCouncil;
import com.atompacman.lereza.core.generator.advisor.PieceLayerCouncil;
import com.atompacman.lereza.core.generator.advisor.ProgressionLayerCouncil;
import com.atompacman.lereza.core.generator.advisor.SectionLayerCouncil;

public enum GenerationLayer {

    PIECE       (PieceLayerCouncil      .class),
    SECTION     (SectionLayerCouncil    .class),
    PROGRESSION (ProgressionLayerCouncil.class),
    PHRASE      (PhraseLayerCouncil     .class);
    
    
    //======================================= FIELDS =============================================\\

    private final Class<? extends Council<?>> assocCouncilClazz;
            
            
            
    //======================================= METHODS ============================================\\

    //--------------------------------- PRIVATE CONSTRUCTORS -------------------------------------\\

    private GenerationLayer(Class<? extends Council<?>> assocCouncilClazz) {
        this.assocCouncilClazz = assocCouncilClazz;
    }


    //--------------------------------------- GETTERS --------------------------------------------\\

    public Council<?> createAssociatedCouncil() throws InstantiationException, 
                                                       IllegalAccessException {
        return assocCouncilClazz.newInstance();
    }
}
