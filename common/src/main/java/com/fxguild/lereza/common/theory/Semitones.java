package com.fxguild.lereza.common.theory;

import static com.google.common.base.Preconditions.checkArgument;

public final class Semitones {

    //
    //  ~  CONSTANTS  ~  //
    //

    public static final int IN_OCTAVE = 12;


    //
    //  ~  INIT  ~  //
    //

    private Semitones() {
        
    }


    //
    //  ~  BETWEEN  ~  //
    //

    public static int between(Tone a, Direction direction, Tone b) {
        int semitonesA = a.semitoneValue();
        int semitonesB = b.semitoneValue();
        
        checkArgument(direction != Direction.STRAIGHT || semitonesA == semitonesB,
                "The STRAIGHT direction cannot be used between %s and %s.", a, b);
        
        if (direction == Direction.DESCENDING) {
            int temp = semitonesA;
            semitonesA = semitonesB;
            semitonesB = temp;
        }

        return normalize(semitonesB - semitonesA);
    }

    public static int between(Pitch a, Pitch b) {
        return b.semitoneValue() - a.semitoneValue();
    }


    //
    //  ~  NORMALIZE  ~  //
    //

    public static int normalize(int semitoneValue) {
        if (semitoneValue >= 0) {
            return semitoneValue % Semitones.IN_OCTAVE;
        } else {
            return Semitones.IN_OCTAVE - (-semitoneValue % Semitones.IN_OCTAVE);
        }
    }
}
