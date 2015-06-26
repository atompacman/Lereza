package com.atompacman.lereza.core.midi.device;

import javax.sound.midi.MidiDevice;

public class MIDIDeviceInfo {
    
    //======================================= FIELDS =============================================\\

    private MidiDevice.Info info;


    
    //======================================= METHODS ============================================\\

    //---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\

    MIDIDeviceInfo(MidiDevice.Info info) {
        this.info = info;
    }
    

    //--------------------------------------- GETTERS --------------------------------------------\\

    public MidiDevice.Info getAPIInfo() {
        return info;
    }
    
    
    //--------------------------------------- EQUALS ---------------------------------------------\\

    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        MIDIDeviceInfo other = (MIDIDeviceInfo) obj;
        if (info.getDescription() == null) {
            if (other.info.getDescription() != null)
                return false;
        } else if (!info.getDescription().equals(other.info.getDescription()))
            return false;
        if (info.getName() == null) {
            if (other.info.getName() != null)
                return false;
        } else if (!info.getName().equals(other.info.getName()))
            return false;
        if (info.getVendor() == null) {
            if (other.info.getVendor() != null)
                return false;
        } else if (!info.getVendor().equals(other.info.getVendor()))
            return false;
        if (info.getVersion() == null) {
            if (other.info.getVersion() != null)
                return false;
        } else if (!info.getVersion().equals(other.info.getVersion()))
            return false;
        return true;
    }

    
    //-------------------------------------- TO STRING -------------------------------------------\\

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[').append(info.getVendor()).append("] ");
        sb.append(info.getName());
        sb.append(" (v").append(info.getVersion()).append(") - ");
        sb.append(info.getDescription());
        return sb.toString();
    }
}
