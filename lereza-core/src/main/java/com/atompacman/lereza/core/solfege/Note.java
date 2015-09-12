package com.atompacman.lereza.core.solfege;

public class Note {

    //======================================= FIELDS =============================================\\

    protected final Pitch pitch;
    protected final Value value;



    //======================================= METHODS ============================================\\

    //------------------------------ PUBLIC STATIC CONSTRUCTOR -----------------------------------\\

    public static Note valueOf(Pitch pitch, Value value) {
        return new Note(pitch, value);
    }

    public static Note valueOf(byte hexNote, Value value) {
        return new Note(Pitch.thatIsMoreCommonForHexValue(hexNote), value);
    }

    public static Note valueOf(String pitch, Value value) {
        return new Note(Pitch.valueOf(pitch), value);
    }


    //--------------------------------- PROTECTED CONSTRUCTOR ------------------------------------\\

    protected Note(Pitch pitch, Value value) {
        this.pitch = pitch;
        this.value = value;
    }


    //--------------------------------------- GETTERS --------------------------------------------\\

    public Pitch getPitch() {
        return pitch;
    }

    public Value getValue() {
        return value;
    }


    //------------------------------------ SERIALIZATION -----------------------------------------\\

    public String toString() {
        return pitch.toString();
    }

    public String toStaccato() {
        return toStaccato(null);
    }

    public String toStaccato(Byte velocity) {
        StringBuilder sb = new StringBuilder();
        sb.append(pitch.toStaccato());
        sb.append(value.toStaccato());
        if (velocity != null) {
            sb.append('a').append(velocity);
        }
        return sb.toString();
    }


    //--------------------------------------- EQUALS ---------------------------------------------\\

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pitch == null) ? 0 : pitch.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Note other = (Note) obj;
        if (pitch == null) {
            if (other.pitch != null)
                return false;
        } else if (!pitch.equals(other.pitch))
            return false;
        if (value != other.value)
            return false;
        return true;
    }
}
