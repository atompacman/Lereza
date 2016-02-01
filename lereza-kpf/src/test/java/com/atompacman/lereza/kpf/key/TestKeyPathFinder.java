package com.atompacman.lereza.kpf.key;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

import com.atompacman.lereza.core.Wizard;
import com.atompacman.lereza.core.midi.sequence.MIDIFileReader;
import com.atompacman.lereza.core.piece.AbstractPiece;
import com.atompacman.lereza.core.piece.NoteStack;
import com.atompacman.lereza.kpf.LRTFKWindow;
import com.atompacman.lereza.pluggin.builtin.key.KeyChangeAnalyzer;
import com.atompacman.toolkat.IO;
import com.atompacman.toolkat.test.TextInputBasedTest;

public class TestKeyPathFinder {

    //====================================== CONSTANTS ===========================================\\

    private static final String TEST_LIST_FILE = "tests.txt";



    //================================== FUNCTIONNAL TESTS =======================================\\

    @Test
    public void testKeyPathFinder() throws FileNotFoundException {
        Wizard.manualInit();
        MIDIFileReader reader = Wizard.getModule(MIDIFileReader.class);

        File kwcFile = IO.getResource(LRTFKWindow.KCW_FILE);
        KeyChangeAnalyzer detector = new KeyChangeAnalyzer(kwcFile, 64, 0.5);
        
        TextInputBasedTest.launchTestsWithExpectedOutput(TEST_LIST_FILE, 
                input -> {
                    try {
                        AbstractPiece<NoteStack> piece = reader.read(new File(input));
                        return detector.detect(piece.getPart(0)).toString();
                    } catch (Exception e) {
                        fail(e.getMessage());
                    }
                    return null;
                });
    }
}
