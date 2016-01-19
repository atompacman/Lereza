package com.atompacman.lereza.core.midi.device;

import javax.sound.midi.Receiver;

public interface MIDIReceiverProvider {
    
    public Receiver getAReceiver() throws MIDIDeviceException;
}
