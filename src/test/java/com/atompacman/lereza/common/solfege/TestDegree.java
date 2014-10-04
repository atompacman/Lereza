package com.atompacman.lereza.common.solfege;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestDegree {

	//------------ STATIC CONSTRUCTORS ------------\\

	@Test
	public void testValueOf() {
		Degree expected = new Degree(ScaleDegree.I, ChordType.valueOf(""));
		assertTrue(Degree.valueOf("I").equals(expected));
		
		expected = new Degree(ScaleDegree.II, ChordType.valueOf("m7"));
		assertTrue(Degree.valueOf("ii7").equals(expected));
		
		expected = new Degree(ScaleDegree.VI, ChordType.valueOf("mM7sus4"));
		assertTrue(Degree.valueOf("viM7sus4").equals(expected));
	}
}
