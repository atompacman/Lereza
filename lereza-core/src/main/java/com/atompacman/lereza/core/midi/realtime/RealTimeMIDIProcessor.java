package com.atompacman.lereza.core.midi.realtime;

import java.util.LinkedList;
import java.util.List;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class RealTimeMIDIProcessor implements Receiver {

    //====================================== CONSTANTS ===========================================\\

    private static final Logger logger = LogManager.getLogger(RealTimeMIDIProcessor.class);
    
    public static final int MILLIS_PER_TIMEUNIT = 20;

    

    //======================================= FIELDS =============================================\\

    private long seqStartTime;
    private List<MidiEvent> events;
    


    //=================================== ABSTRACT METHODS =======================================\\

    public abstract void process(List<MidiEvent> events, int endTU);



    //======================================= METHODS ============================================\\

    //---------------------------------- PUBLIC CONSTRUCTOR --------------------------------------\\

    public RealTimeMIDIProcessor(int timeIntervalMillis) {
        this.seqStartTime = 0;
        this.events = new LinkedList<>();
        
        new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        seqStartTime = System.currentTimeMillis();
                        Thread.sleep(timeIntervalMillis);
                        process(new LinkedList<>(events), timeIntervalMillis / MILLIS_PER_TIMEUNIT);
                        events.clear();
                    }
                } catch (InterruptedException e) {
                    logger.error(e);
                }
            }
        }).start();
    }

    
    //---------------------------------------- SEND ----------------------------------------------\\

    public void send(MidiMessage msg, long timestamp) {
        events.add(new MidiEvent(msg, (int) (System.currentTimeMillis() 
                - seqStartTime) / MILLIS_PER_TIMEUNIT));
    }


    //--------------------------------------- GETTERS --------------------------------------------\\

    public List<MidiEvent> getEvents() {
        return events;
    }
    
    
    //---------------------------------------- CLOSE ---------------------------------------------\\

    public void close() {

    }
}
