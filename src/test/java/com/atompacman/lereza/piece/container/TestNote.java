package com.atompacman.lereza.piece.container;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.atompacman.lereza.common.solfege.Articulation;
import com.atompacman.lereza.common.solfege.Pitch;
import com.atompacman.lereza.common.solfege.Value;

public class TestNote {

	//------------ TEST CONSTRUCTORS ------------\\

	@Test
	public void guessPitchExactness() {
		assertTrue(Note.guessPitch(21).equals(Pitch.valueOf("A0")));
		assertTrue(Note.guessPitch(43).equals(Pitch.valueOf("G2")));
		assertTrue(Note.guessPitch(114).equals(Pitch.valueOf("F#8")));
	}
	
	@Test
	public void constructorEquivalence() {
		Pitch pitch = Pitch.valueOf("B3");
		Note a = new Note(59, Value.QUARTER);
		Note b = new Note(59, Value.QUARTER, false);
		Note c = new Note(pitch, Value.QUARTER);
		Note d = new Note(pitch, Value.QUARTER, false);
		Note e = new Note(pitch, Value.QUARTER, false, Articulation.NORMAL);

		assertTrue(a.equals(b));
		assertTrue(b.equals(c));
		assertTrue(c.equals(d));
		assertTrue(d.equals(e));
	}
}
