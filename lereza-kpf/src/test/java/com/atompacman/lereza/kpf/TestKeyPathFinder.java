package com.atompacman.lereza.kpf;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

import com.atompacman.lereza.core.Wizard;
import com.atompacman.lereza.core.midi.sequence.MIDIFileReader;
import com.atompacman.lereza.core.piece.TiedNote;
import com.atompacman.lereza.core.piece.Piece;
import com.atompacman.lereza.core.piece.Stack;
import com.atompacman.lereza.kpf.key.BadKeyPathFinder;
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

        BadKeyPathFinder finder = new BadKeyPathFinder(IO.getResource(LRTFKWindow.KCW_FILE));
        
        TextInputBasedTest.launchTestsWithExpectedOutput(TEST_LIST_FILE, 
                input -> {
                    try {
                        Piece<Stack<TiedNote>> piece = reader.read(new File(input));
                        return finder.find(piece.getPart(0), 1).toString();
                    } catch (Exception e) {
                        fail(e.getMessage());
                    }
                    return null;
                });
    }
}
