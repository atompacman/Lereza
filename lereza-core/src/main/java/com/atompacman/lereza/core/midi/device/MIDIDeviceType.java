package com.atompacman.lereza.core.midi.device;

import java.util.LinkedList;
import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Sequencer.SyncMode;
import javax.sound.midi.Synthesizer;

public enum MIDIDeviceType {

    MIDI_IN {
        public boolean matchesType(MidiDevice device) {
            return device.getMaxReceivers() != -1;
        }

        public List<String> generateDeviceInfoLog(MidiDevice device) {
            return generateBasicDeviceInfoLog(device);
        }
    }, 
    
    MIDI_OUT {
        public boolean matchesType(MidiDevice device) {
            return device.getMaxTransmitters() != -1;
        }

        public List<String> generateDeviceInfoLog(MidiDevice device) {
            return generateBasicDeviceInfoLog(device);
        }
    }, 
    
    SEQUENCER {
        public boolean matchesType(MidiDevice device) {
            return device instanceof Sequencer;
        }

        public List<String> generateDeviceInfoLog(MidiDevice device) {
            List<String> log = generateBasicDeviceInfoLog(device);

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
    }, 
    
    SYNTHESIZER {
        public boolean matchesType(MidiDevice device) {
            return device instanceof Synthesizer;
        }

        public List<String> generateDeviceInfoLog(MidiDevice device) {
            List<String> log = generateBasicDeviceInfoLog(device);
            Synthesizer synth = (Synthesizer) device;
            
            log.add(formatInfoItem("Latency",            synth.getLatency()));
            log.add(formatInfoItem("Nb loaded instr.",   synth.getLoadedInstruments().length));
            log.add(formatInfoItem("Max polyphony",      synth.getMaxPolyphony()));
            log.add(formatInfoItem("Default soundbank",  synth.getDefaultSoundbank().getName()));
            
            return log;
        }
    };
    
    
    //=================================== ABSTRACT METHODS =======================================\\

    //====================================== CONSTANTS ===========================================\\

    private static final int INFO_LIST_SPACING = 18;
    
    
    
    //=================================== ABSTRACT METHODS =======================================\\

    public abstract boolean matchesType(MidiDevice device);
    
    public abstract List<String> generateDeviceInfoLog(MidiDevice device);
    
    
    
    //======================================= METHODS ============================================\\

    //------------------------------------- GENERATE LOG -----------------------------------------\\

    private static List<String> generateBasicDeviceInfoLog(MidiDevice device) {
        List<String> log = new LinkedList<>();
        
        Info info = device.getDeviceInfo();
        Class<?> clazz = device.getClass();
        Class<?> superclass = clazz.getSuperclass();
        Class<?>[] interfaces = clazz.getInterfaces();

        log.add(formatInfoItem("Name",           info.getName()));
        log.add(formatInfoItem("Description",    info.getDescription()));
        log.add(formatInfoItem("Version",        info.getVersion()));
        log.add(formatInfoItem("Vendor",         info.getVendor()));
        log.add(formatInfoItem("Class",          clazz.getSimpleName()));
        log.add(formatInfoItem("Superclass",     superclass.getSimpleName()));

        if (interfaces.length == 0) {
            log.add(formatInfoItem("Interfaces", "None"));
        } else {
            log.add(formatInfoItem("Interfaces", interfaces[0].getSimpleName()));
            for (int i = 1; i < interfaces.length; ++i) {
                log.add(formatInfoItem("", interfaces[i].getSimpleName()));
            }
        }
        log.add(formatInfoItem("Max IN Ports",   device.getMaxReceivers() == -1 ? 
                "unlimited" : device.getMaxReceivers()));
        log.add(formatInfoItem("Max OUT Ports",  device.getMaxTransmitters() == -1 ? 
                "unlimited" : device.getMaxTransmitters()));
        
        return log;
    }
    
    private static String formatInfoItem(String infoType, Object infoItem) {
        return String.format("%-" + INFO_LIST_SPACING + "s: %s", infoType, infoItem);
    }
}
