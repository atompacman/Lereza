package com.atompacman.lereza.solfege;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.atompacman.lereza.solfege.Accidental;
import com.atompacman.lereza.solfege.NoteLetter;
import com.atompacman.lereza.solfege.Octave;
import com.atompacman.lereza.solfege.Pitch;
import com.atompacman.lereza.solfege.Tone;

public class TestPitch {

	//------------ STATIC CONSTRUCTORS ------------\\

	@Test
	public void testPitchThatIsMoreCommonForHexValue() {
		assertEquals(Pitch.thatIsMoreCommonForHexValue(21), Pitch.valueOf("A0"));
		assertEquals(Pitch.thatIsMoreCommonForHexValue(43), Pitch.valueOf("G2"));
		assertEquals(Pitch.thatIsMoreCommonForHexValue(114), Pitch.valueOf("F#8"));
	}
	
	@Test
	public void staticConstructorsEquivalence() {
		Pitch a = Pitch.valueOf(Tone.valueOf("Bb"), Octave.FIVE);
		Pitch b = Pitch.valueOf(NoteLetter.B, Accidental.FLAT, Octave.FIVE);
		Pitch c = Pitch.valueOf("Bb5");
		Pitch d = Pitch.thatIsMoreCommonForHexValue(82);
		assertEquals(a, b);
		assertEquals(b, c);
		assertEquals(c, d);
	}

	
	//------------ TONE / SEMITONE VALUE ------------\\

	@Test
	public void testSemitoneValue() {
		assertEquals(Pitch.valueOf("C#2").semitoneValue(), 25);
	}
}
