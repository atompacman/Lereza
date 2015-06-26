package com.atompacman.lereza.core.midi.tool;

import java.util.Collection;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class MIDINoteEventProcessor {

    //====================================== CONSTANTS ===========================================\\

    private static final Logger logger = LogManager.getLogger(MIDINoteEventProcessor.class);

    
    
    //===================================== INNER TYPES ==========================================\\

    @FunctionalInterface
    public interface Function {
        void processNoteEvent(byte hexNote, long tick);
    }
    
    
    
    //======================================= METHODS ============================================\\

    //---------------------------------- PRIVATE CONSTRUCTOR -------------------------------------\\

    private MIDINoteEventProcessor() {
        
    }
    
    
    //--------------------------------------- PROCESS --------------------------------------------\\

    public static void process(Collection<MidiEvent> events, Function noteOn, Function noteOff) {
        for (MidiEvent event : events) {
            process(event, noteOn, noteOff);
        }
    }

    public static void process(MidiEvent event, Function noteOn, Function noteOff) {
        process(event.getMessage(), event.getTick(), noteOn, noteOff);
    }
    
    public static void process(MidiMessage msg, long tick, Function noteOn, Function noteOff) {
        switch (msg.getStatus()) {
        case ShortMessage.NOTE_ON:
            noteOn.processNoteEvent((byte)((ShortMessage) msg).getData1(), tick);
            break;
        case ShortMessage.NOTE_OFF:
            noteOff.processNoteEvent((byte)((ShortMessage) msg).getData1(), tick);
            break;
        default:
            logger.warn("Ignoring event with unknown status byte \"" + msg.getStatus());
        }
    }
}