package com.atompacman.lereza.common.solfege;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.atompacman.lereza.common.solfege.Octave;

public class TestOctave {

	@Test
	public void testFromInt() {
		assertTrue(Octave.FIVE == Octave.fromInt(5));
	}
}