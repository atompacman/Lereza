package com.atompacman.lereza.core.solfege;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.atompacman.lereza.core.solfege.Articulation;

public class TestArticulation {

	//------------ STATIC CONSTRUCTORS ------------\\

	@Test
	public void testParseArticulation() {
		assertEquals(Articulation.parseArticulation("·"), Articulation.STACCATO);
	}
}
