package com.atompacman.lereza.core.solfege;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.atompacman.lereza.core.solfege.Accidental;
import com.atompacman.lereza.core.solfege.Direction;
import com.atompacman.lereza.core.solfege.Interval;
import com.atompacman.lereza.core.solfege.IntervalRange;
import com.atompacman.lereza.core.solfege.NoteLetter;
import com.atompacman.lereza.core.solfege.Tone;

public class TestTone {

	//------------ STATIC CONSTRUCTORS ------------\\

	@Test
	public void testValueOfString() {
		assertEquals(Tone.valueOf("Gb"), Tone.valueOf(NoteLetter.G, Accidental.FLAT));
	}
	
	@Test
	public void testWithSemitoneValueOf() {
		List<Tone> possibilities = Arrays.asList(Tone.valueOf("F#"), Tone.valueOf("Gb"));
		assertEquals(Tone.withSemitoneValueOf(6), possibilities);
	}
	
	@Test
	public void testValueOfSemitoneValueDiatonicToneValue() {
		Tone a = Tone.valueOf("G");
		Tone b = Tone.valueOf(7, 4);
		assertEquals(a, b);
		
		a = Tone.valueOf("E#");
		b = Tone.valueOf(17, -5);
		assertEquals(a, b);
	}
	
	@Test
	public void testThatIsMoreCommonForSemitoneValue() {
		assertEquals(Tone.thatIsMoreCommonForSemitoneValue(4),  Tone.valueOf("E"));
		assertEquals(Tone.thatIsMoreCommonForSemitoneValue(10), Tone.valueOf("Bb"));
		assertEquals(Tone.thatIsMoreCommonForSemitoneValue(1),  Tone.valueOf("C#"));
		assertEquals(Tone.thatIsMoreCommonForSemitoneValue(8),  Tone.valueOf("Ab"));
		assertEquals(Tone.thatIsMoreCommonForSemitoneValue(0),  Tone.valueOf("C"));
	}
	
	@Test
	public void staticConstructorEquivalence() {
		Tone a = Tone.valueOf("B");
		Tone b = Tone.valueOf(NoteLetter.B);
		Tone c = Tone.valueOf(NoteLetter.B, Accidental.NONE);
		Tone d = Tone.valueOf(11, 6);
		Tone e = Tone.thatIsMoreCommonForSemitoneValue(11);
		Tone f = Tone.fromNoteAndSemitoneValue(NoteLetter.B, 11);
		
		assertEquals(a, b);
		assertEquals(b, c);
		assertEquals(c, d);
		assertEquals(d, e);
		assertEquals(e, f);
	}
	
	
	//------------ SWITCH ALTERATION ------------\\

	@Test
	public void testSwitchAlteration() {
		Tone a = Tone.valueOf(NoteLetter.G, Accidental.SHARP);
		a.switchAlteration();
		Tone b = Tone.valueOf(NoteLetter.A, Accidental.FLAT);
		assertEquals("Error switching alteration", a, b);
		
		Tone c = Tone.valueOf(NoteLetter.C, Accidental.FLAT);
		c.switchAlteration();
		Tone d = Tone.valueOf(NoteLetter.B, Accidental.SHARP);
		assertEquals("Error switching alteration", c, d);
	}

	
	//------------ AFTER INTERVAL ------------\\

	@Test
	public void testAfterInterval() {
		Tone a = Tone.valueOf(NoteLetter.C);
		Tone b = a.afterInterval(Interval.valueOf(IntervalRange.FIFTH));
		assertEquals(b, Tone.valueOf(NoteLetter.G));
	
		a = Tone.valueOf(NoteLetter.E, Accidental.FLAT);
		b = a.afterInterval(Interval.valueOf(Quality.MINOR, IntervalRange.THIRD));
		assertEquals(b, Tone.valueOf(NoteLetter.G, Accidental.FLAT));
		
		a = Tone.valueOf(NoteLetter.B, Accidental.FLAT);
		b = a.afterInterval(Interval.valueOf(AdvancedQuality.AUGMENTED, IntervalRange.FIFTH));
		assertEquals(b, Tone.valueOf(NoteLetter.F, Accidental.SHARP));
		b = a.afterInterval(Interval.valueOf(Quality.MINOR, IntervalRange.SIXTH));
		assertEquals(b, Tone.valueOf(NoteLetter.G, Accidental.FLAT));
		
		a = Tone.valueOf(NoteLetter.F);
		Interval intervalA = Interval.valueOf(Direction.DESCENDING, 
				AdvancedQuality.DIMINISHED, IntervalRange.FOURTH);
		Interval intervalB = Interval.valueOf(Direction.ASCENDING, 
				AdvancedQuality.DIMINISHED, IntervalRange.FOURTH);
		assertEquals(a, a.afterInterval(intervalA).afterInterval(intervalB));
	}

	
	//------------ TONE / SEMITONE VALUE ------------\\

	@Test
	public void testSemitoneValue() {
		Tone a = Tone.valueOf(NoteLetter.B, Accidental.FLAT);
		assertEquals(a.semitoneValue(), 10);
	}
	
	
	//------------ STRING ------------\\

	@Test
	public void testToString() {
		Tone a = Tone.valueOf(NoteLetter.D, Accidental.SHARP);
		assertEquals(a.toString(), "D#");
		
		Tone b = Tone.valueOf(NoteLetter.F, Accidental.FLAT);
		assertEquals(b.toString(), "Fb");
		
		Tone c = Tone.valueOf(NoteLetter.B);
		assertEquals(c.toString(), "B");
	}
}
