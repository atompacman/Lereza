package com.atompacman.lereza.common.solfege;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.atompacman.lereza.common.solfege.Accidental;

public class TestAccidental {
	
	//------------ STATIC CONSTRUCTORS ------------\\
	
	@Test
	public void testFromSemitoneAlteration() {
		assertTrue(Accidental.fromSemitoneAlteration(1) == Accidental.SHARP);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testBadFromSemitoneAlteration() {
		assertTrue(Accidental.fromSemitoneAlteration(11251) == Accidental.SHARP);
	}
	
	
	//------------ GETTERS ------------\\

	@Test
	public void testSemitoneAlteration() {
		assertTrue(Accidental.FLAT.semitoneAlteration() == -1);
	}
}
