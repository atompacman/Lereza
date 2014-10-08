package com.atompacman.lereza.piece.container;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.atompacman.lereza.common.solfege.Pitch;
import com.atompacman.lereza.common.solfege.Value;
import com.atompacman.lereza.piece.PieceTestsHelper;
import com.atompacman.lereza.piece.PieceTestsHelper.BarInput;

public class TestBar {

	private Bar bar;
	
	@Before
	public void createEmptyBar() {
		bar = PieceTestsHelper.emptyBar();
	}
	
	
	//**********************************************************************************************
	//                                      ADD/GET NOTES
	//**********************************************************************************************
	/**
	 * <h1> Add determined notes to a bar </h1>
	 * <i> Deterministic test </i> <p>
	 * 
	 * Check the exactness of the method by comparing its results with expected results.
	 * <pre> Target: 
	 * 	{@link Bar#add(Pitch, int, int, boolean)}
	 * 	{@link Bar#getNotesAt(int)} </pre>
	 */
	@Test
	public void addDeterminedNotesNote() {
		Bar bar = PieceTestsHelper.determinedBar();
		
		Set<Note> notes = bar.getNotesAt(0);
		assertTrue(notes.size() == 3);
		assertTrue(notes.contains(new Note(Pitch.valueOf("C3"), Value.QUARTER)));
		assertTrue(notes.contains(new Note(Pitch.valueOf("E3"), Value.QUARTER)));
		assertTrue(notes.contains(new Note(Pitch.valueOf("G3"), Value.QUARTER)));
		
		notes = bar.getNotesAt(28);
		assertTrue(notes.size() == 1);
		Note noteAt28 = notes.iterator().next();
		assertEquals(noteAt28.getPitch(), Pitch.valueOf("C4"));
		assertEquals(noteAt28.getValue(), Value.SIXTEENTH);
		assertTrue(!noteAt28.isTied());
		
		notes = bar.getNotesAt(45);
		assertTrue(notes.size() == 2);
		
		assertTrue(bar.getNotesAt(63).isEmpty());
	}
	
	/**
	 * <h1> Randomly add notes to a bar </h1>
	 * <i> Pseudo-random test </i> <p>
	 * 
	 * Checks the absence of exceptions when the random inputs are added to a bar. For 1000 
	 * repetitions, 10 random notes are added to a bar. <p>
	 * 
	 * Target: {@link Bar#add(Pitch, int, int, boolean)}
	 */
	@Test
	public void randomlyAddNote() {
		for (int i = 0; i < 1000; ++i) {
			bar = PieceTestsHelper.emptyBar();
			
			for (int j = 0; j < 10; ++j) {
				BarInput input = PieceTestsHelper.nextRandBarInput();
				bar.add(input.pitch, input.pos, input.length, input.isTied);
			}
		}
	}
	
	
	
	
	//**********************************************************************************************
	//                               SPLITTING NOTES INTO VALUES
	//**********************************************************************************************
	/**
	 * <h1> Note time span splitting: Axiom #1 </h1>
	 * <i> Axiomatic test </i> <p>
	 * 
	 * Assert that the result of splitting a time span covering a whole 64 timeunits bar is a whole 
	 * note value. <p>
	 * 
	 * Target: {@link Bar#splitIntoValues(int, int)}
	 */
	@Test
	public void timeSpanSplittingAxiom1() {
		List<Value> expectedValues = Arrays.asList(Value.WHOLE);
		List<Value> actualValues = bar.splitIntoValues(0, 64);
		assertTrue(actualValues.equals(expectedValues));
	}
	
	/**
	 * <h1> Note time span splitting: Axiom #2 </h1>
	 * <i> Axiomatic test </i> <p>
	 * 
	 * Assert that the result of splitting a time span covering a whole bar minus a sixty-fourth of 
	 * a bar is every kind of values in decreasing order of length (minus the whole value). <p>
	 * 
	 * Target: {@link Bar#splitIntoValues(int, int)}
	 */
	@Test
	public void timeSpanSplittingAxiom2() {
		List<Value> expectedValues = Arrays.asList(Value.HALF, Value.QUARTER, Value.EIGHTH, 
				Value.SIXTEENTH, Value.THIRTYSECONTH, Value.SIXTYFORTH);
		List<Value> actualValues = bar.splitIntoValues(0, 63);
		assertTrue(actualValues.equals(expectedValues));
	}
	
	/**
	 * <h1> Splitting a determined time span </h1>
	 * <i> Deterministic test </i> <p>
	 * 
	 * Check the exactness of the method by comparing its results with expected results. <p>
	 * 
	 * Target: {@link Bar#splitIntoValues(int, int)}
	 */
	@Test
	public void splitDeterminedTimeSpan() {
		List<Value> expectedValues = Arrays.asList(Value.THIRTYSECONTH, Value.SIXTEENTH, 
				Value.EIGHTH, Value.EIGHTH, Value.SIXTEENTH, Value.THIRTYSECONTH, Value.SIXTYFORTH);
		List<Value> actualValues = bar.splitIntoValues(34, 63);
		assertTrue(actualValues.equals(expectedValues));
	}
}
