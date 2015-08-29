package com.atompacman.lereza.kpf.key;

public class KeyChange {

    //======================================= FIELDS =============================================\\

    private int timeunit;
    private int hexKey;



    //======================================= METHODS ============================================\\

    //--------------------------------- PACKAGE CONSTRUCTORS -------------------------------------\\

    public KeyChange(int timeunit, int hexKey) {
        this.timeunit = timeunit;
        this.hexKey = hexKey;
    }

    
    //--------------------------------------- GETTERS --------------------------------------------\\

    public int getTimeunit() {
        return timeunit;
    }
    
    public int getHexKey() {
        return hexKey;
    }
}