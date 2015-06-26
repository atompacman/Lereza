package com.atompacman.lereza.core.solfege;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.atompacman.lereza.core.solfege.ChordType;
import com.atompacman.lereza.core.solfege.Degree;
import com.atompacman.lereza.core.solfege.Interval;
import com.atompacman.lereza.core.solfege.IntervalRange;
import com.atompacman.lereza.core.solfege.ScaleDegree;
import com.atompacman.lereza.core.solfege.ScaleType;

public class TestScaleType {

	//------------ STATIC CONSTRUCTORS ------------\\

	@Test
	public void testValueOf() {
		ScaleType a = ScaleType.valueOf(Quality.MINOR);
		assertEquals(a, ScaleType.NATURAL_MINOR);
	}

	
	//------------ INTERVALS ------------\\

	@Test
	public void testIntervalFromRootTo() {
		Interval a = Interval.valueOf(Quality.MAJOR, IntervalRange.THIRD);
		Interval b = ScaleType.MAJOR.intervalFromRootTo(ScaleDegree.III);
		assertEquals(a, b);
		
		a = Interval.valueOf(Quality.MINOR, IntervalRange.SIXTH);
		b = ScaleType.NATURAL_MINOR.intervalFromRootTo(ScaleDegree.VI);
		assertEquals(a, b);
		
		a = Interval.valueOf(Quality.MAJOR, IntervalRange.SEVENTH);
		b = ScaleType.HARMONIC_MINOR.intervalFromRootTo(ScaleDegree.VII);
		assertEquals(a, b);
	}
	
	
	//------------ GETTERS ------------\\

	@Test
 	public void testDegrees() {
		Degree scaleDegree = ScaleType.MAJOR.getDegree(ScaleDegree.III);
		Degree expected = Degree.valueOf(ScaleDegree.III, ChordType.valueOf("m"));
		assertEquals(scaleDegree, expected);
		
		scaleDegree = ScaleType.NATURAL_MINOR.getDegree(ScaleDegree.II);
		expected = Degree.valueOf(ScaleDegree.II, ChordType.valueOf("dim"));
		assertEquals(scaleDegree, expected);
		
		scaleDegree = ScaleType.MAJOR.getDegree(ScaleDegree.I);
		expected = Degree.valueOf(ScaleDegree.I, ChordType.valueOf(""));
		assertEquals(scaleDegree, expected);
		
		scaleDegree = ScaleType.HARMONIC_MINOR.getDegree(ScaleDegree.IV);
		expected = Degree.valueOf(ScaleDegree.IV, ChordType.valueOf("m"));
		assertEquals(scaleDegree, expected);
	}
}
