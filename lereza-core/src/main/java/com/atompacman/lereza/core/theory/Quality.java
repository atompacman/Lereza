package com.atompacman.lereza.core.theory;

public enum Quality implements IntervalQuality {

    MINOR ("m", -0.5), 
    MAJOR ("",   0.5);

    
    //
    //  ~  FIELDS  ~  //
    //

    private final double semitoneModifier;
    private final String representation;


    //
    //  ~  INIT  ~  //
    //

    private Quality(String representation, double semiToneModifier) {
        this.semitoneModifier = semiToneModifier;
        this.representation   = representation;
    }
    
    public static Quality parseQuality(String str) {
        for (Quality quality : values()) {
            if (str.equalsIgnoreCase(quality.representation)) {
                return quality;
            }
        }
        return valueOf(str);
    }


    //
    //  ~  GETTERS  ~  //
    //

    @Override
    public double semitoneModifier() {
        return semitoneModifier;
    }


    //
    //  ~  SERIALIZATION  ~  //
    //

    @Override
    public String fullName() {
        return name().toLowerCase();
    }

    @Override
    public String toString() {
        return representation;
    }
}