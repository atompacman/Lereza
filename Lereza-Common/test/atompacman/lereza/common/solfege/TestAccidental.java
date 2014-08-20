package atompacman.lereza.common.solfege;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.atompacman.lereza.common.solfege.Accidental;

public class TestAccidental {

	@Test
	public void testSemitoneAlteration() {
		assertTrue(Accidental.FLAT.semitoneAlteration() == -1);
	}
}
