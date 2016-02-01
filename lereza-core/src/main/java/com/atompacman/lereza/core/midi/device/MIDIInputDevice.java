package com.atompacman.lereza.core.midi.device;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.Transmitter;

import com.atompacman.toolkat.exception.Throw;
import com.atompacman.toolkat.misc.Log;

public class MIDIInputDevice extends MIDIDevice implements MIDITransmitterProvider {

    //======================================= METHODS ============================================\\

    //------------------------------------- CONSTRUCTORS -----------------------------------------\\

    public MIDIInputDevice(MidiDevice device) {
        super(device);
    }

    
    //-------------------------------------- CONNECTION ------------------------------------------\\

    public void connectTo(MIDIReceiverProvider receiverProvider) throws MIDIDeviceException {
        try {
            Log.debug("Connecting \"%s\" to \"%s\"", this, receiverProvider);
            getTransmitter().setReceiver(receiverProvider.getAReceiver());
        } catch (MIDIDeviceException e) {
            Throw.a(MIDIDeviceException.class,e,"Could not connect %s to %s",this,receiverProvider);
        }
    }

    public Transmitter getATransmitter() throws MIDIDeviceException {
        return getTransmitter();
    }
}
