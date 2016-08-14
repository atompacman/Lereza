package com.atompacman.lereza.core.midi.in;

public final class MIDINote {

    //
    //  ~  FIELDS  ~  //
    //

    private final byte hexNote;
    private final byte velocity;
    private final long startTick;
    private       long endTick;

    
    //
    //  ~  INIT  ~  //
    //

    MIDINote(byte hexNote, byte velocity, long startTick) {
        this.hexNote   = hexNote;
        this.velocity  = velocity;
        this.startTick = startTick;
        this.endTick   = -1;
    }


    //
    //  ~  SETTERS  ~  //
    //

    void setEndTick(long endTick) {
        this.endTick = endTick;
    }


    //
    //  ~  GETTERS  ~  //
    //

    public byte getHexNote() {
        return hexNote;
    }

    public byte getVelocity() {
        return velocity;
    }

    public int getTickLength() {
        return (int)(endTick - startTick);
    }

    public long startTick() {
        return startTick;
    }

    public long endTick() {
        return endTick;
    }


    //
    //  ~  COMPARISON  ~  //
    //

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + hexNote;
        result = prime * result + (int) startTick;
        return result;
    }

    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MIDINote other = (MIDINote) obj;
        if (hexNote != other.hexNote)
            return false;
        if (startTick != other.startTick)
            return false;
        return true;
    }
}
