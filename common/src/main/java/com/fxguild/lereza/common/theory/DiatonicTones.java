package com.fxguild.lereza.common.theory;

import static com.google.common.base.Preconditions.checkArgument;

public class DiatonicTones {

    //
    //  ~  CONSTANTS  ~  //
    //

    public static final int IN_OCTAVE = 7;


    //
    //  ~  INIT  ~  //
    //

    private DiatonicTones() {
        
    }
    
    
    //
    //  ~  BETWEEN  ~  //
    //

    public static int between(Tone a, Direction direction, Tone b) {
        int diatonicA = a.diatonicToneValue();
        int diatonicB = b.diatonicToneValue();
        
        checkArgument(direction != Direction.STRAIGHT || diatonicA == diatonicB,
                "The STRAIGHT direction cannot be used between %s and %s.", a, b);
        
        if (direction == Direction.DESCENDING) {
            int temp = diatonicA;
            diatonicA = diatonicB;
            diatonicB = temp;
        }

        return normalize(diatonicB - diatonicA);
    }

    public static int between(Pitch a, Pitch b) {
        return b.diatonicToneValue() - a.diatonicToneValue(); 
    }


    //
    //  ~  NORMALIZE  ~  //
    //

    public static int normalize(int diatonicToneValue) {
        if (diatonicToneValue >= 0) {
            return diatonicToneValue % DiatonicTones.IN_OCTAVE;
        } else {
            return DiatonicTones.IN_OCTAVE - (-diatonicToneValue % DiatonicTones.IN_OCTAVE);
        }
    }
}
