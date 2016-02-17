package com.atompacman.lereza.core.solfege.quality;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.atompacman.lereza.core.theory.AdvancedQuality;
import com.atompacman.lereza.core.theory.Quality;

public class TestQuality {
	
	//------------ GETTERS ------------\\

	@Test
	public void testSemitoneModifiers() {
		Quality major = Quality.MAJOR;
		AdvancedQuality augmented = AdvancedQuality.AUGMENTED;
		
		assertEquals(major.semitoneModifier(), 0.5, 0);
		assertEquals(augmented.semitoneModifier(), 1.0, 0);
	}

	
	//------------ STRING ------------\\

	@Test
	public void testRepresentations() {
		Quality minor = Quality.MINOR;
		AdvancedQuality diminished = AdvancedQuality.DIMINISHED;
		
		assertEquals(minor.toString(), "m");
		assertEquals(diminished, AdvancedQuality.parseQuality("dim"));
	}
}
