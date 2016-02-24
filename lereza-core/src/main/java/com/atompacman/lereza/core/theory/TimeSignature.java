package com.atompacman.lereza.core.theory;

import static com.google.common.base.Preconditions.checkState;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class TimeSignature {

    //
    //  ~  CONSTANTS  ~  //
    //

    public static final TimeSignature STANDARD_4_4 = of(4, 4, Grouping.DUPLETS);

    
    //
    //  ~  FIELDS  ~  //
    //

    public abstract int      getMeterNumerator();
    public abstract int      getMeterDenominator();
    public abstract Grouping getNoteGrouping();


    //
    //  ~  INIT  ~  //
    //

    public static TimeSignature of(int meterNum, int meterDen) {
        return new AutoValue_TimeSignature(meterNum, meterDen, Grouping.DUPLETS);
    }

    public static TimeSignature of(int meterNum, int meterDen, Grouping noteGrouping) {
        return new AutoValue_TimeSignature(meterNum, meterDen, noteGrouping);
    }


    //
    //  ~  TIMEUNITS IN A BAR  ~  //
    //

    public int timeunitsInABar() {
        double tu = ((double)getMeterNumerator() / (double)getMeterDenominator()) 
                * RythmnValue.WHOLE.toTimeunit();
        checkState(Double.compare(tu, Math.floor(tu)) == 0, "Invalid time signature");
        return (int)Math.floor(tu);
    }


    //
    //  ~  SERIALIZATION  ~  //
    //

    @Override
    public String toString() {
        return "[" + getMeterNumerator() + "|" + getMeterDenominator() + "]";
    }
}
