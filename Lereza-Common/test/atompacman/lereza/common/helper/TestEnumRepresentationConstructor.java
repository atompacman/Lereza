package atompacman.lereza.common.helper;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.atompacman.lereza.common.helper.EnumRepresConstructor;
import com.atompacman.lereza.common.solfege.NoteLetter;
import com.atompacman.lereza.common.solfege.Octave;

public class TestEnumRepresentationConstructor {

	public static class Tone {
		public NoteLetter letter;
		public Octave octave;

		
		public Tone(NoteLetter letter, Octave octave) {
			this.letter = letter;
			this.octave = octave;
		}
		
		public static Tone valueOf(String repres) {
			EnumRepresConstructor<Tone> a = new EnumRepresConstructor<Tone>(Tone.class);
			return a.newInstance(repres);
		}
		
		public boolean equals(Object o) {
			return letter == ((Tone)o).letter && octave == ((Tone)o).octave;
		}
	}

	@Test
	public void testERC() {
		Tone a = Tone.valueOf("A5");
		Tone b = new Tone(NoteLetter.A, Octave.FIVE);
		assertTrue(a.equals(b));
	}
}
