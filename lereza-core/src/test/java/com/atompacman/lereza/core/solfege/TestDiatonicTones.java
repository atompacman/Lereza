package com.atompacman.lereza.core.solfege;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.atompacman.lereza.core.solfege.DiatonicTones;
import com.atompacman.lereza.core.solfege.Direction;
import com.atompacman.lereza.core.solfege.Pitch;
import com.atompacman.lereza.core.solfege.Tone;

public class TestDiatonicTones {

	//------------ BETWEEN ------------\\

	@Test
	public void testBetweenTones() {
		Tone a = Tone.valueOf("Cb");
		Tone b = Tone.valueOf("E#");
		assertEquals(DiatonicTones.between(a, Direction.ASCENDING, b), 2);
		assertEquals(DiatonicTones.between(a, Direction.DESCENDING, b), -5);
	}
	
	@Test
	public void testBetweenPitches() {
		Pitch a = Pitch.valueOf("A2");
		Pitch b = Pitch.valueOf("Eb3");
		assertEquals(DiatonicTones.between(a, b), 4);
		
		a = Pitch.valueOf("B6");
		b = Pitch.valueOf("C4");
		assertEquals(DiatonicTones.between(a, b), -20);
		
		a = Pitch.valueOf("A2");
		b = Pitch.valueOf("A2");
		assertEquals(DiatonicTones.between(a, b), 0);
	}


	//------------ NORMALIZE ------------\\

	@Test
	public void testNormalize() {
		assertEquals(DiatonicTones.normalize(4), 4);
		assertEquals(DiatonicTones.normalize(23), 2);
		assertEquals(DiatonicTones.normalize(-1), 6);
		assertEquals(DiatonicTones.normalize(12), 5);
		assertEquals(DiatonicTones.normalize(-34), 1);
	}
}
