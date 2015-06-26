package com.atompacman.lereza.core.midi.device;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.atompacman.toolkat.exception.Throw;

public class MIDIDeviceManager {

    //====================================== SINGLETON ===========================================\\
    
    private static class InstanceHolder {
        private static final MIDIDeviceManager instance = new MIDIDeviceManager();
    }
    
    public static MIDIDeviceManager getInstance() {
        return InstanceHolder.instance;
    }
    
    
    
    //====================================== CONSTANTS ===========================================\\

    private static final Logger logger = LogManager.getLogger(MIDIDeviceManager.class);
    private static final Set<String> DEVICES_TO_IGNORE = new HashSet<>(Arrays.asList(
            "[Unknown vendor] Microsoft MIDI Mapper (v5.0) - Windows MIDI_MAPPER"));
    
    
    
    //======================================= FIELDS =============================================\\

    // Devices
    private Set<MIDIDeviceInfo>             detectedDevices;
    private Map<MIDIDeviceInfo, MidiDevice> openedDevices;
    
    
    
    //======================================= METHODS ============================================\\

    //---------------------------------- PRIVATE CONSTRUCTOR -------------------------------------\\

    private MIDIDeviceManager() {
        logger.info("Starting MIDI device manager");
        this.detectedDevices = new HashSet<>();
        this.openedDevices = new HashMap<>();
        refreshDevices();
    }
    
    
    //------------------------------------ CONNECT DEVICES ---------------------------------------\\

    public void connectDevices(MIDIDeviceInfo transmitter, 
                               MIDIDeviceInfo receiver) throws MIDIDeviceException {

        String receiverName = receiver.getAPIInfo().getName();
        try {
            connectDeviceToReceiver(transmitter, getDevice(receiver).getReceiver(), receiverName);
        } catch (MidiUnavailableException e) {
            Throw.a(MIDIDeviceException.class, "Could not connect " + 
                    transmitter.getAPIInfo().getName() + " to " + receiverName, e);
        }
    }
    
    public void connectDeviceToReceiver(MIDIDeviceInfo transmitter, 
            Receiver receiver, String receiverName) throws MIDIDeviceException {
        
        String transmiterName = transmitter.getAPIInfo().getName();
        try {
            logger.debug("Connecting \"" + transmiterName + "\" to \"" + receiverName + "\"");
            getDevice(transmitter).getTransmitter().setReceiver(receiver);
        } catch (MIDIDeviceException | MidiUnavailableException e) {
            Throw.a(MIDIDeviceException.class, "Could not connect " + 
                    transmiterName + " to " + receiverName, e);
        }
    }

    
    //------------------------------------- CLOSE DEVICES ----------------------------------------\\

    public void closeAllDevices() {
        logger.debug("Closing opened devices");
        for (MidiDevice device : openedDevices.values()) {
            device.close();
        }
    }
    
    public void finalize() {
        closeAllDevices();
    }
    
    
    //-------------------------------------- GET DEVICE ------------------------------------------\\

    private MidiDevice getDevice(MIDIDeviceInfo deviceInfo) throws MIDIDeviceException {
        MidiDevice device = openedDevices.get(deviceInfo);
        
        if (device == null) {
            logger.debug("Opening device \"" + deviceInfo + "\"");
            try {
                device = MidiSystem.getMidiDevice(deviceInfo.getAPIInfo());
                if (!device.isOpen()) {
                    device.open();
                }
            } catch (MidiUnavailableException e) {
                Throw.a(MIDIDeviceException.class, "Could not open " + deviceInfo, e);
            }
            openedDevices.put(deviceInfo, device);
        }

        return device;
    }

    public Set<MIDIDeviceInfo> getDeviceList() {
        return detectedDevices;
    }
    
    public List<MIDIDeviceInfo> getDevicesOfType(MIDIDeviceType type) throws MIDIDeviceException {
        List<MIDIDeviceInfo> devices = new LinkedList<>();    
        for (MIDIDeviceInfo info : detectedDevices) {
            if (type.matchesType(getDevice(info))) {
                devices.add(info);
            }
        }
        return devices;
    }
   
    
    //----------------------------------- PRINT DEVICE INFO --------------------------------------\\

    public void printAvailableDevicesInfo() throws MIDIDeviceException {
        for (MIDIDeviceInfo info : detectedDevices) {
            for (MIDIDeviceType type : MIDIDeviceType.values()) {
                if (type.matchesType(getDevice(info))) {
                    type.generateDeviceInfoLog(getDevice(info));
                }
            }
        }
    }
    
    
    //------------------------------------ REFRESH DEVICES ---------------------------------------\\
    
    public void refreshDevices() {
        logger.debug("Refreshing available devices");
        detectedDevices.clear();
        for (MidiDevice.Info deviceInfo : MidiSystem.getMidiDeviceInfo()) {
            MIDIDeviceInfo info = new MIDIDeviceInfo(deviceInfo);
            
            if (DEVICES_TO_IGNORE.contains(info.toString())) {
                logger.debug("Ignored : " + info);
            } else {
                logger.info("Detected: " + info);
                if (!detectedDevices.add(info)) {
                    logger.warn("Ignore additionnal MIDI devices with description \"" + info + "\"");
                }
            }
        }
    }
}
