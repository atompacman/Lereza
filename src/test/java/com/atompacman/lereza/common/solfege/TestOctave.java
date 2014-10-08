package com.atompacman.lereza.common.solfege;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.atompacman.lereza.common.solfege.Octave;

public class TestOctave {

	//------------ STATIC CONSTRUCTORS ------------\\

	@Test
	public void testFromInt() {
		assertTrue(Octave.FIVE == Octave.fromInt(5));
	}
	
	@Test
	public void testFromHex() {
		assertTrue(Octave.ZERO == Octave.fromHex(15));
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
		assertTrue(Octave.FIVE.semitoneValue() == 60);
	}
	
	@Test
	public void diatonicToneValueExactness() {
		assertTrue(Octave.FIVE.diatonicToneValue() == 35);
	}
}
