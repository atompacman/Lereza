package atompacman.lereza.common.solfege;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.atompacman.lereza.common.solfege.Accidental;
import com.atompacman.lereza.common.solfege.Key;
import com.atompacman.lereza.common.solfege.NoteLetter;
import com.atompacman.lereza.common.solfege.Tone;
import com.atompacman.lereza.common.solfege.quality.Quality;

public class TestKey {

	@Test
	public void testToString() {
		Key a = new Key(new Tone(NoteLetter.A, Accidental.FLAT), Quality.MINOR);
		assertTrue(a.toString().equals("Ab minor"));
		
		a = new Key(Tone.valueOf("D"), Quality.MAJOR);
		assertTrue(a.toString().equals("D major"));
	}
	
	@Test
	public void testGetAccidental() {
		Key a = new Key(new Tone(NoteLetter.D), Quality.MINOR);
		assertTrue(a.accidental() == Accidental.FLAT);
		assertTrue(a.nbAccidentals() == 1);
	}
	
	@Test
	public void testParallelKey() {
		Key a = new Key(Tone.valueOf("E"), Quality.MAJOR);
		Key b = new Key(Tone.valueOf("C#"), Quality.MINOR);
		assertTrue(a.parallelKey().equals(b));
		
		a = new Key(Tone.valueOf("Bb"), Quality.MINOR);
		b = new Key(Tone.valueOf("Db"), Quality.MAJOR);
		assertTrue(a.parallelKey().equals(b));
	}
	
	@Test
	public void tooooodooooooooooooooo() {
		int todo;
		//todo testCorrespondingScale and contains
	}
}
