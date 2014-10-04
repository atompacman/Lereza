package com.atompacman.lereza.midi;

import org.junit.BeforeClass;
import org.junit.Test;

import com.atompacman.lereza.common.context.ContextElements;
import com.atompacman.lereza.common.helper.TestFileDetector;
import com.atompacman.lereza.exception.ContextElementsException;
import com.atompacman.lereza.exception.MIDIFileIndexParserException;
import com.atompacman.lereza.exception.MIDIFileReaderException;

public class TestMIDIFileReader {

	@BeforeClass
	public static void beforeAllTests() throws ContextElementsException, MIDIFileIndexParserException {
		ContextElements.parse();
		MIDIFileIndexParser.parse();
	}
	
	/**
	 * Concerto In D, BWV 972 - I - Allegro.mid
	 * @throws MIDIFileReaderException
	 */
	@Test
	public void standardRunningStatus() throws MIDIFileReaderException {
		new MIDIFileReader().read(TestFileDetector.detectSingleFileForCurrentTest());
	}
	
	/**
	 * Prelude & Fugue No.1 In C, BWV 846 - Fugue.mid
	 * @throws MIDIFileReaderException
	 */
	@Test
	public void standardNoteOnOff() throws MIDIFileReaderException {
		new MIDIFileReader().read(TestFileDetector.detectSingleFileForCurrentTest());
	}
}
