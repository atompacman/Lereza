package com.atompacman.lereza.core.solfege;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.atompacman.lereza.core.theory.Octave;

public class TestOctave {

	//------------ STATIC CONSTRUCTORS ------------\\

	@Test
	public void testFromInt() {
		assertEquals(Octave.FIVE, Octave.fromInt(5));
	}
	
	@Test
	public void testFromHex() {
		assertEquals(Octave.ZERO, Octave.fromHex(15));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void fromIntParamVerif() {
		Octave.fromInt(53);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void fromHexParamTooLowVerif() {
		Octave.fromHex(7);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void fromHexParamTooHighVerif() {
		Octave.fromHex(132);
	}
	
	
	//------------ VALUE ------------\\

	@Test
	public void semitoneValueExactness() {
		assertEquals(Octave.FIVE.semitoneValue(), 60);
	}
	
	@Test
	public void diatonicToneValueExactness() {
		assertEquals(Octave.FIVE.diatonicToneValue(), 35);
	}
}
