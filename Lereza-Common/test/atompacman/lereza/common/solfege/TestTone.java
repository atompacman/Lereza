package atompacman.lereza.common.solfege;

import static org.junit.Assert.*;

import org.junit.Test;

import com.atompacman.lereza.common.solfege.Accidental;
import com.atompacman.lereza.common.solfege.Direction;
import com.atompacman.lereza.common.solfege.Interval;
import com.atompacman.lereza.common.solfege.IntervalRange;
import com.atompacman.lereza.common.solfege.NoteLetter;
import com.atompacman.lereza.common.solfege.Tone;
import com.atompacman.lereza.common.solfege.quality.AdvancedQuality;
import com.atompacman.lereza.common.solfege.quality.Quality;

public class TestTone {

	@Test
	public void testToString() {
		Tone a = new Tone(NoteLetter.D, Accidental.SHARP);
		assertTrue(a.toString().equals("D#"));
		
		Tone b = new Tone(NoteLetter.F, Accidental.FLAT);
		assertTrue(b.toString().equals("Fb"));
		
		Tone c = new Tone(NoteLetter.B);
		assertTrue(c.toString().equals("B"));
	}
	
	@Test
	public void testEquality() {
		Tone a = new Tone(NoteLetter.D, Accidental.NONE);
		Tone b = new Tone(NoteLetter.D);
		assertTrue(a.equals(b));
	}
	
	@Test
	public void testSwitchAlteration() {
		Tone a = new Tone(NoteLetter.G, Accidental.SHARP);
		a.switchAlteration();
		Tone b = new Tone(NoteLetter.A, Accidental.FLAT);
		assertTrue("Error switching alteration", a.equals(b));
		
		Tone c = new Tone(NoteLetter.C, Accidental.FLAT);
		c.switchAlteration();
		Tone d = new Tone(NoteLetter.B, Accidental.SHARP);
		assertTrue("Error switching alteration", c.equals(d));
	}
	
	@Test
	public void testNbSemitones() {
		Tone a = new Tone(NoteLetter.B, Accidental.FLAT);
		assertTrue(Tone.fromSemitoneValue(10).equals(a));
		
		assertTrue(a.semitoneValue() == 10);
		
		assertTrue(Tone.fromSemitoneValue(10).semitoneValue() == 10);
	}
	
	@Test
	public void testValueOf() {
		assertTrue(Tone.valueOf("Gb").equals(new Tone(NoteLetter.G, Accidental.FLAT)));
	}
	
	@Test
	public void testAfterInterval() {
		Tone a = new Tone(NoteLetter.C);
		Tone b = a.afterInterval(new Interval(IntervalRange.FIFTH));
		assertTrue("Error getting tone after a fifth", b.equals(new Tone(NoteLetter.G)));
	
		Tone c = new Tone(NoteLetter.E, Accidental.FLAT);
		Tone d = c.afterInterval(new Interval(Quality.MINOR, IntervalRange.THIRD));
		assertTrue("Error getting tone after a minor third", d.equals(new Tone(NoteLetter.G, Accidental.FLAT)));
		
		Tone e = new Tone(NoteLetter.F);
		Interval intervalA = new Interval(Direction.DESCENDING, AdvancedQuality.DIMINISHED, IntervalRange.FOURTH);
		Interval intervalB = new Interval(Direction.ASCENDING, AdvancedQuality.DIMINISHED, IntervalRange.FOURTH);
		assertTrue("Very surprising !", e.equals(e.afterInterval(intervalA).afterInterval(intervalB)));
	}
}
