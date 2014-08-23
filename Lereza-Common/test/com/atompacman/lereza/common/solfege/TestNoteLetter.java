package com.atompacman.lereza.common.solfege;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TestNoteLetter {

	//------------ STATIC CONSTRUCTORS ------------\\

	@Test
	public void testFromDiatonicToneValue() {
		assertTrue(NoteLetter.fromDiatonicToneValue(4) == NoteLetter.G);
		assertTrue(NoteLetter.fromDiatonicToneValue(15) == NoteLetter.D);
		assertTrue(NoteLetter.fromDiatonicToneValue(-19) == NoteLetter.E);
	}
	
	@Test
	public void testFromSemitoneValue() {
		List<NoteLetter> a = NoteLetter.fromSemitoneValue(4);
		assertTrue(a.equals(Arrays.asList(NoteLetter.E, NoteLetter.F)));
	}
	
	
	//------------ CAN BE ASSIGNED FROM ------------\\

	@Test
	public void testCanBeAssignedFrom() {
		assertTrue(NoteLetter.A.canBeAssignedFrom(8));
		assertTrue(NoteLetter.A.canBeAssignedFrom(9));
		assertTrue(NoteLetter.A.canBeAssignedFrom(10));
		assertTrue(!NoteLetter.A.canBeAssignedFrom(11));
	}
}
