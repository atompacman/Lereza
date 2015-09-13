package com.atompacman.lereza.kpf;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.atompacman.lereza.core.solfege.TimeSignature;
import com.atompacman.toolkat.IO;
import com.atompacman.toolkat.misc.Log;

public class Metronome {

    //====================================== CONSTANTS ===========================================\\

    public static final double DEFAULT_INITIAL_BPM      = 120.0;
    public static final String DEFAULT_BAR_CLICK_SOUND  = "VT_RimShot.wav";
    public static final String DEFAULT_BEAT_CLICK_SOUND = "VT_RimShot_2.wav";



    //======================================= FIELDS =============================================\\

    // Resources
    private Clip barClick;
    private Clip beatClick;

    // Internal
    private Thread playback;
    
    // State
    private TimeSignature timeSign;
    private double        bpm;
    private boolean       isPlaying;



    //======================================= METHODS ============================================\\

    //--------------------------------- PUBLIC CONSTRUCTORS --------------------------------------\\

    public Metronome() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        this(IO.getResource(DEFAULT_BAR_CLICK_SOUND), 
             IO.getResource(DEFAULT_BEAT_CLICK_SOUND), 
             DEFAULT_INITIAL_BPM, 
             TimeSignature.STANDARD_4_4 );
    }
    
    public Metronome(double initBPM, TimeSignature timeSign) 
            throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        
        this(IO.getResource(DEFAULT_BAR_CLICK_SOUND), 
             IO.getResource(DEFAULT_BEAT_CLICK_SOUND), 
             initBPM, 
             timeSign);
    }
    
    public Metronome(File barSound, File beatSound, double initBPM, TimeSignature timeSign) 
            throws LineUnavailableException, IOException, UnsupportedAudioFileException {

        // Resources
        this.barClick  = AudioLinesManager.openClip(barSound);
        this.beatClick = AudioLinesManager.openClip(beatSound);
        
        // Internal
        this.playback  = new Thread(new Runnable() {

            public void run() {
                int beat = 0;
                
                while (true) {
                    if (beat % timeSign.getMeterNumerator() == 0) {
                        barClick.start();
                    } else {
                        beatClick.start();
                    }
                    try {
                        Thread.sleep((long)(60000.0 / bpm));
                    } catch (InterruptedException e) {
                        Log.error(e.getMessage());
                    }
                    if (beat % timeSign.getMeterNumerator() == 0) {
                        barClick.setFramePosition(0);
                    } else {
                        beatClick.setFramePosition(0);
                    }
                    ++beat;
                }
            }
        });
        
        // State
        this.timeSign  = timeSign;
        this.bpm       = initBPM;
        this.isPlaying = false;
    }


    //---------------------------------------- START ---------------------------------------------\\

    public void start() {
        playback.start();
        isPlaying = true;
    }

    public void stop() {
        playback.interrupt();
        isPlaying = false;
    }


    //--------------------------------------- SET BPM --------------------------------------------\\

    public void setBPM(double bpm) {
        this.bpm = bpm;
        Log.debug("BPM set to %.1f", bpm);
    }


    //--------------------------------------- GETTERS --------------------------------------------\\

    public double getBPM() {
        return bpm;
    }

    public TimeSignature getTimeSignature() {
        return timeSign;
    }
    

    //---------------------------------------- STATE ---------------------------------------------\\

    public boolean isPlaying() {
        return isPlaying;
    }
}
