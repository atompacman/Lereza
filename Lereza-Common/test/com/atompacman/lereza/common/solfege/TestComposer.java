package com.atompacman.lereza.common.solfege;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.atompacman.lereza.common.solfege.Composer;

public class TestComposer {

	@Test
	public void testUnicity() {
		assertTrue(Composer.get("goglu").equals(Composer.get("goglu")));
	}
}
