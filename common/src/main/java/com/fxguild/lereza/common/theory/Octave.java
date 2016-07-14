package com.fxguild.lereza.common.theory;

import static com.google.common.base.Preconditions.*;

public enum Octave {

    ZERO, 
    ONE, 
    TWO, 
    THREE, 
    FOUR, 
    FIVE, 
    SIX, 
    SEVEN, 
    EIGHT, 
    NINE;


    //
    //  ~  INIT  ~  //
    //

    public static Octave fromInt(int octaveNumber) {
        checkArgument(octaveNumber >= 0 && octaveNumber < values().length, "No octave number "
                + "below 0 and above %d (got \"%d\").", values().length, octaveNumber);
        
        return values()[octaveNumber];
    }

    public static Octave fromHex(int hexValue) {
        int octaveNumber = (int) (hexValue / Semitones.IN_OCTAVE) - 1;
        
        checkArgument(octaveNumber >= 0, "Pitch hex value \"%d\" is too low to "
                + "generate an octave number greater or equal to 0.", hexValue);
        
        checkArgument(octaveNumber < values().length, "Pitch hex value \"%d\" is too high "
                + "to generate an octave number below %d.", hexValue, values().length);
        
        return values()[octaveNumber];
    }


    //
    //  ~  VALUE  ~  //
    //

    public int semitoneValue() {
        return ordinal() * Semitones.IN_OCTAVE;
    }

    public int diatonicToneValue() {
        return ordinal() * DiatonicTones.IN_OCTAVE;
    }


    //
    //  ~  SERIALIZATION  ~  //
    //

    @Override
    public String toString() {
        return Integer.toString(ordinal());
    }
}
