package com.atompacman.lereza.rte;

import java.util.LinkedList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import com.atompacman.lereza.core.midi.device.MIDIDeviceException;
import com.atompacman.lereza.core.midi.device.MIDIDeviceProvider;
import com.atompacman.lereza.core.midi.device.MIDIOutputDevice;
import com.atompacman.lereza.core.solfege.TimeSignature;
import com.atompacman.toolkat.misc.StringHelper;

public class RealTimeCompositionEngine implements Runnable {

    //====================================== CONSTANTS ===========================================\\

    public static final int GENERAL_MIDI_PERCU_CHANNEL      = 9;
    public static final int NUM_AVAIL_GENERAL_MIDI_CHANNELS = 16;



    //======================================= FIELDS =============================================\\

    private final RealTimeGenerator[] channels;
    private long                    intervalMS;
    private TimeSignature           timeSign;
    private int                     currTU;
    private final MIDIOutputDevice  outputDevice;
    private final Receiver          receiver;
    
    
    
    //======================================= METHODS ============================================\\

    //------------------------------------- CONSTRUCTORS -----------------------------------------\\

    RealTimeCompositionEngine(String midiOutputDeviceDesc) throws MIDIDeviceException {
        this(MIDIDeviceProvider.getMIDIDeviceAs(midiOutputDeviceDesc, MIDIOutputDevice.class));
    }

    RealTimeCompositionEngine(MIDIOutputDevice outputDevice) throws MIDIDeviceException {
        this.channels     = new RealTimeGenerator[NUM_AVAIL_GENERAL_MIDI_CHANNELS];
        setBPM(120.0);
        this.timeSign     = TimeSignature.STANDARD_4_4;
        this.currTU       = 0;
        this.outputDevice = outputDevice;
        this.receiver     = outputDevice.getAReceiver();
    }
    

    //---------------------------------------- START ---------------------------------------------\\

    public void start() {
        new Thread(this, StringHelper.splitClassName(this)).start();
    }
    
    public void run() {
        while (true) {
            try {
                tick();
            } catch (InterruptedException | InvalidMidiDataException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    private void tick() throws InvalidMidiDataException, InterruptedException {
        // Save time before doing work
        long start = System.nanoTime();
        
        // Ask composers for note on events
        List<ShortMessage> events = new LinkedList<>();
        for (int i = 0; i < NUM_AVAIL_GENERAL_MIDI_CHANNELS; ++i) {
            RealTimeGenerator channel = channels[i];
            if (channel == null) {
                continue;
            }
            
            List<Byte> hexNotes = channel.generate(currTU);
            
            if (hexNotes == null) {
                continue;
            }
            
            for (Byte hexNote : hexNotes) {
                events.add(new ShortMessage(ShortMessage.NOTE_ON, i, hexNote, 100));
            }
        }
        
        // Play notes
        for (ShortMessage event : events) {
            receiver.send(event, 0);
        }
        
        // Wait for the interval to complete
        Thread.sleep(intervalMS - (long)((System.nanoTime() - start) * 0.000001));
        
        // Increment timeunits
        ++currTU;
    }
    
    
    //--------------------------------------- SETTERS --------------------------------------------\\

    public void setChannelComposer(int channel, RealTimeGenerator composer) {
        if (channel < 0 || channel >= NUM_AVAIL_GENERAL_MIDI_CHANNELS) {
            throw new IllegalArgumentException("Invalid channel number \"" + channel + "\"");
        }
        try {
            channels[channel] = composer;
        } catch (IllegalArgumentException | SecurityException  e) {
            throw new RuntimeException(e);
        }
    }

    public void setPercussionChannelComposer(RealTimeGenerator composer) {
        setChannelComposer(GENERAL_MIDI_PERCU_CHANNEL, composer);
    }
    
    public void setBPM(double bpm) {
        intervalMS = (long) Math.rint(60000.0 / (bpm * 16.0));
    }
    
    public void setTimeSignature(TimeSignature timeSign) {
        this.timeSign = timeSign;
    }
}
