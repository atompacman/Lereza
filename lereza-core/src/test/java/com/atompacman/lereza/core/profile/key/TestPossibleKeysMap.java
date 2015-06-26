package com.atompacman.lereza.core.profile.key;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.atompacman.lereza.core.piece.PieceTestsHelper;
import com.atompacman.lereza.core.piece.container.Bar;
import com.atompacman.lereza.core.piece.container.Part;
import com.atompacman.lereza.core.piece.container.Piece;
import com.atompacman.lereza.core.piece.tool.PieceNavigator;
import com.atompacman.lereza.core.profile.key.KeyProfilerTestHelper;
import com.atompacman.lereza.core.profile.key.PossibleKeysMap;
import com.atompacman.lereza.core.solfege.Pitch;
import com.atompacman.lereza.core.solfege.RythmicSignature;
import com.atompacman.lereza.core.solfege.Tone;
import com.atompacman.lereza.resources.TestFileDetector;

public class TestPossibleKeysMap {

	@Test
	public void buildSingleNotePossibleKeysMap() throws IOException {
		RythmicSignature stdRS = PieceTestsHelper.standardRythmicSign();
		Bar bar = new Bar(stdRS, 0);
		bar.addNote(Pitch.valueOf("C4"), 0, 1);
		Part part = new Part(1, stdRS);
		part.setBar(bar, 0);
		Piece piece = new Piece(null, stdRS);
		piece.addPart(part);
		piece.addPart(new Part(0, stdRS));
		
		PieceNavigator navig = new PieceNavigator(piece);
		PossibleKeysMap map = new PossibleKeysMap(navig);
		
		assertEquals(1, map.nbNotes(0));
		assertEquals(0, map.nbNotes(1));
	}
	
	@Test
	public void buildStandardPossibleKeysMap() throws IOException {
		String testFilePath = TestFileDetector.detectSingleFileForCurrentTest();
		List<List<Tone>> tones = KeyProfilerTestHelper.parseToneSequence(testFilePath);
		Piece piece = KeyProfilerTestHelper.buildPiece(tones);
		PieceNavigator navig = new PieceNavigator(piece);
		PossibleKeysMap map = new PossibleKeysMap(navig);
		map.getClass();
		
		assertEquals(7, map.nbNotes(0));
		assertEquals(-7, map.getInterval(1, 6).maxKey);
		assertEquals(-8, map.getInterval(1, 6).minKey);
		assertEquals(12, map.getInterval(0, 6).maxKey);
		assertEquals(7, map.getInterval(0, 6).minKey);
		assertTrue(map.toneCanBeOfKey(2, 1, -4));
		assertFalse(map.toneCanBeOfKey(3, 0, -3));
		
		try {
			map.getInterval(-14, 421);
		} catch (IllegalArgumentException e) {
			try {
				map.toneCanBeOfKey(0, 421, 0);
			} catch (IllegalArgumentException x) {
				return;
			}
		}
		fail("Exceptions should have occured.");
	}	
}
