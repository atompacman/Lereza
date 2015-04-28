package com.atompacman.lereza.piece.container;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.atompacman.lereza.piece.PieceTestsHelper;
import com.atompacman.lereza.piece.PieceTestsHelper.BarInput;
import com.atompacman.lereza.solfege.Pitch;
import com.atompacman.lereza.solfege.Value;

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
	 * 	{@link Bar#getAllNotesAt(int)} </pre>
	 */
	@Test
	public void addDeterminedNotesNote() {
		Bar bar = PieceTestsHelper.determinedBar();
		
		Set<Note> notes = bar.getAllNotesAt(0);
		assertEquals(notes.size(), 3);
		assertTrue(notes.contains(Note.valueOf(Pitch.valueOf("C3"), Value.QUARTER)));
		assertTrue(notes.contains(Note.valueOf(Pitch.valueOf("E3"), Value.QUARTER)));
		assertTrue(notes.contains(Note.valueOf(Pitch.valueOf("G3"), Value.QUARTER)));
		
		notes = bar.getAllNotesAt(28);
		assertEquals(notes.size(), 1);
		Note noteAt28 = notes.iterator().next();
		assertEquals(noteAt28.getPitch(), Pitch.valueOf("C2"));
		assertEquals(noteAt28.getValue(), Value.SIXTEENTH);
		assertTrue(!noteAt28.isTied());
		
		notes = bar.getAllNotesAt(47);
		assertEquals(notes.size(), 2);
		
		assertTrue(bar.getAllNotesAt(63).isEmpty());
	}
	
	/**
	 * <h1> Confirm difference between note getters </h1>
	 * <i> Deterministic test </i> <p>
	 * 
	 * Check the difference between the two note getters.
	 * <pre> Target: 
	 * 	{@link Bar#getNotesStartingAt(int)}
	 * 	{@link Bar#getAllNotesAt(int)} </pre>
	 */
	@Test
	public void differentNoteGetters() {
		Bar bar = PieceTestsHelper.determinedBar();
		
		Set<Note> notes = bar.getNotesStartingAt(46);
		assertEquals(notes.size(), 1);
		Note startingNote = notes.iterator().next();
		
		notes = bar.getAllNotesAt(46);
		assertEquals(notes.size(), 2);
		assertTrue(notes.contains(startingNote));
		notes.remove(startingNote);
		assertEquals(notes.iterator().next(), Note.valueOf(Pitch.valueOf("E2"), Value.SIXTEENTH));
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
				if (input.isTied) {
					bar.addTiedNote(input.pitch, input.pos, input.length);
				} else {
					bar.addNote(input.pitch, input.pos, input.length);
				}
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
		assertEquals(actualValues, expectedValues);
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
		assertEquals(actualValues, expectedValues);
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
		assertEquals(actualValues, expectedValues);
	}
}
