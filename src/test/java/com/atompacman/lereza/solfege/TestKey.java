package com.atompacman.lereza.solfege;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.atompacman.lereza.solfege.Accidental;
import com.atompacman.lereza.solfege.Key;
import com.atompacman.lereza.solfege.NoteLetter;
import com.atompacman.lereza.solfege.Scale;
import com.atompacman.lereza.solfege.ScaleType;
import com.atompacman.lereza.solfege.Tone;
import com.atompacman.lereza.solfege.quality.Quality;

public class TestKey {

	//------------ STATIC CONSTRUCTORS ------------\\

	@Test
	public void testValueOf() {
		Key a = Key.valueOf("Bbm");
		Key b = Key.valueOf(Tone.valueOf("Bb"), Quality.MINOR);
		assertEquals(a, b);
		
		a = Key.valueOf("E");
		b = Key.valueOf(Tone.valueOf("E"), Quality.MAJOR);
		assertEquals(a, b);
	}
	
	
	//------------ CORRESPONDING SCALE ------------\\

	@Test
	public void testCorrespondingScale() {
		Key a = Key.valueOf("Cm");
		Scale b = Scale.valueOf(Tone.valueOf("C"), ScaleType.NATURAL_MINOR);
		assertEquals(a.correspondingScale(), b);
	}

	@Test
	public void testContainsTones() {
		Key a = Key.valueOf("Cm");
		List<Tone> tones = new ArrayList<Tone>();
		tones.add(Tone.valueOf("D"));
		tones.add(Tone.valueOf("Eb"));
		assertTrue(a.contains(tones));
		tones.add(Tone.valueOf("E"));
		assertTrue(!a.contains(tones));
	}
	
	
	//------------ ARMOR ------------\\

	@Test
	public void testGetAccidental() {
		Key a = Key.valueOf(Tone.valueOf(NoteLetter.D), Quality.MINOR);
		assertEquals(a.accidental(), Accidental.FLAT);
		assertEquals(a.nbAccidentals(), 1);
	}
	
	
	//------------ PARALLEL KEY ------------\\

	@Test
	public void testParallelKey() {
		Key a = Key.valueOf(Tone.valueOf("E"), Quality.MAJOR);
		Key b = Key.valueOf(Tone.valueOf("C#"), Quality.MINOR);
		assertEquals(a.parallelKey(), b);
		
		a = Key.valueOf(Tone.valueOf("Bb"), Quality.MINOR);
		b = Key.valueOf(Tone.valueOf("Db"), Quality.MAJOR);
		assertEquals(a.parallelKey(), b);
	}

	
	//------------ STRING ------------\\

	@Test
	public void testToString() {
		Key a = Key.valueOf(Tone.valueOf(NoteLetter.A, Accidental.FLAT), Quality.MINOR);
		assertEquals(a.toString(), "Ab minor");
		
		a = Key.valueOf(Tone.valueOf("D"), Quality.MAJOR);
		assertEquals(a.toString(), "D major");
	}
}
