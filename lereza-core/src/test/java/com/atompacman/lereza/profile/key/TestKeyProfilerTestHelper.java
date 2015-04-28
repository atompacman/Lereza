package com.atompacman.lereza.profile.key;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.atompacman.lereza.piece.container.Note;
import com.atompacman.lereza.piece.container.Piece;
import com.atompacman.lereza.resources.TestFileDetector;
import com.atompacman.lereza.solfege.NoteLetter;
import com.atompacman.lereza.solfege.Tone;

public class TestKeyProfilerTestHelper {

	@Test
	public void testValidToneSequenceParsing() throws IOException {
		String testFilePath = TestFileDetector.detectSingleFileForCurrentTest();
		List<List<Tone>> tones = KeyProfilerTestHelper.parseToneSequence(testFilePath);
		
		assertEquals(4, tones.size());
		assertEquals(7, tones.get(0).size());
		assertEquals(Tone.valueOf(NoteLetter.E), tones.get(1).get(3));
	}

	@Test (expected = IllegalArgumentException.class)
	public void testInvalidToneSequenceParsing() throws IOException {
		String testFilePath = TestFileDetector.detectSingleFileForCurrentTest();
		KeyProfilerTestHelper.parseToneSequence(testFilePath);
	}
	
	@Test
	public void testPieceBuilding() throws IOException {
		String testFilePath = TestFileDetector.detectSingleFileForCurrentTest();
		List<List<Tone>> tones = KeyProfilerTestHelper.parseToneSequence(testFilePath);
		Piece piece = KeyProfilerTestHelper.buildPiece(tones);
				
		assertEquals(4, piece.numParts());
		assertEquals(1, piece.getPartNo(2).numBars());
		Set<Note> notes = piece.getPartNo(2).getBarNo(0).getNotesStartingAt(1);
		assertEquals(Tone.valueOf("Bb"), notes.iterator().next().getPitch().getTone());
	}
	
	@Test
	public void testLongPieceBuilding() throws IOException {
		String testFilePath = TestFileDetector.detectSingleFileForCurrentTest();
		List<List<Tone>> tones = KeyProfilerTestHelper.parseToneSequence(testFilePath);
		KeyProfilerTestHelper.buildPiece(tones);
	}
}
