package com.atompacman.lereza.core.theory;

import java.util.ArrayList;
import java.util.List;

public enum RythmnValue {

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

    public static RythmnValue fromTimeunit(int length) {
        double exponent = Math.log10(length) / Math.log10(2);
        if ((exponent - (int) exponent) != 0) {
            throw new IllegalArgumentException("Length \"" + length + 
                    "\" cannot be converted to a simple value.");
        }
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

    public static List<RythmnValue> splitIntoValues(int length) {
        List<RythmnValue> values = new ArrayList<>();

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