package com.atompacman.lereza.common.solfege;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestSemitones {

	//------------ BETWEEN ------------\\

	@Test
	public void testBetweenPitches() {
		Pitch a = Pitch.valueOf("G4");
		Pitch b = Pitch.valueOf("C5");
		assertTrue(Semitones.between(a, b) == 5);
		
		a = Pitch.valueOf("Bb4");
		b = Pitch.valueOf("G#2");
		assertTrue(Semitones.between(a, b) == -26);
	}
	
	@Test
	public void testBetweenTones() {
		Tone a = Tone.valueOf("E");
		Tone b = Tone.valueOf("Fb");
		assertTrue(Semitones.between(a, Direction.ASCENDING, b) == 0);
		assertTrue(Semitones.between(a, Direction.STRAIGHT, b) == 0);
		
		a = Tone.valueOf("Ab");
		b = Tone.valueOf("C#");
		assertTrue(Semitones.between(a, Direction.ASCENDING, b) == 5);
		assertTrue(Semitones.between(a, Direction.DESCENDING, b) == -7);
		
		a = Tone.valueOf("D");
		b = Tone.valueOf("E");
		assertTrue(Semitones.between(a, Direction.ASCENDING, b) == 2);
		assertTrue(Semitones.between(a, Direction.DESCENDING, b) == -10);
	}
}
