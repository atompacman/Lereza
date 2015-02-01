package com.atompacman.lereza.solfege;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.atompacman.lereza.solfege.IntervalRange;
import com.atompacman.lereza.solfege.quality.AdvancedQuality;
import com.atompacman.lereza.solfege.quality.Quality;

public class TestIntervalRange {

	//------------ PRIVATE CONSTRUCTOR ------------\\

	@Test
	public void testConstructor() {
		assertEquals(IntervalRange.FOURTH.getQualityType(), AdvancedQuality.class);
		assertEquals(IntervalRange.THIRTEENTH.getQualityType(), Quality.class);
	}
	
	
	//------------ STATIC CONSTRUCTORS ------------\\

	@Test
	public void testClosertRangesFrom() {
		List<IntervalRange> closestRanges = IntervalRange.closestRangesFrom(5);
		assertEquals(closestRanges.size(), 1);
		assertEquals(closestRanges.get(0), IntervalRange.FOURTH);
		
		closestRanges = IntervalRange.closestRangesFrom(6);
		assertEquals(closestRanges.size(), 2);
		assertEquals(closestRanges.get(0), IntervalRange.FOURTH);
		assertEquals(closestRanges.get(1), IntervalRange.FIFTH);
		
		closestRanges = IntervalRange.closestRangesFrom(23);
		assertEquals(closestRanges.size(), 2);
		assertEquals(closestRanges.get(0), IntervalRange.FOURTEENTH);
		assertEquals(closestRanges.get(1), IntervalRange.DOUBLE_OCTAVE);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testPositiveVerification() {
		IntervalRange.closestRangesFrom(-4);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testMaximumValueVerification() {
		IntervalRange.closestRangesFrom(44);
	}
	
	
	//------------ TONE / SEMITONE VALUE ------------\\

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
