package com.atompacman.lereza.core.piece;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PBT {

    //
    //  ~  FIELDS  ~  //
    //
    
    public abstract int getPart();
    public abstract int getBar();
    public abstract int getTimeunit();


    //
    //  ~  INIT  ~  //
    //

    public static PBT of() {
        return new AutoValue_PBT(0, 0, 0);
    }
    
    public static PBT of(int part) {
        return new AutoValue_PBT(part, 0, 0);
    }
    
    public static PBT of(int part, int bar) {
        return new AutoValue_PBT(part, bar, 0);
    }
    
    public static PBT of(int part, int bar, int timeunit) {
        return new AutoValue_PBT(part, bar, timeunit);
    }
}
