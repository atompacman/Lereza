package com.atompacman.lereza.core.midi.tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.sound.midi.MidiSystem;

import org.jfugue.midi.MidiParserListener;
import org.staccato.StaccatoParser;

public final class StaccatoUtils {

    //
    //  ~  INIT  ~  //
    //

    private StaccatoUtils() {
        
    }
    
    
    //
    //  ~  WRITE  ~  //
    //

    public static void writeToFile(String staccato, File path) throws IOException {
        writeToOutput(staccato, new FileOutputStream(path));
    }
    
    public static void writeToOutput(String staccato, OutputStream out) throws IOException {
        StaccatoParser parser = new StaccatoParser();
        MidiParserListener listener = new MidiParserListener();
        parser.addParserListener(listener);
        parser.parse(staccato);
        int fileType = MidiSystem.getMidiFileTypes(listener.getSequence())[0];
        MidiSystem.write(listener.getSequence(), fileType, out);
    }
}