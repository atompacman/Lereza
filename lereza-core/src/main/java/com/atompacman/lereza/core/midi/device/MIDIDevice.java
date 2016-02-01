package com.atompacman.lereza.core.midi.device;

import java.util.LinkedList;
import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

import com.atompacman.toolkat.exception.Throw;

public abstract class MIDIDevice {

    //====================================== CONSTANTS ===========================================\\

    private static final int INFO_LIST_SPACING = 18;
    
    
    
    //======================================= FIELDS =============================================\\

    protected final MidiDevice device;
    
    
    
    //======================================= METHODS ============================================\\

    //------------------------------------- CONSTRUCTORS -----------------------------------------\\

    public MIDIDevice(MidiDevice device) {
        this.device = device;
    }
    
   
    //-------------------------------------- CONNECTION ------------------------------------------\\

    protected Receiver getReceiver() throws MIDIDeviceException {
        try {
            return device.getReceiver();
        } catch (MidiUnavailableException e) {
            Throw.a(MIDIDeviceException.class, "Could not get a receiver", e);
            return null;
        }
    }
    
    protected Transmitter getTransmitter() throws MIDIDeviceException {
        try {
            return device.getTransmitter();
        } catch (MidiUnavailableException e) {
            Throw.a(MIDIDeviceException.class, "Could not get a transmitter", e);
            return null;
        }
    }
    
    
    //--------------------------------------- GETTERS --------------------------------------------\\

    public MidiDevice.Info info() {
        return device.getDeviceInfo();
    }
    
    
    //--------------------------------------- EQUALS ---------------------------------------------\\

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        MidiDevice.Info info = info();
        result = prime*result+((info.getDescription() == null)?0:info.getDescription().hashCode());
        result = prime*result+((info.getName() == null)       ?0:info.getName().hashCode());
        result = prime*result+((info.getVendor() == null)     ?0:info.getVendor().hashCode());
        result = prime*result+((info.getVersion() == null)    ?0:info.getVersion().hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MIDIDevice other = (MIDIDevice) obj;
        MidiDevice.Info info = info();
        if (info.getDescription() == null) {
            if (other.info().getDescription() != null)
                return false;
        } else if (!info.getDescription().equals(other.info().getDescription()))
            return false;
        if (info.getName() == null) {
            if (other.info().getName() != null)
                return false;
        } else if (!info.getName().equals(other.info().getName()))
            return false;
        if (info.getVendor() == null) {
            if (other.info().getVendor() != null)
                return false;
        } else if (!info.getVendor().equals(other.info().getVendor()))
            return false;
        if (info.getVersion() == null) {
            if (other.info().getVersion() != null)
                return false;
        } else if (!info.getVersion().equals(other.info().getVersion()))
            return false;
        return true;
    }
    
    
    //---------------------------------------- CLOSE ---------------------------------------------\\

    protected void finalize() {
        device.close();
    }
    
    
    //------------------------------------- GENERATE LOG -----------------------------------------\\

    protected List<String> generateInfoLog() {
        List<String> log = new LinkedList<>();
        
        Class<?> clazz = device.getClass();
        Class<?> superclass = clazz.getSuperclass();
        Class<?>[] interfaces = clazz.getInterfaces();

        log.add(formatInfoItem("Name",           info().getName()));
        log.add(formatInfoItem("Description",    info().getDescription()));
        log.add(formatInfoItem("Version",        info().getVersion()));
        log.add(formatInfoItem("Vendor",         info().getVendor()));
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
    
    protected static String formatInfoItem(String infoType, Object infoItem) {
        return String.format("%-" + INFO_LIST_SPACING + "s: %s", infoType, infoItem);
    }
    
    
    //-------------------------------------- TO STRING -------------------------------------------\\

    public String toString() {
        return MIDIDeviceProvider.infoToString(info());
    }
}
