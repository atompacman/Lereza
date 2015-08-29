package com.atompacman.lereza.core.solfege;

public enum Accidental {

    NONE  ("",   0), SHARP ("#",  1), FLAT  ("b", -1);


    //======================================= FIELDS =============================================\\

    private String representation;
    private int semitoneAlteration;



    //======================================= METHODS ============================================\\

    //--------------------------------- PRIVATE CONSTRUCTORS -------------------------------------\\

    private Accidental(String representation, int semitoneAlteration) {
        this.representation = representation;
        this.semitoneAlteration = semitoneAlteration;
    }


    //------------------------------ PUBLIC STATIC CONSTRUCTORS ----------------------------------\\

    public static Accidental parseAccidental(String string) {
        for (Accidental accidental : Accidental.values()) {
            if (string.equalsIgnoreCase(accidental.toString())) {
                return accidental;
            }
        }
        throw new IllegalArgumentException("\"" + string + "\" is not a valid string "
                + "representation of an accidental.");
    }

    public static Accidental fromSemitoneAlteration(int semitoneAlteration) {
        for (Accidental accidental : values()) {
            if (accidental.semitoneAlteration == semitoneAlteration) {
                return accidental;
            }
        }
        throw new IllegalArgumentException("No accidental has a semitone alteration of " 
                + semitoneAlteration + ".");
    }


    //--------------------------------------- GETTERS --------------------------------------------\\

    public int semitoneAlteration() {
        return semitoneAlteration;
    }


    //-------------------------------------- TO STRING -------------------------------------------\\

    public String toString() {
        return representation;
    }
}
