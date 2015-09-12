package com.atompacman.lereza.rte;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;

import com.atompacman.lereza.core.midi.sequence.MIDIInstrument;
import com.atompacman.lereza.core.solfege.TimeSignature;

public abstract class MIDIChannelPlayer implements Transmitter {

    //====================================== CONSTANTS ===========================================\\

    private static final int GENERAL_MIDI_NUM_AVAIL_CHANNELS = 16;
    
    // Default constructor values
    private static final String DEFAULT_PLAYER_NAME = "DEFAULT NAME";
    private static final int    DEFAULT_CHANNEL     = 0;
    private static final double DEFAULT_INITIAL_BPM = 120;



    //======================================= FIELDS =============================================\\

    private final String    name;
    private final int       channel;
    private Receiver        receiver;
    protected TimeSignature timeSign;
    private long            interval;



    //=================================== ABSTRACT METHODS =======================================\\

    protected abstract void playNextTU();



    //======================================= METHODS ============================================\\

    //---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\

    MIDIChannelPlayer() {
        this(DEFAULT_PLAYER_NAME, DEFAULT_CHANNEL, TimeSignature.STANDARD_4_4, DEFAULT_INITIAL_BPM);
    }

    MIDIChannelPlayer(String name, int channel, TimeSignature timeSign, double initBPM) {
        if (channel < 0 || channel >= GENERAL_MIDI_NUM_AVAIL_CHANNELS) {
            throw new IllegalArgumentException("Invalid channel number \"" + channel + "\"");
        }
        this.name     = "MIDI channel " + (channel + 1) + " - " + name;
        this.channel  = channel;
        this.receiver = null;
        this.timeSign = timeSign;
        setBPM(initBPM);
    }


    //---------------------------------------- START ---------------------------------------------\\

    public void start() {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(interval);
                        playNextTU();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, name).start();
    }

    protected void playNote(int hexNote) {
        try {
            receiver.send(new ShortMessage(ShortMessage.NOTE_ON, channel, hexNote, 100), 0);
        } catch (InvalidMidiDataException e) {
            throw new RuntimeException(e);
        }
    }

    protected void changeInstrument(MIDIInstrument instrument) {
        try {
            receiver.send(new ShortMessage(ShortMessage.PROGRAM_CHANGE,
                    channel, instrument.ordinal(), 0), 0);
        } catch (InvalidMidiDataException e) {
            throw new RuntimeException(e);
        }
    }


    //--------------------------------------- SETTERS --------------------------------------------\\

    public void setBPM(double bpm) {
        interval = (long) Math.rint(60000.0 / (bpm * 16.0));
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }


    //--------------------------------------- GETTERS --------------------------------------------\\

    public int getChannel() {
        return channel;
    }

    public Receiver getReceiver() {
        return receiver;
    }


    //---------------------------------------- CLOSE ---------------------------------------------\\

    public void close() {

    }
}
