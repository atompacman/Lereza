package com.atompacman.lereza.core.solfege;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.atompacman.lereza.core.theory.AdvancedQuality;
import com.atompacman.lereza.core.theory.Direction;
import com.atompacman.lereza.core.theory.Interval;
import com.atompacman.lereza.core.theory.IntervalRange;
import com.atompacman.lereza.core.theory.Pitch;
import com.atompacman.lereza.core.theory.Quality;
import com.atompacman.lereza.core.theory.Tone;

public class TestInterval {

	//------------ STATIC CONSTRUCTORS ------------\\

	@Test (expected=IllegalArgumentException.class)
	public void testQualityTypeVerification() {
		Interval.of(Quality.MAJOR, IntervalRange.FIFTH);
	}

	@Test (expected=IllegalArgumentException.class)
	public void testUnisonDirectionVerification() {
		Interval.valueOf(Direction.ASCENDING, Quality.MAJOR, IntervalRange.UNISON);
	}

	@Test
	public void testWithSemitoneValue() {
		List<Interval> a = Interval.withSemitoneValue(10);
		assertEquals(a.size(), 1);
		assertEquals(a.get(0), Interval.of(Quality.MINOR, IntervalRange.SEVENTH));
		
		a = Interval.withSemitoneValue(6);
		assertEquals(a.size(), 2);
		assertEquals(a.get(0), Interval.of(AdvancedQuality.AUGMENTED, IntervalRange.FOURTH));
		assertEquals(a.get(1), Interval.of(AdvancedQuality.DIMINISHED, IntervalRange.FIFTH));
		
		a = Interval.withSemitoneValue(12);
		assertEquals(a.size(), 1);
		assertEquals(a.get(0), Interval.valueOf(IntervalRange.OCTAVE));
		
		a = Interval.withSemitoneValue(-14);
		assertEquals(a.size(), 1);
		assertEquals(a.get(0), Interval.valueOf(Direction.DESCENDING, Quality.MAJOR, IntervalRange.NINTH));
	}

	
	//------------ TONE / SEMITONE VALUE ------------\\
	
	@Test
	public void testSemitoneValue() {
		Interval a = Interval.valueOf(Direction.ASCENDING, Quality.MAJOR, IntervalRange.SEVENTH);
		assertEquals(a.semitoneValue(), 11);
		
		a = Interval.valueOf(Direction.DESCENDING, AdvancedQuality.AUGMENTED, IntervalRange.ELEVENTH);
		assertEquals(a.semitoneValue(), -18);
	}

	@Test
	public void testDiatonicToneValue() {
		Interval a = Interval.valueOf(Direction.DESCENDING, Quality.MINOR, IntervalRange.SIXTH);
		assertEquals(a.diatonicToneValue(), -5);
	}
	
	
	//------------ BETWEEN ------------\\

	@Test
	public void testBetweenPitches() {
		Pitch a = Pitch.valueOf("A5");
		Pitch b = Pitch.valueOf("B5");
		Interval expected = Interval.of(Quality.MAJOR, IntervalRange.SECOND);
		assertEquals(Interval.between(a, b), expected);
		
		a = Pitch.valueOf("Gb3");
		b = Pitch.valueOf("D4");
		expected = Interval.of(AdvancedQuality.AUGMENTED, IntervalRange.FIFTH);
		assertEquals(Interval.between(a, b), expected);
		
		a = Pitch.valueOf("F#6");
		b = Pitch.valueOf("Bb4");
		expected = Interval.valueOf(Direction.DESCENDING, 
				AdvancedQuality.AUGMENTED, IntervalRange.TWELVTH);
		assertEquals(Interval.between(a, b), expected);
	}
	
	@Test
	public void testBetweenTones() {
		Tone a = Tone.valueOf("A");
		Tone b = Tone.valueOf("C#");
		Interval expected = Interval.valueOf(Direction.ASCENDING, 
				Quality.MAJOR, IntervalRange.THIRD);
		assertEquals(Interval.between(a, Direction.ASCENDING, b), expected);
		expected = Interval.valueOf(Direction.DESCENDING, Quality.MINOR, IntervalRange.SIXTH);
		assertEquals(Interval.between(a, Direction.DESCENDING, b), expected);

		a = Tone.valueOf("D");
		b = Tone.valueOf("A#");
		expected = Interval.of(AdvancedQuality.AUGMENTED, IntervalRange.FIFTH);
		assertEquals(Interval.between(a, Direction.ASCENDING, b), expected);
		expected = Interval.valueOf(Direction.DESCENDING, 
				AdvancedQuality.DIMINISHED, IntervalRange.FOURTH);
		assertEquals(Interval.between(a, Direction.DESCENDING, b), expected);
	}
}
