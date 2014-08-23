package com.atompacman.lereza.common.solfege;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.atompacman.lereza.common.solfege.DiatonicTones;
import com.atompacman.lereza.common.solfege.Pitch;
import com.atompacman.lereza.common.solfege.Tone;

public class TestDiatonicTones {

	//------------ BETWEEN ------------\\

	@Test
	public void testBetween() {
		Tone a = Tone.valueOf("Cb");
		Tone b = Tone.valueOf("E#");
		assertTrue(DiatonicTones.between(a, b) == 2);
		
		Pitch c = Pitch.valueOf("A2");
		Pitch d = Pitch.valueOf("Eb3");
		assertTrue(DiatonicTones.between(c, d) == 5);
		
		c = Pitch.valueOf("A2");
		d = Pitch.valueOf("A2");
		assertTrue(DiatonicTones.between(c, d) == 0);
	}
}
