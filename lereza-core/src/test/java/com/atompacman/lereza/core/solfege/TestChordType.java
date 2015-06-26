package com.atompacman.lereza.core.solfege;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.atompacman.lereza.core.solfege.ChordType;
import com.atompacman.lereza.core.solfege.Interval;
import com.atompacman.lereza.core.solfege.IntervalRange;

public class TestChordType {
	
	//------------ STATIC CONSTRUCTORS ------------\\

	@Test
	public void testValueOfIntervals() {
		List<Interval> intervals = new ArrayList<Interval>();
		intervals.add(Interval.valueOf(				  IntervalRange.UNISON));
		intervals.add(Interval.valueOf(Quality.MINOR, IntervalRange.THIRD));
		intervals.add(Interval.valueOf(				  IntervalRange.FIFTH));
		ChordType a = ChordType.valueOf(intervals);
		
		assertEquals(a.getIntervals(), intervals);
		
		Interval expected = Interval.valueOf(IntervalRange.FIFTH);
		assertEquals(a.getIntervalForRange(IntervalRange.FIFTH), expected);
	}

	@Test
	public void testValueOfRepresentation() {
		List<Interval> intervals = new ArrayList<Interval>();
		intervals.add(Interval.valueOf(				  IntervalRange.UNISON));
		intervals.add(Interval.valueOf(Quality.MINOR, IntervalRange.THIRD));
		intervals.add(Interval.valueOf(				  IntervalRange.FIFTH));
		ChordType a = ChordType.valueOf("m");
		assertEquals(a.getIntervals(), intervals);
		
		intervals.clear();
		intervals.add(Interval.valueOf(				  IntervalRange.UNISON));
		intervals.add(Interval.valueOf(Quality.MAJOR, IntervalRange.THIRD));
		intervals.add(Interval.valueOf(				  IntervalRange.FIFTH));
		intervals.add(Interval.valueOf(Quality.MINOR, IntervalRange.SEVENTH));
		a = ChordType.valueOf("7");
		assertEquals(a.getIntervals(), intervals);
			
		intervals.clear();
		intervals.add(Interval.valueOf(				  IntervalRange.UNISON));
		intervals.add(Interval.valueOf(				  IntervalRange.FOURTH));
		intervals.add(Interval.valueOf(				  IntervalRange.FIFTH));
		intervals.add(Interval.valueOf(Quality.MINOR, IntervalRange.SEVENTH));
		a = ChordType.valueOf("m7sus4");
		assertEquals(a.getIntervals(), intervals);
		
		intervals.clear();
		intervals.add(Interval.valueOf(				  			  IntervalRange.UNISON));
		intervals.add(Interval.valueOf(Quality.MAJOR, 			  IntervalRange.THIRD));
		intervals.add(Interval.valueOf(AdvancedQuality.AUGMENTED, IntervalRange.FIFTH));
		intervals.add(Interval.valueOf(Quality.MAJOR, 			  IntervalRange.NINTH));
		a = ChordType.valueOf("augadd9");
		assertEquals(a.getIntervals(), intervals);
		
		intervals.clear();
		intervals.add(Interval.valueOf(				  IntervalRange.UNISON));
		intervals.add(Interval.valueOf(Quality.MINOR, IntervalRange.THIRD));
		intervals.add(Interval.valueOf(				  IntervalRange.FIFTH));
		intervals.add(Interval.valueOf(Quality.MAJOR, IntervalRange.SEVENTH));
		intervals.add(Interval.valueOf(Quality.MAJOR, IntervalRange.NINTH));
		intervals.add(Interval.valueOf(				  IntervalRange.ELEVENTH));
		intervals.add(Interval.valueOf(Quality.MAJOR, IntervalRange.THIRTEENTH));
		a = ChordType.valueOf("mM7add13");
		assertEquals(a.getIntervals(), intervals);
	}

	@Test (expected=IllegalArgumentException.class)
	public void testValueOfBadOrder() {
		List<Interval> intervals = new ArrayList<Interval>();
		intervals.add(Interval.valueOf(				  IntervalRange.UNISON));
		intervals.add(Interval.valueOf(				  IntervalRange.FOURTH));
		intervals.add(Interval.valueOf(				  IntervalRange.FIFTH));
		intervals.add(Interval.valueOf(Quality.MINOR, IntervalRange.SEVENTH));
		ChordType.valueOf("sus4M7");
	}
}
