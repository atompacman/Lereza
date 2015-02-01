package com.atompacman.lereza.solfege;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.atompacman.lereza.solfege.ChordType;
import com.atompacman.lereza.solfege.Degree;
import com.atompacman.lereza.solfege.ScaleDegree;

public class TestDegree {

	//------------ STATIC CONSTRUCTORS ------------\\

	@Test
	public void testValueOf() {
		Degree expected = Degree.valueOf(ScaleDegree.I, ChordType.valueOf(""));
		assertEquals(Degree.valueOf("I"), expected);
		
		expected = Degree.valueOf(ScaleDegree.II, ChordType.valueOf("m7"));
		assertEquals(Degree.valueOf("ii7"), expected);
		
		expected = Degree.valueOf(ScaleDegree.VI, ChordType.valueOf("mM7sus4"));
		assertEquals(Degree.valueOf("viM7sus4"), expected);
	}
}
