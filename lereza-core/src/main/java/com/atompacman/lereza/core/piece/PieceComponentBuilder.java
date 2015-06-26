package com.atompacman.lereza.core.piece;

import com.atompacman.toolkat.module.Module;

abstract class PieceComponentBuilder<T extends PieceComponent> extends Module {

    //=================================== ABSTRACT METHODS =======================================\\

    protected abstract T buildComponent();

    protected abstract void reset();



    //======================================= METHODS ============================================\\

    //---------------------------------------- BUILD ---------------------------------------------\\

    final T build() {
        T component = buildComponent();
        reset();
        super.reset();
        return component;
    }
}
