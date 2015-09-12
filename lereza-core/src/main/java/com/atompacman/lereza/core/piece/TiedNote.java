package com.atompacman.lereza.core.piece;

import com.atompacman.lereza.core.solfege.Note;
import com.atompacman.lereza.core.solfege.Pitch;
import com.atompacman.lereza.core.solfege.Value;

public class TiedNote extends Note implements PieceComponent {

    //======================================= FIELDS =============================================\\

    private TiedNote prev;
    private TiedNote next;

    

    //======================================= METHODS ============================================\\

    //------------------------------ PUBLIC STATIC CONSTRUCTOR -----------------------------------\\

    public static TiedNote valueOf(Pitch pitch, Value value) {
        return new TiedNote(pitch, value);
    }

    public static TiedNote valueOf(byte hexNote, Value value) {
        return new TiedNote(Pitch.thatIsMoreCommonForHexValue(hexNote), value);
    }

    public static TiedNote valueOf(String pitch, Value value) {
        return new TiedNote(Pitch.valueOf(pitch), value);
    }


    //--------------------------------- PROTECTED CONSTRUCTOR ------------------------------------\\

    protected TiedNote(Pitch pitch, Value value) {
        super(pitch, value);
    }


    //--------------------------------------- SETTERS --------------------------------------------\\

    TiedNote tieTo(TiedNote next) {
        if (this == next) {
            throw new IllegalArgumentException("Cannot tie a note to itself");
        }
        if (!this.pitch.equals(next.pitch)) {
            throw new IllegalArgumentException("Cannot tie notes of different pitch");
        }
        this.next = next;
        next.prev = this;
        
        return next;
    }
    
    
    //--------------------------------------- GETTERS --------------------------------------------\\

    public TiedNote getNextTiedNote() {
        return next;
    }

    public TiedNote getPrevTiedNote() {
        return prev;
    }


    //---------------------------------------- STATE ---------------------------------------------\\

    public boolean isTied() {
        return prev != null;
    }
    
    
    //------------------------------------ SERIALIZATION -----------------------------------------\\

    public String toStaccato(Byte velocity) {
        StringBuilder sb = new StringBuilder();
        sb.append(pitch.toStaccato());
        if (prev != null) {
            sb.append('-');
        }
        sb.append(value.toStaccato());
        if (next != null) {
            sb.append('-');
        }
        if (velocity != null) {
            sb.append('a').append(velocity);
        }
        return sb.toString();
    }
}
