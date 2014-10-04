package com.atompacman.lereza.common.solfege;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.atompacman.lereza.common.solfege.DiatonicTones;
import com.atompacman.lereza.common.solfege.Pitch;
import com.atompacman.lereza.common.solfege.Tone;

public class TestDiatonicTones {

	//------------ BETWEEN ------------\\

	@Test
	public void testBetweenTones() {
		Tone a = Tone.valueOf("Cb");
		Tone b = Tone.valueOf("E#");
		assertTrue(DiatonicTones.between(a, Direction.ASCENDING, b) == 2);
		assertTrue(DiatonicTones.between(a, Direction.DESCENDING, b) == -5);
	}
	
	@Test
	public void testBetweenPitches() {
		Pitch a = Pitch.valueOf("A2");
		Pitch b = Pitch.valueOf("Eb3");
		assertTrue(DiatonicTones.between(a, b) == 4);
		
		a = Pitch.valueOf("B6");
		b = Pitch.valueOf("C4");
		assertTrue(DiatonicTones.between(a, b) == -20);
		
		a = Pitch.valueOf("A2");
		b = Pitch.valueOf("A2");
		assertTrue(DiatonicTones.between(a, b) == 0);
	}
}
