package com.atompacman.lereza.core.midi;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.InputStream;

import org.jfugue.midi.MidiFileManager;
import org.junit.Test;

import com.atompacman.lereza.core.Parameters;
import com.atompacman.lereza.core.Wizard;
import com.atompacman.lereza.core.Parameters.Paths.Test.MIDI;
import com.atompacman.lereza.core.midi.sequence.MIDIFileReader;
import com.atompacman.toolkat.IO;
import com.atompacman.toolkat.test.AbstractTest;
import com.atompacman.toolkat.test.TextInputBasedTest;

public class TestMIDIFileReader extends AbstractTest {

    //==================================== STATIC FIELDS =========================================\\

    private static MIDIFileReader reader;

    
    
    //================================== FUNCTIONNAL TESTS =======================================\\

	@Test
	public void testMIDIFileReader() {
	    Wizard.manualInit();
	    reader = Wizard.getModule(MIDIFileReader.class);
	    TextInputBasedTest.launchTestsWithExpectedOutput(MIDI.MIDI_FILE_READER_TEST_LIST, 
	            input -> launchTest(Parameters.Paths.MIDI_ROOT_DIR + input));
	}
	
	private String launchTest(String midiFile) {
        try {
            InputStream is = IO.getResourceasStream(midiFile);
            String staccato = MidiFileManager.loadPatternFromMidi(is).toString();
            System.out.println(staccato);
            return reader.read(new File(midiFile)).toString();
        } catch (Exception e) {
            fail(e.getMessage());
        }
        return null;
	}
}
