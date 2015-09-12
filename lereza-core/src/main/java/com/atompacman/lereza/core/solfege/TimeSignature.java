package com.atompacman.lereza.core.solfege;

import java.nio.ByteBuffer;

public final class TimeSignature {

    //====================================== CONSTANTS ===========================================\\

    public static final TimeSignature STANDARD_4_4 = new TimeSignature(4,4, Grouping.DUPLETS);



    //======================================= FIELDS =============================================\\

    private final int meterNum;
    private final int meterDen;
    private final Grouping noteGrouping;



    //======================================= METHODS ============================================\\

    //------------------------------ PUBLIC STATIC CONSTRUCTORS ----------------------------------\\

    public static TimeSignature valueOf(int meterNum, int meterDen) {
        return new TimeSignature(meterNum, meterDen, Grouping.DUPLETS);
    }

    public static TimeSignature valueOf(int meterNum, int meterDen, Grouping noteGrouping) {
        return new TimeSignature(meterNum, meterDen, noteGrouping);
    }

    //- - - - - - - - - - - - - - - - - - - FROM BINARY - - - - - - - - - - - - - - - - - - - - - \\

    public static TimeSignature valueOf(ByteBuffer buffer) {
        return new TimeSignature(buffer.get(), buffer.get(), Grouping.values()[buffer.get()]);
    }
    
    
    //--------------------------------- PRIVATE CONSTRUCTOR --------------------------------------\\

    private TimeSignature(int meterNum, int meterDen, Grouping noteGrouping) {
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
