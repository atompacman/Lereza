package com.atompacman.lereza.core.solfege;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.atompacman.lereza.core.theory.Accidental;
import com.atompacman.lereza.core.theory.Chord;
import com.atompacman.lereza.core.theory.ChordType;
import com.atompacman.lereza.core.theory.Interval;
import com.atompacman.lereza.core.theory.IntervalRange;
import com.atompacman.lereza.core.theory.NoteLetter;
import com.atompacman.lereza.core.theory.Quality;
import com.atompacman.lereza.core.theory.Tone;

public class TestChord {

	//------------ STATIC CONSTRUCTORS ------------\\

	@Test
	public void testValueOf() {
		Chord a = Chord.of(Tone.valueOf("B"), ChordType.of("m"));
		List<Interval> intervals = new ArrayList<Interval>();
		intervals.add(Interval.valueOf(				  IntervalRange.UNISON));
		intervals.add(Interval.of(Quality.MINOR, IntervalRange.THIRD));
		intervals.add(Interval.valueOf(				  IntervalRange.FIFTH));
		Tone tone = Tone.valueOf(NoteLetter.B, Accidental.NONE);
		assertTrue(a.getTone().equals(tone));
		assertTrue(a.getChord().getIntervals().equals(intervals));
		assertTrue(a.getIntervals().toList().equals(intervals));
		
		a = Chord.of(Tone.valueOf("D#"), ChordType.of("sus2"));
		intervals.clear();
		intervals.add(Interval.valueOf(				  IntervalRange.UNISON));
		intervals.add(Interval.of(Quality.MAJOR, IntervalRange.SECOND));
		intervals.add(Interval.valueOf(				  IntervalRange.FIFTH));
		tone = Tone.valueOf(NoteLetter.D, Accidental.SHARP);
		assertTrue(a.getTone().equals(tone));
		assertTrue(a.getChord().getIntervals().equals(intervals));
		assertTrue(a.getIntervals().toList().equals(intervals));
	}
}
