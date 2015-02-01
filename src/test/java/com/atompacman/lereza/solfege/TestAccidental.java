package com.atompacman.lereza.solfege;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.atompacman.lereza.solfege.Accidental;

public class TestAccidental {
	
	//------------ STATIC CONSTRUCTORS ------------\\
	
	@Test
	public void testFromSemitoneAlteration() {
		assertEquals(Accidental.fromSemitoneAlteration(1), Accidental.SHARP);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testBadFromSemitoneAlteration() {
		assertEquals(Accidental.fromSemitoneAlteration(11251), Accidental.SHARP);
	}
	
	
	//------------ GETTERS ------------\\

	@Test
	public void testSemitoneAlteration() {
		assertEquals(Accidental.FLAT.semitoneAlteration(), -1);
	}
}
