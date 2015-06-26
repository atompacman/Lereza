package com.atompacman.lereza.core.piece;

import com.atompacman.lereza.core.solfege.Pitch;
import com.atompacman.lereza.core.solfege.Value;

public class Note implements PieceComponent {

    //======================================= FIELDS =============================================\\

    private final Pitch   pitch;
    private final Value   value;
    private final boolean isTied;



    //======================================= METHODS ============================================\\

    //------------------------------ PUBLIC STATIC CONSTRUCTOR -----------------------------------\\

    public static Note valueOf(Pitch pitch, Value value) {
        return new Note(pitch, value, false);
    }

    public static Note valueOf(byte hexNote, Value value, boolean isTied) {
        Pitch pitch = Pitch.thatIsMoreCommonForHexValue(hexNote);
        return new Note(pitch, value, isTied);
    }

    public static Note valueOf(Pitch pitch, Value value, boolean isTied) {
        return new Note(pitch, value, isTied);
    }


    //--------------------------------- POTECTED CONSTRUCTOR ------------------------------------\\

    protected Note(Pitch pitch, Value value, boolean isTied) {
        this.pitch = pitch;
        this.value = value;
        this.isTied = isTied;
    }


    //--------------------------------------- GETTERS --------------------------------------------\\

    public Pitch getPitch() {
        return pitch;
    }

    public Value getValue() {
        return value;
    }


    //---------------------------------------- STATE ---------------------------------------------\\

    public boolean isTied() {
        return isTied;
    }


    //-------------------------------------- TO STRING -------------------------------------------\\

    public String toCompleteString() {
        StringBuilder sb = new StringBuilder();
        sb.append(isTied ? '(' : '[').append(pitch.toString());
        sb.append('|').append(value.toString());
        sb.append(isTied ? ')' : ']');
        return sb.toString();
    }

    public String toString() {
        return pitch.toString();
    }


    //--------------------------------------- EQUALS ---------------------------------------------\\

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (isTied ? 1231 : 1237);
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
        if (isTied != other.isTied)
            return false;
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
