package com.atompacman.lereza.core.piece;

import org.junit.Before;

public class TestBar {

    //======================================= FIELDS =============================================\\

    private BarBuilder builder;



    //======================================= METHODS ============================================\\

    //===================================== BEFORE TESTS =========================================\\

    @Before
    public void beforeTest() {
        builder = BarBuilder.create();
    }


}
