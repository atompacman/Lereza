package com.atompacman.lereza.common.solfege;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.atompacman.lereza.common.solfege.ChordType;
import com.atompacman.lereza.common.solfege.Interval;
import com.atompacman.lereza.common.solfege.IntervalRange;
import com.atompacman.lereza.common.solfege.quality.AdvancedQuality;
import com.atompacman.lereza.common.solfege.quality.Quality;

public class TestChordType {
	
	//------------ STATIC CONSTRUCTORS ------------\\

	@Test
	public void testFromIntervals() {
		List<Interval> intervals = new ArrayList<Interval>();
		intervals.add(new Interval(				  IntervalRange.UNISON));
		intervals.add(new Interval(Quality.MINOR, IntervalRange.THIRD));
		intervals.add(new Interval(				  IntervalRange.FIFTH));
		ChordType a = ChordType.fromIntervals(intervals);
		
		assertTrue(a.getIntervals().equals(intervals));
		
		Interval expected = new Interval(IntervalRange.FIFTH);
		assertTrue(a.getIntervalForRange(IntervalRange.FIFTH).equals(expected));
	}

	@Test
	public void testValueOf() {
		List<Interval> intervals = new ArrayList<Interval>();
		intervals.add(new Interval(				  IntervalRange.UNISON));
		intervals.add(new Interval(Quality.MINOR, IntervalRange.THIRD));
		intervals.add(new Interval(				  IntervalRange.FIFTH));
		ChordType a = ChordType.valueOf("m");
		assertTrue(a.getIntervals().equals(intervals));
		
		intervals.clear();
		intervals.add(new Interval(				  IntervalRange.UNISON));
		intervals.add(new Interval(Quality.MAJOR, IntervalRange.THIRD));
		intervals.add(new Interval(				  IntervalRange.FIFTH));
		intervals.add(new Interval(Quality.MINOR, IntervalRange.SEVENTH));
		a = ChordType.valueOf("7");
		assertTrue(a.getIntervals().equals(intervals));
			
		intervals.clear();
		intervals.add(new Interval(				  IntervalRange.UNISON));
		intervals.add(new Interval(				  IntervalRange.FOURTH));
		intervals.add(new Interval(				  IntervalRange.FIFTH));
		intervals.add(new Interval(Quality.MINOR, IntervalRange.SEVENTH));
		a = ChordType.valueOf("m7sus4");
		assertTrue(a.getIntervals().equals(intervals));
		
		intervals.clear();
		intervals.add(new Interval(				  			  IntervalRange.UNISON));
		intervals.add(new Interval(Quality.MAJOR, 			  IntervalRange.THIRD));
		intervals.add(new Interval(AdvancedQuality.AUGMENTED, IntervalRange.FIFTH));
		intervals.add(new Interval(Quality.MAJOR, 			  IntervalRange.NINTH));
		a = ChordType.valueOf("augadd9");
		assertTrue(a.getIntervals().equals(intervals));
		
		intervals.clear();
		intervals.add(new Interval(				  IntervalRange.UNISON));
		intervals.add(new Interval(Quality.MINOR, IntervalRange.THIRD));
		intervals.add(new Interval(				  IntervalRange.FIFTH));
		intervals.add(new Interval(Quality.MAJOR, IntervalRange.SEVENTH));
		intervals.add(new Interval(Quality.MAJOR, IntervalRange.NINTH));
		intervals.add(new Interval(				  IntervalRange.ELEVENTH));
		intervals.add(new Interval(Quality.MAJOR, IntervalRange.THIRTEENTH));
		a = ChordType.valueOf("mM7add13");
		assertTrue(a.getIntervals().equals(intervals));
	}

	@Test (expected=IllegalArgumentException.class)
	public void testValueOfBadOrder() {
		List<Interval> intervals = new ArrayList<Interval>();
		intervals.add(new Interval(				  IntervalRange.UNISON));
		intervals.add(new Interval(				  IntervalRange.FOURTH));
		intervals.add(new Interval(				  IntervalRange.FIFTH));
		intervals.add(new Interval(Quality.MINOR, IntervalRange.SEVENTH));
		ChordType.valueOf("sus4M7");
	}
}
