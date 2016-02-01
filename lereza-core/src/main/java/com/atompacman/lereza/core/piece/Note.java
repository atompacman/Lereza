package com.atompacman.lereza.core.piece;

import com.atompacman.lereza.core.solfege.BasicNote;
import com.atompacman.lereza.core.solfege.Pitch;
import com.atompacman.lereza.core.solfege.Value;

public class Note extends BasicNote implements PieceComponent {

    //======================================= FIELDS =============================================\\

    private Note prev;
    private Note next;

    

    //======================================= METHODS ============================================\\

    //------------------------------------- CONSTRUCTORS -----------------------------------------\\

    public static Note valueOf(Pitch pitch, Value value) {
        return new Note(pitch, value);
    }

    public static Note valueOf(byte hexNote, Value value) {
        return new Note(Pitch.thatIsMoreCommonForHexValue(hexNote), value);
    }

    public static Note valueOf(String pitch, Value value) {
        return new Note(Pitch.valueOf(pitch), value);
    }

    private Note(Pitch pitch, Value value) {
        super(pitch, value);
    }


    //--------------------------------------- SETTERS --------------------------------------------\\

    public Note tieTo(Note next) {
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

    public Note getNextTiedNote() {
        return next;
    }

    public Note getPrevTiedNote() {
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
