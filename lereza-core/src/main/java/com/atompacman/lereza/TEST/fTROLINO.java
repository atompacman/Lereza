package com.atompacman.lereza.TEST;

import java.util.Arrays;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

public class fTROLINO {

    public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException, InterruptedException {
        for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
            System.out.println(info.getName() + " " + info.getDescription() + " " + info.getVendor() + " " + info.getVersion());
        }
        
        MidiDevice.Info info = Arrays.asList(MidiSystem.getMidiDeviceInfo()).stream().filter(i -> i.getName().contains("loop") && i.getDescription().contains("External")).findAny().get();
        MidiDevice device = MidiSystem.getMidiDevice(info);
        device.open();
        System.out.println(device.getMaxReceivers());
        System.out.println(device.getMaxTransmitters());
        Receiver receiver = device.getReceiver();
        receiver.send(new ShortMessage(ShortMessage.NOTE_ON, 73, 100), -1);
        Thread.sleep(1000);
        receiver.send(new ShortMessage(ShortMessage.NOTE_OFF, 73, 100), -1);
    }
}
