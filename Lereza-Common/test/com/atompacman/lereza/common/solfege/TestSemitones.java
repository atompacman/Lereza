package com.atompacman.lereza.common.solfege;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestSemitones {

	//------------ BETWEEN ------------\\

	@Test
	public void testBetween() {
		Pitch a = Pitch.valueOf("G4");
		Pitch b = Pitch.valueOf("C5");
		assertTrue(Semitones.between(a, b) == 5);
		
		a = Pitch.valueOf("Bb4");
		b = Pitch.valueOf("G#2");
		assertTrue(Semitones.between(a, b) == -26);
		
		Tone c = Tone.valueOf("E");
		Tone d = Tone.valueOf("Fb");
		assertTrue(Semitones.between(c, d) == 0);
		
		c = Tone.valueOf("Ab");
		d = Tone.valueOf("C#");
		assertTrue(Semitones.between(c, d) == -7);
	}
}
