package com.atompacman.lereza.core.solfege;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.atompacman.lereza.core.theory.ChordType;
import com.atompacman.lereza.core.theory.Degree;
import com.atompacman.lereza.core.theory.Interval;
import com.atompacman.lereza.core.theory.IntervalRange;
import com.atompacman.lereza.core.theory.Quality;
import com.atompacman.lereza.core.theory.ScaleDegree;
import com.atompacman.lereza.core.theory.ScaleType;

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
		Interval a = Interval.of(Quality.MAJOR, IntervalRange.THIRD);
		Interval b = ScaleType.MAJOR.intervalFromRootTo(ScaleDegree.III);
		assertEquals(a, b);
		
		a = Interval.of(Quality.MINOR, IntervalRange.SIXTH);
		b = ScaleType.NATURAL_MINOR.intervalFromRootTo(ScaleDegree.VI);
		assertEquals(a, b);
		
		a = Interval.of(Quality.MAJOR, IntervalRange.SEVENTH);
		b = ScaleType.HARMONIC_MINOR.intervalFromRootTo(ScaleDegree.VII);
		assertEquals(a, b);
	}
	
	
	//------------ GETTERS ------------\\

	@Test
 	public void testDegrees() {
		Degree scaleDegree = ScaleType.MAJOR.getDegree(ScaleDegree.III);
		Degree expected = Degree.of(ScaleDegree.III, ChordType.of("m"));
		assertEquals(scaleDegree, expected);
		
		scaleDegree = ScaleType.NATURAL_MINOR.getDegree(ScaleDegree.II);
		expected = Degree.of(ScaleDegree.II, ChordType.of("dim"));
		assertEquals(scaleDegree, expected);
		
		scaleDegree = ScaleType.MAJOR.getDegree(ScaleDegree.I);
		expected = Degree.of(ScaleDegree.I, ChordType.of(""));
		assertEquals(scaleDegree, expected);
		
		scaleDegree = ScaleType.HARMONIC_MINOR.getDegree(ScaleDegree.IV);
		expected = Degree.of(ScaleDegree.IV, ChordType.of("m"));
		assertEquals(scaleDegree, expected);
	}
}
