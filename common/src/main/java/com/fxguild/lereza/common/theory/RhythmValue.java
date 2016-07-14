package com.fxguild.lereza.common.theory;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;

public enum RhythmValue {

    SIXTYFORTH, 
    THIRTYSECONTH, 
    SIXTEENTH, 
    EIGHTH, 
    QUARTER, 
    HALF, 
    WHOLE;


    //
    //  ~  INIT  ~  //
    //

    public static RhythmValue fromTimeunit(int length) {
        double exponent = Math.log10(length) / Math.log10(2);
        Preconditions.checkArgument((exponent - (int) exponent) == 0, 
                "Length \"%d\" cannot be converted to a simple value", length);
        return values()[(int)exponent];
    }


    //
    //  ~  TO TIMEUNIT  ~  //
    //

    public int toTimeunit() {
        return (int) Math.pow(2, this.ordinal());
    }


    //
    //  ~  SPLIT INTO VALUES  ~  //
    //

    public static List<RhythmValue> splitIntoValues(int length) {
        List<RhythmValue> values = new ArrayList<>();

        while (length > WHOLE.toTimeunit()) {
            values.add(WHOLE);
            length -= WHOLE.toTimeunit();
        }
        double exponent = Math.log10(length) / Math.log10(2);

        while ((exponent - (int) exponent) != 0) {
            int biggestLength = (int) Math.pow(2, (int) exponent);
            values.add(fromTimeunit(biggestLength));
            length -= biggestLength;
            exponent = Math.log10(length) / Math.log10(2);
        }
        values.add(values()[(int)exponent]);

        return values;
    }


    //
    //  ~  SERIALIZATION  ~  //
    //

    @Override
    public String toString() {
        return "1/" + Integer.toString((int) Math.pow(2, values().length - ordinal() - 1));
    }
    
    public String toStaccato() {
        switch (this) {
        case WHOLE:         return "w";
        case HALF:          return "h";
        case QUARTER:       return "q";
        case EIGHTH:        return "i";
        case SIXTEENTH:     return "s";
        case THIRTYSECONTH: return "t";
        case SIXTYFORTH:    return "x";
        default:            return "?";
        }
    }
}