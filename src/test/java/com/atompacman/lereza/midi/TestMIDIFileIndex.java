package com.atompacman.lereza.midi;

import org.junit.BeforeClass;
import org.junit.Test;

import com.atompacman.lereza.common.context.ContextElements;
import com.atompacman.lereza.common.helper.TestFileDetector;
import com.atompacman.lereza.exception.ContextElementsException;
import com.atompacman.lereza.exception.MIDIFileIndexParserException;

public class TestMIDIFileIndex {
	
	@BeforeClass
	public static void beforeAllTests() throws ContextElementsException {
		ContextElements.parse();
	}
	
	@Test
	public void completeTest() throws MIDIFileIndexParserException {
		MIDIFileIndexParser.parseIndex(TestFileDetector.detectSingleFileForCurrentTest());
	}
}