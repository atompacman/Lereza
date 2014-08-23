package com.atompacman.lereza.common.solfege;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.atompacman.lereza.common.solfege.ChordType;
import com.atompacman.lereza.common.solfege.Degree;
import com.atompacman.lereza.common.solfege.ScaleDegree;
import com.atompacman.lereza.common.solfege.ScaleType;
import com.atompacman.lereza.common.solfege.quality.Quality;

public class TestScaleType {

	@Test
	public void testFromQuality() {
		ScaleType a = ScaleType.fromQuality(Quality.MINOR);
		assertTrue(a == ScaleType.NATURAL_MINOR);
	}

	@Test
	public void testIntervalFromRootTo() {
		Interval a = new Interval(Quality.MAJOR, IntervalRange.THIRD);
		Interval b = ScaleType.MAJOR.intervalFromRootTo(ScaleDegree.III);
		assertTrue(a.equals(b));
		
		a = new Interval(Quality.MINOR, IntervalRange.SIXTH);
		b = ScaleType.NATURAL_MINOR.intervalFromRootTo(ScaleDegree.VI);
		assertTrue(a.equals(b));
		
		a = new Interval(Quality.MAJOR, IntervalRange.SEVENTH);
		b = ScaleType.HARMONIC_MINOR.intervalFromRootTo(ScaleDegree.VII);
		assertTrue(a.equals(b));
	}
	
	@Test
 	public void testDegrees() {
		Degree scaleDegree = ScaleType.MAJOR.getDegree(ScaleDegree.III);
		Degree expected = new Degree(ScaleDegree.III, ChordType.valueOf("m7"));
		assertTrue(scaleDegree.equals(expected));
		
		scaleDegree = ScaleType.NATURAL_MINOR.getDegree(ScaleDegree.II);
		expected = new Degree(ScaleDegree.II, ChordType.valueOf("dim7"));
		assertTrue(scaleDegree.equals(expected));
		
		scaleDegree = ScaleType.MAJOR.getDegree(ScaleDegree.I);
		expected = new Degree(ScaleDegree.I, ChordType.valueOf("M7"));
		assertTrue(scaleDegree.equals(expected));
		
		scaleDegree = ScaleType.HARMONIC_MINOR.getDegree(ScaleDegree.IV);
		expected = new Degree(ScaleDegree.IV, ChordType.valueOf("m7"));
		assertTrue(scaleDegree.equals(expected));
	}
}
