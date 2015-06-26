package com.atompacman.lereza.core.solfege;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.atompacman.lereza.core.solfege.Key;
import com.atompacman.lereza.core.solfege.Scale;
import com.atompacman.lereza.core.solfege.ScaleType;
import com.atompacman.lereza.core.solfege.Tone;

public class TestScale {

	//------------ GETTERS ------------\\

	@Test
	public void testGetters() {
		Scale a = Scale.valueOf(Tone.valueOf("G"), ScaleType.MAJOR);
		assertEquals(a.getTone(), Tone.valueOf("G"));
		assertEquals(a.getType(), ScaleType.MAJOR);
		assertEquals(a.getKey(), Key.valueOf(Tone.valueOf("G"), Quality.MAJOR));
		assertEquals(a.getQuality(), Quality.MAJOR);
		
		a = Scale.valueOf(Tone.valueOf("Bb"), ScaleType.HARMONIC_MINOR);
		assertEquals(a.getTone(), Tone.valueOf("Bb"));
		assertEquals(a.getType(), ScaleType.HARMONIC_MINOR);
		assertEquals(a.getKey(), Key.valueOf(Tone.valueOf("Bb"), Quality.MINOR));
		assertEquals(a.getQuality(), Quality.MINOR);
	}


	//------------ CONTAINS ------------\\

	@Test
	public void testContainsSingleTone() {
		Scale a = Scale.valueOf(Tone.valueOf("A"), ScaleType.MAJOR);
		assertTrue(a.contains(Tone.valueOf("A")));
		assertTrue(a.contains(Tone.valueOf("C#")));
		assertTrue(!a.contains(Tone.valueOf("G")));
		
		a = Scale.valueOf(Tone.valueOf("Db"), ScaleType.HARMONIC_MINOR);
		assertTrue(a.contains(Tone.valueOf("C")));
		assertTrue(a.contains(Tone.valueOf("Fb")));
		assertTrue(!a.contains(Tone.valueOf("Cb")));
	}
	
	@Test
	public void testContainsTones() {
		Scale a = Scale.valueOf(Tone.valueOf("B"), ScaleType.NATURAL_MINOR);
		List<Tone> tones = new ArrayList<Tone>();
		tones.add(Tone.valueOf("C#"));
		tones.add(Tone.valueOf("D"));
		tones.add(Tone.valueOf("A"));
		assertTrue(a.contains(tones));
		tones.add(Tone.valueOf("B#"));
		assertTrue(!a.contains(tones));
	}
}
