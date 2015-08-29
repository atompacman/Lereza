package com.atompacman.lereza.core.midi.realtime;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

import com.atompacman.lereza.core.midi.tool.MIDINoteEventProcessor;

public abstract class PressedKeysListener implements Receiver {

    //======================================= FIELDS =============================================\\

    private boolean heldKeys[];
    
    
    
    //=================================== ABSTRACT METHODS =======================================\\

    //--------------------------------------- UPDATE ---------------------------------------------\\

    protected abstract void update(boolean heldKeys[]);
    
    
    
    //======================================= METHODS ============================================\\

    //---------------------------------- PUBLIC CONSTRUCTOR --------------------------------------\\

    public PressedKeysListener() {
        this.heldKeys = new boolean[256];
    }
    
    
    //---------------------------------------- SEND ----------------------------------------------\\
    
    public void send(MidiMessage message, long timestamp) {
        MIDINoteEventProcessor.process(message, timestamp,
                (hexNote, tick) -> heldKeys[hexNote] = true,
                (hexNote, tick) -> heldKeys[hexNote] = false);
        update(heldKeys);
    }
    
    
    //---------------------------------------- CLOSE ---------------------------------------------\\
    
    public void close() {
        
    }
}
