package com.atompacman.lereza.common.solfege;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.atompacman.lereza.common.solfege.quality.AdvancedQuality;
import com.atompacman.lereza.common.solfege.quality.Quality;

public class TestIntervalRange {

	//------------ CONSTRUCTORS ------------\\

	@Test
	public void testConstructor() {
		assertTrue(IntervalRange.FOURTH.getQualityType() == AdvancedQuality.class);
		assertTrue(IntervalRange.THIRTEENTH.getQualityType() == Quality.class);
	}
	
	
	//------------ STATIC CONSTRUCTORS ------------\\

	@Test
	public void testClosertRangesFrom() {
		List<IntervalRange> closestRanges = IntervalRange.closestRangesFrom(5);
		assertTrue(closestRanges.size() == 1);
		assertTrue(closestRanges.get(0) == IntervalRange.FOURTH);
		
		closestRanges = IntervalRange.closestRangesFrom(6);
		assertTrue(closestRanges.size() == 2);
		assertTrue(closestRanges.get(0) == IntervalRange.FOURTH);
		assertTrue(closestRanges.get(1) == IntervalRange.FIFTH);
		
		closestRanges = IntervalRange.closestRangesFrom(23);
		assertTrue(closestRanges.size() == 2);
		assertTrue(closestRanges.get(0) == IntervalRange.FOURTEENTH);
		assertTrue(closestRanges.get(1) == IntervalRange.DOUBLE_OCTAVE);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testPositiveVerification() {
		IntervalRange.closestRangesFrom(-4);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testMaximumValueVerification() {
		IntervalRange.closestRangesFrom(44);
	}
	
	
	//------------ TONE / SEMITONE ------------\\

	@Test
	public void testIsWithinSemitoneRangeOf() {
		IntervalRange range = IntervalRange.SIXTH;
		assertTrue(range.isWithinSemitoneRangeOf(8));
		assertTrue(!range.isWithinSemitoneRangeOf(11));
		
		range = IntervalRange.ELEVENTH;
		assertTrue(!range.isWithinSemitoneRangeOf(15));
		assertTrue(range.isWithinSemitoneRangeOf(18));
	}
}
