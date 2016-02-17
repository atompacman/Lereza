package com.atompacman.lereza.core.solfege;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.atompacman.lereza.core.theory.AdvancedQuality;
import com.atompacman.lereza.core.theory.ChordType;
import com.atompacman.lereza.core.theory.Interval;
import com.atompacman.lereza.core.theory.IntervalRange;
import com.atompacman.lereza.core.theory.Quality;

public class TestChordType {
	
	//------------ STATIC CONSTRUCTORS ------------\\

	@Test
	public void testValueOfIntervals() {
		List<Interval> intervals = new ArrayList<Interval>();
		intervals.add(Interval.valueOf(				  IntervalRange.UNISON));
		intervals.add(Interval.of(Quality.MINOR, IntervalRange.THIRD));
		intervals.add(Interval.valueOf(				  IntervalRange.FIFTH));
		ChordType a = ChordType.of(intervals);
		
		assertEquals(a.getIntervals(), intervals);
		
		Interval expected = Interval.valueOf(IntervalRange.FIFTH);
		assertEquals(a.getIntervalForRange(IntervalRange.FIFTH), expected);
	}

	@Test
	public void testValueOfRepresentation() {
		List<Interval> intervals = new ArrayList<Interval>();
		intervals.add(Interval.valueOf(				  IntervalRange.UNISON));
		intervals.add(Interval.of(Quality.MINOR, IntervalRange.THIRD));
		intervals.add(Interval.valueOf(				  IntervalRange.FIFTH));
		ChordType a = ChordType.of("m");
		assertEquals(a.getIntervals(), intervals);
		
		intervals.clear();
		intervals.add(Interval.valueOf(				  IntervalRange.UNISON));
		intervals.add(Interval.of(Quality.MAJOR, IntervalRange.THIRD));
		intervals.add(Interval.valueOf(				  IntervalRange.FIFTH));
		intervals.add(Interval.of(Quality.MINOR, IntervalRange.SEVENTH));
		a = ChordType.of("7");
		assertEquals(a.getIntervals(), intervals);
			
		intervals.clear();
		intervals.add(Interval.valueOf(				  IntervalRange.UNISON));
		intervals.add(Interval.valueOf(				  IntervalRange.FOURTH));
		intervals.add(Interval.valueOf(				  IntervalRange.FIFTH));
		intervals.add(Interval.of(Quality.MINOR, IntervalRange.SEVENTH));
		a = ChordType.of("m7sus4");
		assertEquals(a.getIntervals(), intervals);
		
		intervals.clear();
		intervals.add(Interval.valueOf(				  			  IntervalRange.UNISON));
		intervals.add(Interval.of(Quality.MAJOR, 			  IntervalRange.THIRD));
		intervals.add(Interval.of(AdvancedQuality.AUGMENTED, IntervalRange.FIFTH));
		intervals.add(Interval.of(Quality.MAJOR, 			  IntervalRange.NINTH));
		a = ChordType.of("augadd9");
		assertEquals(a.getIntervals(), intervals);
		
		intervals.clear();
		intervals.add(Interval.valueOf(				  IntervalRange.UNISON));
		intervals.add(Interval.of(Quality.MINOR, IntervalRange.THIRD));
		intervals.add(Interval.valueOf(				  IntervalRange.FIFTH));
		intervals.add(Interval.of(Quality.MAJOR, IntervalRange.SEVENTH));
		intervals.add(Interval.of(Quality.MAJOR, IntervalRange.NINTH));
		intervals.add(Interval.valueOf(				  IntervalRange.ELEVENTH));
		intervals.add(Interval.of(Quality.MAJOR, IntervalRange.THIRTEENTH));
		a = ChordType.of("mM7add13");
		assertEquals(a.getIntervals(), intervals);
	}

	@Test (expected=IllegalArgumentException.class)
	public void testValueOfBadOrder() {
		List<Interval> intervals = new ArrayList<Interval>();
		intervals.add(Interval.valueOf(				  IntervalRange.UNISON));
		intervals.add(Interval.valueOf(				  IntervalRange.FOURTH));
		intervals.add(Interval.valueOf(				  IntervalRange.FIFTH));
		intervals.add(Interval.of(Quality.MINOR, IntervalRange.SEVENTH));
		ChordType.of("sus4M7");
	}
}
