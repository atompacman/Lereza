package com.fxguild.lereza.common.theory;

import com.fxguild.common.annotations.Implement;

public enum AdvancedQuality implements IntervalQuality {

    DIMINISHED ("dim", -1.0), 
    PERFECT    ("",     0.0), 
    AUGMENTED  ("aug",  1.0);


    //
    //  ~  FIELDS  ~  //
    //

    private final double semitoneModifier;
    private final String representation;


    //
    //  ~  INIT  ~  //
    //

    public static AdvancedQuality of(String str) {
        for (AdvancedQuality quality : values()) {
            if (str.equals(quality.representation)) {
                return quality;
            }
        }
        return valueOf(str);
    }
    
    private AdvancedQuality(String representation, double semiToneModifier) {
        this.semitoneModifier = semiToneModifier;
        this.representation   = representation;
    }


    //
    //  ~  GETTERS  ~  //
    //

    @Implement
    public double semitoneModifier() {
        return semitoneModifier;
    }


    //
    //  ~  SERIALIZATION  ~  //
    //
    
    @Implement
    public String fullName() {
        return name().toLowerCase();
    }

    @Override
    public String toString() {
        return representation;
    }
}