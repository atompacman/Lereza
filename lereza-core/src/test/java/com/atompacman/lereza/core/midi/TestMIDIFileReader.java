package com.atompacman.lereza.core.midi;

import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Test;

import com.atompacman.lereza.core.Parameters;
import com.atompacman.lereza.core.Parameters.Paths.Test.MIDI;
import com.atompacman.lereza.core.Wizard;
import com.atompacman.lereza.core.midi.sequence.MIDIFileReader;
import com.atompacman.toolkat.test.AbstractTest;
import com.atompacman.toolkat.test.TextInputBasedTest;


public class TestMIDIFileReader extends AbstractTest {

    //================================== FUNCTIONNAL TESTS =======================================\\

    @Test
    public void testMIDIFileReader() {
        Wizard.manualInit();
        MIDIFileReader reader = Wizard.getModule(MIDIFileReader.class);
        TextInputBasedTest.launchTestsWithExpectedOutput(MIDI.MIDI_FILE_READER_TEST_LIST, 
                input -> {
                    try {
                        File file = new File(Parameters.MIDI.MIDI_TEST_DIR, input);
                        return reader.read(file).toStaccato();
                    } catch (Exception e) {
                        fail(e.getMessage());
                    }
                    return null;
                });
    }
}
