package com.atompacman.lereza.core.theory;

public enum Accidental {

    NONE  ("",   0), 
    SHARP ("#",  1), 
    FLAT  ("b", -1);


    //
    //  ~  FIELDS  ~  //
    //

    private final String representation;
    private final int    semitoneAlteration;


    //
    //  ~  INIT  ~  //
    //

    public static Accidental of(String str) {
        for (Accidental accidental : values()) {
            if (str.equals(accidental.representation)) {
                return accidental;
            }
        }
        return valueOf(str);
    }

    public static Accidental of(int semitoneAlteration) {
        for (Accidental accidental : values()) {
            if (accidental.semitoneAlteration == semitoneAlteration) {
                return accidental;
            }
        }
        throw new IllegalArgumentException("No accidental has a semitone "
                + "alteration of " + semitoneAlteration + ".");
    }

    private Accidental(String representation, int semitoneAlteration) {
        this.representation     = representation;
        this.semitoneAlteration = semitoneAlteration;
    }

    
    //
    //  ~  GETTERS  ~  //
    //

    public int semitoneAlteration() {
        return semitoneAlteration;
    }
    

    //
    //  ~  SERIALIZATION  ~  //
    //

    @Override
    public String toString() {
        return representation;
    }
}
