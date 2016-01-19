package com.atompacman.lereza.core.midi.device;

import javax.sound.midi.Transmitter;

public interface MIDITransmitterProvider {

    public Transmitter getATransmitter() throws MIDIDeviceException;
    
    public void connectTo(MIDIReceiverProvider receiver) throws MIDIDeviceException;
}
