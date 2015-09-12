package com.atompacman.lereza.kpf.key;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;

import com.atompacman.lereza.core.midi.sequence.MIDIFileReaderException;
import com.atompacman.lereza.kpf.ScoreRenderer;
import com.atompacman.toolkat.gui.GUIUtils;
import com.xenoage.utils.exceptions.InvalidFormatException;

public class TestScoreRenderer {

    public static void main(String[] args) throws InvalidFormatException, 
            IOException, InvalidMidiDataException, MIDIFileReaderException {
        File file = new File("C:/Users/Utilisateur/Downloads/reve.xml");
        GUIUtils.displayImageInWindow(ScoreRenderer.renderScoreFromMXMLFile(file, 0, 1));
    }
}
