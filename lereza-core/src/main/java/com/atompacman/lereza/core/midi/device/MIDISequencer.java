package com.atompacman.lereza.core.midi.device;

import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Sequencer.SyncMode;

public class MIDISequencer extends MIDIInputDevice implements MIDIReceiverProvider {

    //======================================= METHODS ============================================\\

    //------------------------------------- CONSTRUCTORS -----------------------------------------\\

    public MIDISequencer(MidiDevice device) {
        super(device);
    }


    //-------------------------------------- CONNECTION ------------------------------------------\\

    public Receiver getAReceiver() throws MIDIDeviceException {
        return getReceiver();
    }
    
    
    //------------------------------------- GENERATE LOG -----------------------------------------\\

    protected List<String> generateInfoLog() {
        List<String> log = super.generateInfoLog();

        SyncMode[] masterSyncModes = ((Sequencer)device).getMasterSyncModes();
        log.add(formatInfoItem("Master sync modes", masterSyncModes[0]));
        for (int i = 1; i < masterSyncModes.length; ++i) {
            log.add(formatInfoItem("", masterSyncModes[i]));
        }

        SyncMode[] slaveSyncModes = ((Sequencer)device).getSlaveSyncModes();
        log.add(formatInfoItem("Slave sync modes", slaveSyncModes[0]));
        for (int i = 1; i < slaveSyncModes.length; ++i) {
            log.add(formatInfoItem("", slaveSyncModes[i]));
        }
        
        return log;
    }
}
