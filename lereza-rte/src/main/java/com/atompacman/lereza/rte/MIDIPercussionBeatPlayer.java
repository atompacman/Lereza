package com.atompacman.lereza.rte;

import java.io.IOException;
import java.util.Map.Entry;

import com.atompacman.lereza.core.midi.device.MIDIDeviceException;
import com.atompacman.lereza.core.midi.device.MIDIDeviceInfo;
import com.atompacman.lereza.core.midi.device.MIDIDeviceManager;
import com.atompacman.lereza.core.midi.sequence.MIDIInstrument;
import com.atompacman.lereza.core.profile.drum.DrumBeat;
import com.atompacman.lereza.core.profile.drum.PercussionElement;
import com.atompacman.lereza.core.profile.drum.PercussionPattern;
import com.atompacman.toolkat.IO;

public class MIDIPercussionBeatPlayer extends MIDIChannelPlayer {

    //====================================== CONSTANTS ===========================================\\

    private static final int GENERAL_MIDI_PERCU_CHANNEL = 9;
    
    
    
    //======================================= FIELDS =============================================\\

    private DrumBeat beat;
    private int      tu;
    
    
    
    //======================================= METHODS ============================================\\

    public static void main(String[] args) throws MIDIDeviceException, IOException {
        MIDIDeviceManager manager = MIDIDeviceManager.getInstance();
        MIDIDeviceInfo output = manager.getDeviceList().stream().filter(
                device -> device.getAPIInfo().getName().equals("Microsoft GS Wavetable Synth")
                ).findAny().get();
        DrumBeat beat = new DrumBeat(IO.getResourceByteBuffer("drum_beat_2.ldb"));
        MIDIPercussionBeatPlayer mpbp = new MIDIPercussionBeatPlayer(beat, 120);
        manager.connectTransmitterToDevice(mpbp, output, "MIDIPercussionBeatPlayer");
        mpbp.start();
    }
    
    //---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\

    MIDIPercussionBeatPlayer(DrumBeat beat, double initBPM) {
        super("Percussions", GENERAL_MIDI_PERCU_CHANNEL, beat.getRythSign(), initBPM);
        this.beat = beat;
        this.tu   = 0;
    }


    //---------------------------------------- INIT ----------------------------------------------\\

    protected void init() {
        changeInstrument(MIDIInstrument.STEEL_DRUMS);
    }
    
    
    //---------------------------------------- PLAY ----------------------------------------------\\

    protected void playNextTU() {
        for (Entry<PercussionElement, PercussionPattern> entry : beat.getPatterns().entrySet()) {
            PercussionPattern pattern = entry.getValue();
            if (pattern.hasAHit(tu % pattern.getLengthTU())) {
                playNote(entry.getKey().getHexNote());
            }
        }
        tu = (tu + 1) % beat.getLengthTU();
    }
}
