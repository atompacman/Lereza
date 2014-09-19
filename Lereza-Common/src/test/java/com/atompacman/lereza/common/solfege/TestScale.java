package com.atompacman.lereza.common.solfege;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.atompacman.lereza.common.solfege.quality.Quality;

public class TestScale {

	//------------ GETTERS ------------\\

	@Test
	public void testGetters() {
		Scale a = new Scale(Tone.valueOf("G"), ScaleType.MAJOR);
		assertTrue(a.getTone().equals(Tone.valueOf("G")));
		assertTrue(a.getType().equals(ScaleType.MAJOR));
		assertTrue(a.getKey().equals(new Key(Tone.valueOf("G"), Quality.MAJOR)));
		assertTrue(a.getQuality().equals(Quality.MAJOR));
		
		a = new Scale(Tone.valueOf("Bb"), ScaleType.HARMONIC_MINOR);
		assertTrue(a.getTone().equals(Tone.valueOf("Bb")));
		assertTrue(a.getType().equals(ScaleType.HARMONIC_MINOR));
		assertTrue(a.getKey().equals(new Key(Tone.valueOf("Bb"), Quality.MINOR)));
		assertTrue(a.getQuality().equals(Quality.MINOR));
	}


	//------------ CONTAINS ------------\\

	@Test
	public void testContainsSingleTone() {
		Scale a = new Scale(Tone.valueOf("A"), ScaleType.MAJOR);
		assertTrue(a.contains(Tone.valueOf("A")));
		assertTrue(a.contains(Tone.valueOf("C#")));
		assertTrue(!a.contains(Tone.valueOf("G")));
		
		a = new Scale(Tone.valueOf("Db"), ScaleType.HARMONIC_MINOR);
		assertTrue(a.contains(Tone.valueOf("C")));
		assertTrue(a.contains(Tone.valueOf("Fb")));
		assertTrue(!a.contains(Tone.valueOf("Cb")));
	}
	
	@Test
	public void testContainsTones() {
		Scale a = new Scale(Tone.valueOf("B"), ScaleType.NATURAL_MINOR);
		List<Tone> tones = new ArrayList<Tone>();
		tones.add(Tone.valueOf("C#"));
		tones.add(Tone.valueOf("D"));
		tones.add(Tone.valueOf("A"));
		assertTrue(a.contains(tones));
		tones.add(Tone.valueOf("B#"));
		assertTrue(!a.contains(tones));
	}
}
