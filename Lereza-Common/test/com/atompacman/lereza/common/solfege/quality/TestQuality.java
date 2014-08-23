package com.atompacman.lereza.common.solfege.quality;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.atompacman.lereza.common.solfege.quality.AdvancedQuality;
import com.atompacman.lereza.common.solfege.quality.Quality;

public class TestQuality {

	@Test
	public void testRepresentations() {
		Quality minor = Quality.MINOR;
		AdvancedQuality diminished = AdvancedQuality.DIMINISHED;
		
		assertTrue(minor.toString().equals("m"));
		assertTrue(diminished.equals(AdvancedQuality.parseQuality("dim")));
	}
	
	@Test
	public void testSemitoneModifiers() {
		Quality major = Quality.MAJOR;
		AdvancedQuality augmented = AdvancedQuality.AUGMENTED;
		
		assertTrue(major.semitoneModifier() == 0.5);
		assertTrue(augmented.semitoneModifier() == 1.0);
	}
}
