package com.atompacman.lereza.common.solfege;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.atompacman.lereza.common.solfege.quality.Quality;

public class TestScale {

	@Test
	public void testGetters() {
		Scale a = new Scale(Tone.valueOf("G"), ScaleType.MAJOR);
		assertTrue(a.getTone().equals(Tone.valueOf("G")));
		assertTrue(a.getType().equals(ScaleType.MAJOR));
		assertTrue(a.getKey().equals(new Key(Tone.valueOf("G"), Quality.MAJOR)));
		assertTrue(a.getQuality().equals(Quality.MAJOR));
	}
}
