package com.atompacman.lereza.core.piece;

import java.util.Arrays;
import java.util.List;

import com.atompacman.lereza.core.solfege.Pitch;
import com.atompacman.lereza.core.solfege.Value;

public class Note implements PieceComponent {

    //======================================= FIELDS =============================================\\

    private final Pitch       pitch;
    private final List<Value> values;



    //======================================= METHODS ============================================\\

    //------------------------------ PUBLIC STATIC CONSTRUCTOR -----------------------------------\\

    public static Note valueOf(Pitch pitch, Value value) {
        return new Note(pitch, Arrays.asList(value));
    }

    public static Note valueOf(Pitch pitch, List<Value> values) {
        return new Note(pitch, values);
    }

    public static Note valueOf(byte hexNote, Value value) {
        Pitch pitch = Pitch.thatIsMoreCommonForHexValue(hexNote);
        return new Note(pitch, Arrays.asList(value));
    }

    public static Note valueOf(byte hexNote, List<Value> values) {
        Pitch pitch = Pitch.thatIsMoreCommonForHexValue(hexNote);
        return new Note(pitch, values);
    }

    public static Note valueOf(String pitch, Value value) {
        return new Note(Pitch.valueOf(pitch), Arrays.asList(value));
    }

    public static Note valueOf(String pitch, List<Value> values) {
        return new Note(Pitch.valueOf(pitch), values);
    }


    //--------------------------------- POTECTED CONSTRUCTOR ------------------------------------\\

    protected Note(Pitch pitch, List<Value> values) {
        if (values.isEmpty()) {
            throw new IllegalArgumentException("Value list cannot be empty");
        }
        this.pitch  = pitch;
        this.values = values;
    }


    //--------------------------------------- GETTERS --------------------------------------------\\

    public Pitch getPitch() {
        return pitch;
    }

    public List<Value> getValues() {
        return values;
    }

    public Value getMainValue() {
        return values.get(0);
    }

    public int getTotalValue() {
        int total = 0;
        for (Value value : values) {
            total += value.toTimeunit();
        }
        return total;
    }


    //-------------------------------------- TO STRING -------------------------------------------\\

    public String toCompleteString() {
        StringBuilder sb = new StringBuilder();
        sb.append(pitch.toString()).append('(').append(values.get(0).toString());
        for (int i = 1; i < values.size(); ++i) {
            sb.append('+').append(values.get(i).toString());  
        }
        return sb.append(')').toString();
    }

    public String toString() {
        return pitch.toString();
    }

    public String toStaccato() {
        return toStaccato(null);
    }

    public String toStaccato(Byte velocity) {
        StringBuilder sb = new StringBuilder();
        sb.append(pitch.toString());
        for (Value value : values) {
            sb.append(value.toStaccato());
        }
        if (velocity != null) {
            sb.append('A').append(velocity);
        }
        return sb.toString();
    }


    //--------------------------------------- EQUALS ---------------------------------------------\\

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pitch == null) ? 0 : pitch.hashCode());
        result = prime * result + ((values == null) ? 0 : values.hashCode());
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
        if (values == null) {
            if (other.values != null)
                return false;
        } else if (!values.equals(other.values))
            return false;
        return true;
    }
}
