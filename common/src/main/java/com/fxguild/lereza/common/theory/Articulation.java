package com.fxguild.lereza.common.theory;

public enum Articulation {

    NORMAL   (""), 
    STACCATO ("·");


    //
    //  ~  FIELDS  ~  //
    //

    private final String representation;

    
    //
    //  ~  INIT  ~  //
    //

    public static Articulation of(String str) {
        for (Articulation articulation : values()) {
            if (str.equals(articulation.representation)) {
                return articulation;
            }
        }
        return valueOf(str);
    }

    private Articulation(String representation) {
        this.representation = representation;
    }


    //
    //  ~  SERIALIZATION  ~  //
    //

    @Override
    public String toString() {
        return representation;
    }
}
