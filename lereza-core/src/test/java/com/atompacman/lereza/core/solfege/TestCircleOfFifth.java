package com.atompacman.lereza.core.solfege;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.atompacman.lereza.core.theory.Accidental;
import com.atompacman.lereza.core.theory.CircleOfFifths;
import com.atompacman.lereza.core.theory.Key;
import com.atompacman.lereza.core.theory.NoteLetter;
import com.atompacman.lereza.core.theory.Quality;
import com.atompacman.lereza.core.theory.Tone;

public class TestCircleOfFifth {

	//------------ KEY ARMOR ------------\\

	@Test
	public void testAccidentalOfKey() {
		Key key = Key.valueOf(Tone.valueOf("Bb"));
		assertEquals(CircleOfFifths.accidentalOfKey(key), Accidental.FLAT);
		
		key = Key.of(Tone.valueOf("E"), Quality.MINOR);
		assertEquals(CircleOfFifths.accidentalOfKey(key), Accidental.SHARP);
		
		key = Key.of(Tone.valueOf("A"), Quality.MINOR);
		assertEquals(CircleOfFifths.accidentalOfKey(key), Accidental.NONE);
	}
	
	@Test
	public void testNbAccidentalOfKey() {
		Key key = Key.valueOf(Tone.valueOf("Bb"));
		assertEquals(CircleOfFifths.nbAccidentalsOfKey(key), 2);
		
		key = Key.of(Tone.valueOf("E"), Quality.MINOR);
		assertEquals(CircleOfFifths.nbAccidentalsOfKey(key), 1);
		
		key = Key.of(Tone.valueOf("A"), Quality.MINOR);
		assertEquals(CircleOfFifths.nbAccidentalsOfKey(key), 0);
	}
	
	
	//------------ POSITION IN ORDERS ------------\\

	@Test
	public void testPositionInOrders() {
		assertEquals(CircleOfFifths.positionInOrderOfSharps(Tone.valueOf("G#")), 2);
		assertEquals(CircleOfFifths.positionInOrderOfSharps(Tone.valueOf("B#")), 6);
		assertEquals(CircleOfFifths.positionInOrderOfFlats(Tone.valueOf("Eb")), 1);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testPositionInOrdersParamVerif1() {
		assertEquals(CircleOfFifths.positionInOrderOfSharps(Tone.valueOf("Bb")), 2);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testPositionInOrdersParamVerif2() {
		assertEquals(CircleOfFifths.positionInOrderOfFlats(Tone.valueOf("B")), 2);
	}
	
	
	//------------ TONE AT POSITION ------------\\

	@Test
	public void testToneAtPosition() {
		Tone a = Tone.valueOf(NoteLetter.A);
		assertEquals(CircleOfFifths.toneAtPosition(3), a);
		
		a = Tone.valueOf(NoteLetter.A, Accidental.FLAT);
		assertEquals(CircleOfFifths.toneAtPosition(-4), a);
		
		a = Tone.valueOf(NoteLetter.A, Accidental.SHARP);
		assertEquals(CircleOfFifths.toneAtPosition(10), a);
	}
	
	//------------ PRIVATE UTILS ------------\\

	@Test
	public void testBasicPositionInOrderOfSharps() {
		assertEquals(CircleOfFifths.basicPositionInOrderOfSharps(NoteLetter.B), 6);
		assertEquals(CircleOfFifths.basicPositionInOrderOfSharps(NoteLetter.F), 0);
		assertEquals(CircleOfFifths.basicPositionInOrderOfSharps(NoteLetter.C), 1);
	}
}