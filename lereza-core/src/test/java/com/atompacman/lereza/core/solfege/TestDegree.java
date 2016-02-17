package com.atompacman.lereza.core.solfege;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.atompacman.lereza.core.theory.ChordType;
import com.atompacman.lereza.core.theory.Degree;
import com.atompacman.lereza.core.theory.ScaleDegree;

public class TestDegree {

	//------------ STATIC CONSTRUCTORS ------------\\

	@Test
	public void testValueOf() {
		Degree expected = Degree.of(ScaleDegree.I, ChordType.of(""));
		assertEquals(Degree.of("I"), expected);
		
		expected = Degree.of(ScaleDegree.II, ChordType.of("m7"));
		assertEquals(Degree.of("ii7"), expected);
		
		expected = Degree.of(ScaleDegree.VI, ChordType.of("mM7sus4"));
		assertEquals(Degree.of("viM7sus4"), expected);
	}
}
