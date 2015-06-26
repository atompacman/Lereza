package com.atompacman.lereza.core.solfege;

public final class Dynamic {

    //===================================== INNER TYPES ==========================================\\

    public enum Marker {

        PIANISSISSIMO   ("ppp", 0),
        PIANISSIMO      ("pp",  10),
        PIANO           ("p",   30),
        MEZZO_PIANO     ("mp",  50),
        MEZZO_FORTE     ("mf",  64),
        FORTE           ("f",   78),
        FORTISSIMO      ("ff",  98),
        FORTISSISSIMO   ("fff", 118);


        //===================================== FIELDS ===========================================\\

        private String  repres;
        private int     minVelocity;



        //===================================== METHODS ==========================================\\

        //----------------------------- PUBLIC STATIC CONSTRUCTOR --------------------------------\\

        public static Marker valueOf(int hexVelocity) {
            if (hexVelocity < 0 || hexVelocity >= 128) {
                throw new IllegalArgumentException("Velocity must be between 0 and 128.");
            }
            for (int i = 1; i < values().length; ++i) {
                if (values()[i].minVelocity >= hexVelocity) {
                    return values()[i-1];
                }
            }
            return FORTISSISSIMO;
        }

        private Marker(String repres, int minVelocity) {
            this.repres = repres;
            this.minVelocity = minVelocity;
        }


        //------------------------------------ TO STRING -----------------------------------------\\

        public String toString() {
            return repres;
        }
    }



    //======================================= FIELDS =============================================\\

    private final byte   velocity;
    private final Marker marker;



    //======================================= METHODS ============================================\\

    //------------------------------ PUBLIC STATIC CONSTRUCTORS ----------------------------------\\

    public static Dynamic valueOf(byte velocity) {
        return new Dynamic(velocity, Marker.valueOf(velocity));
    }


    //---------------------------------- PRIVATE CONSTRUCTOR -------------------------------------\\

    private Dynamic(byte velocity, Marker marker) {
        this.velocity = velocity;
        this.marker   = marker;
    }


    //--------------------------------------- GETTERS --------------------------------------------\\

    public int getVelocity() {
        return velocity;
    }

    public Marker getMarker() {
        return marker;
    }
}
