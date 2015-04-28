package com.atompacman.lereza.piece.container;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.atompacman.lereza.solfege.Articulation;
import com.atompacman.lereza.solfege.Pitch;
import com.atompacman.lereza.solfege.Value;

public class TestNote {

	//------------ STATIC CONSTRUCTORS ------------\\

	@Test
	public void staticConstructorEquivalence() {
		Pitch pitch = Pitch.valueOf("B3");
		Note a = Note.valueOf(59, Value.QUARTER);
		Note b = Note.valueOf(59, Value.QUARTER, false);
		Note c = Note.valueOf(pitch, Value.QUARTER);
		Note d = Note.valueOf(pitch, Value.QUARTER, false);
		Note e = Note.valueOf(pitch, Value.QUARTER, false, Articulation.NORMAL);

		assertEquals(a, b);
		assertEquals(b, c);
		assertEquals(c, d);
		assertEquals(d, e);
	}
	
	//------------ SETTERS ------------\\

	@Test
	public void testSetAndConfirmPitchCorrect() {
		Note a = Note.valueOf(68, Value.EIGHTH);
		a.confirmPitch(Pitch.valueOf("G#4"));
		assertTrue(a.pitchConfirmed());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testSetAndConfirmPitchInorrect() {
		Note a = Note.valueOf(44, Value.EIGHTH);
		a.confirmPitch(Pitch.valueOf("Bb0"));
		assertTrue(a.pitchConfirmed());
	}
}
