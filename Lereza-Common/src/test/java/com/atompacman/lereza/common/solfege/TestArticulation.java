package com.atompacman.lereza.common.solfege;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.atompacman.lereza.common.solfege.Articulation;

public class TestArticulation {

	//------------ STATIC CONSTRUCTORS ------------\\

	@Test
	public void testParseArticulation() {
		assertTrue(Articulation.parseArticulation("·") == Articulation.STACCATO);
	}
}
