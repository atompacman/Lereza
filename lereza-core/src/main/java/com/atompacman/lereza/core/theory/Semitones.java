package com.atompacman.lereza.core.theory;

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
        int semitoneValueA = a.semitoneValue();
        int semitoneValueB = b.semitoneValue();

        switch (direction) {
        case ASCENDING:
            if (semitoneValueB < semitoneValueA) {
                semitoneValueB += IN_OCTAVE;
            }
            return semitoneValueB - semitoneValueA;
        case DESCENDING:
            if (semitoneValueB > semitoneValueA) {
                semitoneValueA += IN_OCTAVE;
            }
            return semitoneValueB - semitoneValueA;
        default:
            if (semitoneValueA != semitoneValueB) {
                throw new IllegalArgumentException("The STRAIGHT direction "
                        + "cannot be used between " + a + " and " + b + ".");
            }
            return 0;
        }
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
