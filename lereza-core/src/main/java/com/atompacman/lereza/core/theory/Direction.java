package com.atompacman.lereza.core.theory;

public enum Direction {

    ASCENDING  ( 1), 
    STRAIGHT   ( 0), 
    DESCENDING (-1);


    //
    //  ~  FIELDS  ~  //
    //

    private int semitoneMultiplier;


    //
    //  ~  INIT  ~  //
    //

    private Direction(int semitoneMultiplier) {
        this.semitoneMultiplier = semitoneMultiplier;
    }


    //
    //  ~  GETTERS  ~  //
    //

    
    public int semitoneMultiplier() {
        return semitoneMultiplier;
    }


    //
    //  ~  SERIALIZATION  ~  //
    //

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}