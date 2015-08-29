package com.atompacman.lereza.core.solfege;

public final class RythmicSignature {

    //====================================== CONSTANTS ===========================================\\

    public static final RythmicSignature STANDARD_4_4 = new RythmicSignature(4,4, Grouping.DUPLETS);



    //======================================= FIELDS =============================================\\

    private final int meterNum;
    private final int meterDen;
    private final Grouping noteGrouping;



    //======================================= METHODS ============================================\\

    //------------------------------ PUBLIC STATIC CONSTRUCTORS ----------------------------------\\

    public static RythmicSignature valueOf(int meterNum, int meterDen) {
        return new RythmicSignature(meterNum, meterDen, Grouping.DUPLETS);
    }

    public static RythmicSignature valueOf(int meterNum, int meterDen, Grouping noteGrouping) {
        return new RythmicSignature(meterNum, meterDen, noteGrouping);
    }


    //--------------------------------- PRIVATE CONSTRUCTOR --------------------------------------\\

    private RythmicSignature(int meterNum, int meterDen, Grouping noteGrouping) {
        this.meterNum = meterNum;
        this.meterDen = meterDen;
        this.noteGrouping = noteGrouping;
    }


    //--------------------------------------- GETTERS --------------------------------------------\\

    public int getMeterNumerator() {
        return meterNum;
    }

    public int getMeterDenominator() {
        return meterDen;
    }

    public Grouping getNoteGrouping() {
        return noteGrouping;
    }

    public int timeunitsInABar() {
        return meterNum * Value.QUARTER.toTimeunit();
    }


    //-------------------------------------- TO STRING -------------------------------------------\\

    public String toString() {
        return "[" + meterNum + "|" + meterDen + "]";
    }
}
