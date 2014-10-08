package com.atompacman.lereza.common.solfege;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.atompacman.lereza.common.solfege.Accidental;
import com.atompacman.lereza.common.solfege.CircleOfFifths;
import com.atompacman.lereza.common.solfege.Key;
import com.atompacman.lereza.common.solfege.Tone;
import com.atompacman.lereza.common.solfege.quality.Quality;

public class TestCircleOfFifth {

	//------------ KEY ARMOR ------------\\

	@Test
	public void testAccidentalOfKey() {
		Key key = new Key(Tone.valueOf("Bb"));
		assertTrue(CircleOfFifths.accidentalOfKey(key) == Accidental.FLAT);
		
		key = new Key(Tone.valueOf("E"), Quality.MINOR);
		assertTrue(CircleOfFifths.accidentalOfKey(key) == Accidental.SHARP);
		
		key = new Key(Tone.valueOf("A"), Quality.MINOR);
		assertTrue(CircleOfFifths.accidentalOfKey(key) == Accidental.NONE);
	}
	
	@Test
	public void testNbAccidentalOfKey() {
		Key key = new Key(Tone.valueOf("Bb"));
		assertTrue(CircleOfFifths.nbAccidentalsOfKey(key) == 2);
		
		key = new Key(Tone.valueOf("E"), Quality.MINOR);
		assertTrue(CircleOfFifths.nbAccidentalsOfKey(key) == 1);
		
		key = new Key(Tone.valueOf("A"), Quality.MINOR);
		assertTrue(CircleOfFifths.nbAccidentalsOfKey(key) == 0);
	}
	
	
	//------------ POSITION IN ORDERS ------------\\

	@Test
	public void testPositionInOrders() {
		assertTrue(CircleOfFifths.positionInOrderOfSharps(Tone.valueOf("G#")) == 2);
		assertTrue(CircleOfFifths.positionInOrderOfSharps(Tone.valueOf("B#")) == 6);
		assertTrue(CircleOfFifths.positionInOrderOfFlats(Tone.valueOf("Eb")) == 1);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testPositionInOrdersParamVerif() {
		assertTrue(CircleOfFifths.positionInOrderOfSharps(Tone.valueOf("Bb")) == 2);
	}
}
