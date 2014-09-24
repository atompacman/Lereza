package com.atompacman.lereza.common.solfege;

import static org.junit.Assert.*;

import java.util.Arrays;

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

	//------------ STATIC CONSTRUCTORS ------------\\

	@Test
	public void testValueOf() {
		assertTrue(Tone.valueOf("Gb").equals(new Tone(NoteLetter.G, Accidental.FLAT)));
	}
	
	@Test
	public void testThatIsMoreCommonForSemitoneValue() {
		assertTrue(Tone.thatIsMoreCommonForSemitoneValue(4).equals(Tone.valueOf("E")));
		assertTrue(Tone.thatIsMoreCommonForSemitoneValue(10).equals(Tone.valueOf("Bb")));
		assertTrue(Tone.thatIsMoreCommonForSemitoneValue(1).equals(Tone.valueOf("Db")));
		assertTrue(Tone.thatIsMoreCommonForSemitoneValue(8).equals(Tone.valueOf("Ab")));
		assertTrue(Tone.thatIsMoreCommonForSemitoneValue(0).equals(Tone.valueOf("C")));
	}
	
	@Test
	public void testFromSemitoneValue() {
		Tone a = Tone.valueOf("G");
		Tone b = Tone.fromSemitoneValue(7, 4);
		assertTrue(a.equals(b));
		
		a = Tone.valueOf("E#");
		b = Tone.fromSemitoneValue(17, -5);
		assertTrue(a.equals(b));
		
		assertTrue(Tone.fromSemitoneValue(6).equals(Arrays.asList(Tone.valueOf("F#"), Tone.valueOf("Gb"))));
	}
	
	
	//------------ SWITCH ALTERATION ------------\\

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

	
	//------------ AFTER INTERVAL ------------\\

	@Test
	public void testAfterInterval() {
		Tone a = new Tone(NoteLetter.C);
		Tone b = a.afterInterval(new Interval(IntervalRange.FIFTH));
		assertTrue(b.equals(new Tone(NoteLetter.G)));
	
		a = new Tone(NoteLetter.E, Accidental.FLAT);
		b = a.afterInterval(new Interval(Quality.MINOR, IntervalRange.THIRD));
		assertTrue(b.equals(new Tone(NoteLetter.G, Accidental.FLAT)));
		
		a = new Tone(NoteLetter.B, Accidental.FLAT);
		b = a.afterInterval(new Interval(AdvancedQuality.AUGMENTED, IntervalRange.FIFTH));
		assertTrue(b.equals(new Tone(NoteLetter.F, Accidental.SHARP)));
		b = a.afterInterval(new Interval(Quality.MINOR, IntervalRange.SIXTH));
		assertTrue(b.equals(new Tone(NoteLetter.G, Accidental.FLAT)));
		
		a = new Tone(NoteLetter.F);
		Interval intervalA = new Interval(Direction.DESCENDING, AdvancedQuality.DIMINISHED, IntervalRange.FOURTH);
		Interval intervalB = new Interval(Direction.ASCENDING, AdvancedQuality.DIMINISHED, IntervalRange.FOURTH);
		assertTrue(a.equals(a.afterInterval(intervalA).afterInterval(intervalB)));
	}

	
	//------------ TONE / SEMITONE ------------\\

	@Test
	public void testSemitoneValue() {
		Tone a = new Tone(NoteLetter.B, Accidental.FLAT);
		assertTrue(a.semitoneValue() == 10);
	}
	
	
	//------------ STRING ------------\\

	@Test
	public void testToString() {
		Tone a = new Tone(NoteLetter.D, Accidental.SHARP);
		assertTrue(a.toString().equals("D#"));
		
		Tone b = new Tone(NoteLetter.F, Accidental.FLAT);
		assertTrue(b.toString().equals("Fb"));
		
		Tone c = new Tone(NoteLetter.B);
		assertTrue(c.toString().equals("B"));
	}

	
	//------------ EQUALITIES ------------\\
	
	@Test
	public void testEquality() {
		Tone a = new Tone(NoteLetter.D, Accidental.NONE);
		Tone b = new Tone(NoteLetter.D);
		assertTrue(a.equals(b));
	}
}
