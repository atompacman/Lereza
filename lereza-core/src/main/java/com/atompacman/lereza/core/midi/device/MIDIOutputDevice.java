package com.atompacman.lereza.core.midi.device;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.Receiver;

public class MIDIOutputDevice extends MIDIDevice implements MIDIReceiverProvider {

    //======================================= METHODS ============================================\\

    //------------------------------------- CONSTRUCTORS -----------------------------------------\\

    public MIDIOutputDevice(MidiDevice device) {
        super(device);
    }

    
    //-------------------------------------- CONNECTION ------------------------------------------\\

    public Receiver getAReceiver() throws MIDIDeviceException {
        return getReceiver();
    }
}
