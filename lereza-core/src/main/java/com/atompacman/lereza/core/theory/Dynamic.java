package com.atompacman.lereza.core.theory;

import static com.google.common.base.Preconditions.*;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Dynamic {
  
    //
    //  ~  INNER TYPES  ~  //
    //

    public enum Marker {

        PIANISSISSIMO   ("ppp", 0),
        PIANISSIMO      ("pp",  10),
        PIANO           ("p",   30),
        MEZZO_PIANO     ("mp",  50),
        MEZZO_FORTE     ("mf",  64),
        FORTE           ("f",   78),
        FORTISSIMO      ("ff",  98),
        FORTISSISSIMO   ("fff", 118);

        
        //
        //  ~  FIELDS  ~  //
        //

        private final String  repres;
        private final byte    minVelocity;


        //
        //  ~  INIT  ~  //
        //

        public static Marker of(int hexVelocity) {
            checkArgument(hexVelocity>=0 && hexVelocity<128, "Velocity must be between 0 and 128.");
            
            for (int i = 1; i < values().length; ++i) {
                if (values()[i].minVelocity >= hexVelocity) {
                    return values()[i-1];
                }
            }
            return FORTISSISSIMO;
        }

        private Marker(String repres, int minVelocity) {
            this.repres      = repres;
            this.minVelocity = (byte) minVelocity;
        }

        
        //
        //  ~  GETTERS  ~  //
        //

        public byte getMinimumVelocity() {
            return minVelocity;
        }
        
        
        //
        //  ~  SERIALIZATION  ~  //
        //
        
        @Override
        public String toString() {
            return repres;
        }
    }


    //
    //  ~  FIELDS  ~  //
    //

    public abstract byte   getVelocity();
    public abstract Marker getMarker();


    //
    //  ~  INIT  ~  //
    //

    public static Dynamic of(byte velocity) {
        return new AutoValue_Dynamic(velocity, Marker.of(velocity));
    }
}
