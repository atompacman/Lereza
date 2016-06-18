package com.atompacman.lereza.core.piece;

import org.apache.logging.log4j.Level;

import com.atompacman.toolkat.module.BaseModule;

abstract class PieceComponentBuilder<T extends PieceComponent> extends BaseModule {

    //=================================== ABSTRACT METHODS =======================================\\

    protected abstract T buildComponent();

    protected abstract void reset();



    //======================================= METHODS ============================================\\

    //------------------------------------- CONSTRUCTORS -----------------------------------------\\

    protected PieceComponentBuilder() {
        super(Level.TRACE);
    }
    
    protected PieceComponentBuilder(BaseModule parentModule) {
        super(Level.TRACE, parentModule);
    }
    
    
    //---------------------------------------- BUILD ---------------------------------------------\\

    final T build() {
        T component = buildComponent();
        reset();
        super.reset();
        return component;
    }
}
