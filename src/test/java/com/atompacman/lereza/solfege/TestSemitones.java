package com.atompacman.lereza.solfege;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.atompacman.lereza.solfege.Direction;
import com.atompacman.lereza.solfege.Pitch;
import com.atompacman.lereza.solfege.Semitones;
import com.atompacman.lereza.solfege.Tone;

public class TestSemitones {

	//------------ BETWEEN ------------\\

	@Test
	public void testBetweenPitches() {
		Pitch a = Pitch.valueOf("G4");
		Pitch b = Pitch.valueOf("C5");
		assertEquals(Semitones.between(a, b), 5);
		
		a = Pitch.valueOf("Bb4");
		b = Pitch.valueOf("G#2");
		assertEquals(Semitones.between(a, b), -26);
	}
	
	@Test
	public void testBetweenTones() {
		Tone a = Tone.valueOf("E");
		Tone b = Tone.valueOf("Fb");
		assertEquals(Semitones.between(a, Direction.ASCENDING, b), 0);
		assertEquals(Semitones.between(a, Direction.STRAIGHT, b), 0);
		
		a = Tone.valueOf("Ab");
		b = Tone.valueOf("C#");
		assertEquals(Semitones.between(a, Direction.ASCENDING, b), 5);
		assertEquals(Semitones.between(a, Direction.DESCENDING, b), -7);
		
		a = Tone.valueOf("D");
		b = Tone.valueOf("E");
		assertEquals(Semitones.between(a, Direction.ASCENDING, b), 2);
		assertEquals(Semitones.between(a, Direction.DESCENDING, b), -10);
	}
	
	
	//------------ NORMALIZE ------------\\

	@Test
	public void testNormalize() {
		assertEquals(Semitones.normalize(4), 4);
		assertEquals(Semitones.normalize(23), 11);
		assertEquals(Semitones.normalize(-1), 11);
		assertEquals(Semitones.normalize(12), 0);
		assertEquals(Semitones.normalize(-34), 2);
	}
}
