package com.atompacman.lereza.core.midi.device;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;

import com.atompacman.toolkat.exception.Throw;
import com.atompacman.toolkat.misc.Log;

public class MIDIDeviceProvider {

    //====================================== CONSTANTS ===========================================\\

    private static final Set<String> DEVICES_TO_IGNORE = new HashSet<>(Arrays.asList(
            "[Unknown vendor] Microsoft MIDI Mapper (v5.0) - Windows MIDI_MAPPER"));
    
    
    
    //======================================= METHODS ============================================\\

    //--------------------------------------- GETTERS --------------------------------------------\\

    public static <T extends MIDIDevice> T getMIDIDeviceAs(String   deviceDesc, 
                                                           Class<T> deviceClazz) 
                                                           throws MIDIDeviceException {
        
        Object[] devices = getDeviceDescriptions().stream().filter(
                info -> infoToString(info).equalsIgnoreCase(deviceDesc)
                ).toArray();
        
        if (devices.length == 0) {
            Throw.a(MIDIDeviceException.class, "No device named \"%s\" found", deviceDesc);
        }
        
        if (devices.length > 1) {
            Log.warn("Found multiple devices named \"%s\". Arbitrarily choosing one", deviceDesc);
        }
        
        return getMIDIDeviceAs((MidiDevice.Info) devices[0], deviceClazz);
    }
    
    @SuppressWarnings("unchecked")
    public static <T extends MIDIDevice> T getMIDIDeviceAs(MidiDevice.Info info, 
                                                           Class<T>        deviceClazz) 
                                                           throws MIDIDeviceException {
        
        Log.info("Opening device \"%s\"", infoToString(info));
        
        MidiDevice device = null;
        
        try {
            device = MidiSystem.getMidiDevice(info);
            if (!device.isOpen()) {
                device.open();
            }
        } catch (MidiUnavailableException e) {
            Throw.a(MIDIDeviceException.class, e, "Could not open %s", infoToString(info));
        }

        if (deviceClazz == MIDISynthesizer.class  && device instanceof Synthesizer) {
            return (T) new MIDISynthesizer(device);
        }
        
        if (deviceClazz == MIDISequencer.class    && device instanceof Sequencer) {
            return (T) new MIDISequencer(device);
        }

        if (deviceClazz == MIDIOutputDevice.class && hasAnAvailableConnection(device, false)) {
            return (T) new MIDIOutputDevice(device);
        }
        
        if (deviceClazz == MIDIInputDevice.class  && hasAnAvailableConnection(device, true)) {
            return (T) new MIDIInputDevice(device);
        }
        
        Throw.a(MIDIDeviceException.class, "%s is not a %s", 
                infoToString(info), deviceClazz.getSimpleName());
        return null;
    }

    private static boolean hasAnAvailableConnection(MidiDevice device, boolean isAnInputDevice) {
        int numConnections = isAnInputDevice ? device.getMaxTransmitters():device.getMaxReceivers();
        return numConnections == -1 || numConnections > 0;
    }
    
    public static List<MidiDevice.Info> getDeviceDescriptions() {
        List<MidiDevice.Info> infos = new LinkedList<>();
        for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
            if (!DEVICES_TO_IGNORE.contains(infoToString(info))) {
                infos.add(info);
            }
        }
        return infos;
    }
    

    //----------------------------------- PRINT DEVICE INFO --------------------------------------\\

    public static void printAvailableDevices() throws MIDIDeviceException {
        for (MidiDevice.Info info : getDeviceDescriptions()) {
            Log.info(infoToString(info));
        }
    }
    
    static String infoToString(MidiDevice.Info info) {
        StringBuilder sb = new StringBuilder();
        sb.append('[').append(info.getVendor()).append("] ");
        sb.append(info.getName());
        sb.append(" (v").append(info.getVersion()).append(") - ");
        sb.append(info.getDescription());
        return sb.toString();
    }
}
