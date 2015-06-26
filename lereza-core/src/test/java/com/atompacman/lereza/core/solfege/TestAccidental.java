package com.atompacman.lereza.core.solfege;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.atompacman.lereza.core.solfege.Accidental;

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
