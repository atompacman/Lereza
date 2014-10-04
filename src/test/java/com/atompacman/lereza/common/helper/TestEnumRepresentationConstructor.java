package com.atompacman.lereza.common.helper;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.atompacman.lereza.common.helper.EnumRepresConstructor;
import com.atompacman.lereza.common.solfege.NoteLetter;
import com.atompacman.lereza.common.solfege.Octave;

public class TestEnumRepresentationConstructor {

	public static class DummyTone {
		public NoteLetter letter;
		public Octave octave;

		
		public DummyTone(NoteLetter letter, Octave octave) {
			this.letter = letter;
			this.octave = octave;
		}
		
		public static DummyTone valueOf(String repres) {
			EnumRepresConstructor<DummyTone> a = new EnumRepresConstructor<DummyTone>(DummyTone.class);
			return a.newInstance(repres);
		}
		
		public boolean equals(Object o) {
			return letter == ((DummyTone)o).letter && octave == ((DummyTone)o).octave;
		}
	}

	@Test
	public void testERC() {
		DummyTone a = DummyTone.valueOf("A5");
		DummyTone b = new DummyTone(NoteLetter.A, Octave.FIVE);
		assertTrue(a.equals(b));
	}
}
