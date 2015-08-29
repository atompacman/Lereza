package com.atompacman.lereza.kpf;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.atompacman.toolkat.IO;
import com.atompacman.toolkat.misc.Log;

public class AudioLinesManager {

    //====================================== CONSTANTS ===========================================\\

    private static final String UNKNOWN_INFO = "Unknown";
    
    
    
    //======================================= METHODS ============================================\\

    //-------------------------------------- OPEN CLIP -------------------------------------------\\

    public static Clip openClip(String soundFile) throws LineUnavailableException, IOException, 
                                                         UnsupportedAudioFileException {
        return openClip(IO.getResource(soundFile));
    }
    
    public static Clip openClip(File soundFile) throws LineUnavailableException, IOException, 
                                                       UnsupportedAudioFileException {
        Clip clip = AudioSystem.getClip();
        clip.open(AudioSystem.getAudioInputStream(soundFile));
        return clip;
    }
    
    
    //---------------------------------------- PRINT ---------------------------------------------\\

    public static void printMixerInfo() {
        for (Mixer.Info info : AudioSystem.getMixerInfo()) {
            printLineInfo("Name",        info.getName());
            printLineInfo("Vendor",      info.getVendor());
            printLineInfo("Version",     info.getVersion());
            printLineInfo("Description", info.getDescription());
            Log.info("---------------------------------------------");
        }
    }
    
    public static void printSupportedAudioFileTypes() {
        for (AudioFileFormat.Type type : AudioSystem.getAudioFileTypes()) {
            printLineInfo("Name",       type.getExtension());
            printLineInfo("Extension",  type.toString());
            Log.info("---------------------------------------------");
        }
    }

    private static void printLineInfo(String field, String value) {
        if (!value.contains(UNKNOWN_INFO)) {
            Log.info("%-12s: %s", field, value);
        }
    }
}
