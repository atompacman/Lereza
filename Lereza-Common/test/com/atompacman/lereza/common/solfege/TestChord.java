package com.atompacman.lereza.common.solfege;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.atompacman.lereza.common.solfege.Accidental;
import com.atompacman.lereza.common.solfege.Chord;
import com.atompacman.lereza.common.solfege.ChordType;
import com.atompacman.lereza.common.solfege.Interval;
import com.atompacman.lereza.common.solfege.IntervalRange;
import com.atompacman.lereza.common.solfege.NoteLetter;
import com.atompacman.lereza.common.solfege.Tone;
import com.atompacman.lereza.common.solfege.quality.Quality;

public class TestChord {

	@Test
	public void testValueOf() {
		Chord a = new Chord(Tone.valueOf("B"), ChordType.valueOf("m"));
		List<Interval> intervals = new ArrayList<Interval>();
		intervals.add(new Interval(				  IntervalRange.UNISON));
		intervals.add(new Interval(Quality.MINOR, IntervalRange.THIRD));
		intervals.add(new Interval(				  IntervalRange.FIFTH));
		Tone tone = new Tone(NoteLetter.B, Accidental.NONE);
		assertTrue(a.getTone().equals(tone));
		assertTrue(a.getChord().getIntervals().equals(intervals));
		assertTrue(a.getIntervals().toList().equals(intervals));
		
		a = new Chord(Tone.valueOf("D#"), ChordType.valueOf("sus2"));
		intervals.clear();
		intervals.add(new Interval(				  IntervalRange.UNISON));
		intervals.add(new Interval(Quality.MAJOR, IntervalRange.SECOND));
		intervals.add(new Interval(				  IntervalRange.FIFTH));
		tone = new Tone(NoteLetter.D, Accidental.SHARP);
		assertTrue(a.getTone().equals(tone));
		assertTrue(a.getChord().getIntervals().equals(intervals));
		assertTrue(a.getIntervals().toList().equals(intervals));
	}
}
