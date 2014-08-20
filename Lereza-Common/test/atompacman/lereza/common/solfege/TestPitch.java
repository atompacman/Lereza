package atompacman.lereza.common.solfege;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.atompacman.lereza.common.solfege.Accidental;
import com.atompacman.lereza.common.solfege.NoteLetter;
import com.atompacman.lereza.common.solfege.Octave;
import com.atompacman.lereza.common.solfege.Pitch;
import com.atompacman.lereza.common.solfege.Tone;

public class TestPitch {

	@Test
	public void testValueOf() {
		Pitch a = Pitch.valueOf("A#5");
		Pitch b = new Pitch(new Tone(NoteLetter.A, Accidental.SHARP), Octave.FIVE);
		assertTrue(a.equals(b));
	}
	
	@Test
	public void testGetSemitoneRank() {
		Pitch a = new Pitch(new Tone(NoteLetter.C, Accidental.SHARP), Octave.TWO);
		assertTrue(a.semitoneValue() == 25);
	}
}
