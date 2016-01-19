package com.atompacman.lereza.core.midi.device;

import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.Synthesizer;

public class MIDISynthesizer extends MIDIOutputDevice {

    //======================================= METHODS ============================================\\

    //------------------------------------- CONSTRUCTORS -----------------------------------------\\

    public MIDISynthesizer(MidiDevice device) {
        super(device);
    }
    
    
    //------------------------------------- GENERATE LOG -----------------------------------------\\

    protected List<String> generateInfoLog() {
        List<String> log = super.generateInfoLog();
        Synthesizer synth = (Synthesizer) device;
        
        log.add(formatInfoItem("Latency",            synth.getLatency()));
        log.add(formatInfoItem("Nb loaded instr.",   synth.getLoadedInstruments().length));
        log.add(formatInfoItem("Max polyphony",      synth.getMaxPolyphony()));
        log.add(formatInfoItem("Default soundbank",  synth.getDefaultSoundbank().getName()));
        
        return log;
    }
}
