package com.atompacman.lereza.solfege;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.atompacman.lereza.solfege.NoteLetter;

public class TestNoteLetter {

	//------------ STATIC CONSTRUCTORS ------------\\

	@Test
	public void testFromDiatonicToneValue() {
		assertEquals(NoteLetter.fromDiatonicToneValue(4), NoteLetter.G);
		assertEquals(NoteLetter.fromDiatonicToneValue(15), NoteLetter.D);
		assertEquals(NoteLetter.fromDiatonicToneValue(-19), NoteLetter.E);
	}
	
	@Test
	public void testFromSemitoneValue() {
		List<NoteLetter> a = NoteLetter.withSemitoneValue(4);
		assertEquals(a, Arrays.asList(NoteLetter.E, NoteLetter.F));
	}
	
	
	//------------ CAN BE ASSIGNED FROM ------------\\

	@Test
	public void testCanBeAssignedFrom() {
		assertTrue(NoteLetter.A.canBeAssignedFrom(8));
		assertTrue(NoteLetter.A.canBeAssignedFrom(9));
		assertTrue(NoteLetter.A.canBeAssignedFrom(10));
		assertTrue(!NoteLetter.A.canBeAssignedFrom(11));
		assertTrue(NoteLetter.C.canBeAssignedFrom(11));
	}
	
	
	//------------ PREVIOUS / NEXT ------------\\

	@Test
	public void testGetNextAndPrevious() {
		assertEquals(NoteLetter.B.getNext().getPrevious(), NoteLetter.B);
		assertEquals(NoteLetter.B.getNext(), NoteLetter.C);
	}
}
