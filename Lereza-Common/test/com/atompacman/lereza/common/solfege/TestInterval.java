package com.atompacman.lereza.common.solfege;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.atompacman.lereza.common.solfege.Direction;
import com.atompacman.lereza.common.solfege.Interval;
import com.atompacman.lereza.common.solfege.IntervalRange;
import com.atompacman.lereza.common.solfege.quality.AdvancedQuality;
import com.atompacman.lereza.common.solfege.quality.Quality;

public class TestInterval {

	//------------ CONSTRUCTORS ------------\\

	@Test (expected=IllegalArgumentException.class)
	public void testQualityTypeVerification() {
		new Interval(Quality.MAJOR, IntervalRange.FIFTH);
	}

	@Test (expected=IllegalArgumentException.class)
	public void testUnisonDirectionVerification() {
		new Interval(Direction.ASCENDING, Quality.MAJOR, IntervalRange.UNISON);
	}

	
	//------------ STATIC CONSTRUCTORS ------------\\

	@Test
	public void testFromSemitoneValue() {
		List<Interval> a = Interval.fromSemitoneValue(10);
		assertTrue(a.size() == 1);
		assertTrue(a.get(0).equals(new Interval(Quality.MINOR, IntervalRange.SEVENTH)));
		
		a = Interval.fromSemitoneValue(6);
		assertTrue(a.size() == 2);
		assertTrue(a.get(0).equals(new Interval(AdvancedQuality.AUGMENTED, IntervalRange.FOURTH)));
		assertTrue(a.get(1).equals(new Interval(AdvancedQuality.DIMINISHED, IntervalRange.FIFTH)));
		
		a = Interval.fromSemitoneValue(12);
		assertTrue(a.size() == 1);
		assertTrue(a.get(0).equals(new Interval(IntervalRange.OCTAVE)));
		
		a = Interval.fromSemitoneValue(-14);
		assertTrue(a.size() == 1);
		assertTrue(a.get(0).equals(new Interval(Direction.DESCENDING, Quality.MAJOR, IntervalRange.NINTH)));
	}

	
	//------------ TONE /  SEMITONE ------------\\
	
	@Test
	public void testSemitoneValue() {
		Interval a = new Interval(Direction.ASCENDING, Quality.MAJOR, IntervalRange.SEVENTH);
		assertTrue(a.semitoneValue() == 11);
		
		a = new Interval(Direction.DESCENDING, AdvancedQuality.AUGMENTED, IntervalRange.ELEVENTH);
		assertTrue(a.semitoneValue() == -18);
	}

	@Test
	public void testDiatonicToneValue() {
		Interval a = new Interval(Direction.DESCENDING, Quality.MINOR, IntervalRange.SIXTH);
		assertTrue(a.diatonicToneValue() == -5);
	}
	
	
	//------------ BETWEEN ------------\\

}
