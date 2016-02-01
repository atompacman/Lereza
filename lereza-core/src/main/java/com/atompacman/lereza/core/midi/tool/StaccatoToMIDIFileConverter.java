package com.atompacman.lereza.core.midi.tool;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.MidiSystem;

import org.jfugue.midi.MidiParserListener;
import org.staccato.StaccatoParser;

public class StaccatoToMIDIFileConverter {
    
    public static void main(String[] args) throws IOException {
        writeToFile("Cq Dq Eq Cq", new File("yolo.mid"));
    }
    
    public static void writeToFile(String staccato, File path) throws IOException {
        StaccatoParser parser = new StaccatoParser();
        MidiParserListener listener = new MidiParserListener();
        parser.addParserListener(listener);
        parser.parse(staccato);
        int fileType = MidiSystem.getMidiFileTypes(listener.getSequence())[0];
        MidiSystem.write(listener.getSequence(), fileType, path);
    }
}
