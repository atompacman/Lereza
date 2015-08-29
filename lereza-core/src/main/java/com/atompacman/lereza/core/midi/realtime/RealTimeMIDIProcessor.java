package com.atompacman.lereza.core.midi.realtime;

import java.util.LinkedList;
import java.util.List;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

import com.atompacman.toolkat.misc.Log;

public abstract class RealTimeMIDIProcessor implements Receiver {

    //======================================= FIELDS =============================================\\

    // Settings
    private final int timeIntervalMillis;

    // Status
    private boolean started;

    // Temporary
    private final List<MidiEvent> events;
    private long seqStartTime;



    //=================================== ABSTRACT METHODS =======================================\\

    public abstract void process(List<MidiEvent> events);



    //======================================= METHODS ============================================\\

    //---------------------------------- PUBLIC CONSTRUCTOR --------------------------------------\\

    public RealTimeMIDIProcessor(int timeIntervalMillis) {
        // Settings
        this.timeIntervalMillis = timeIntervalMillis;

        // Status
        this.started = false;

        // Temporary
        this.seqStartTime = 0;
        this.events = new LinkedList<>();
    }


    //---------------------------------------- START ---------------------------------------------\\

    public void start() {
        if (started) {
            Log.warn("Real-time MIDI processor was already started");
            return;
        }
        
        new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        seqStartTime = System.nanoTime();
                        Thread.sleep(timeIntervalMillis);
                        LinkedList<MidiEvent> copy = new LinkedList<>(events);
                        events.clear();
                        process(copy);
                    }
                } catch (InterruptedException e) {
                    Log.error(e);
                }
            }
        }).start();
        
        started = true;
    }


    //---------------------------------------- SEND ----------------------------------------------\\

    public void send(MidiMessage msg, long timestamp) {
        events.add(new MidiEvent(msg, (int) ((System.nanoTime() - seqStartTime) * 0.001)));
    }


    //---------------------------------------- CLOSE ---------------------------------------------\\

    public void close() {

    }
}
