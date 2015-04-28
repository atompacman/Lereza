package com.atompacman.lereza.solfege;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.atompacman.lereza.solfege.Accidental;
import com.atompacman.lereza.solfege.Chord;
import com.atompacman.lereza.solfege.ChordType;
import com.atompacman.lereza.solfege.Interval;
import com.atompacman.lereza.solfege.IntervalRange;
import com.atompacman.lereza.solfege.NoteLetter;
import com.atompacman.lereza.solfege.Tone;
import com.atompacman.lereza.solfege.quality.Quality;

public class TestChord {

	//------------ STATIC CONSTRUCTORS ------------\\

	@Test
	public void testValueOf() {
		Chord a = Chord.valueOf(Tone.valueOf("B"), ChordType.valueOf("m"));
		List<Interval> intervals = new ArrayList<Interval>();
		intervals.add(Interval.valueOf(				  IntervalRange.UNISON));
		intervals.add(Interval.valueOf(Quality.MINOR, IntervalRange.THIRD));
		intervals.add(Interval.valueOf(				  IntervalRange.FIFTH));
		Tone tone = Tone.valueOf(NoteLetter.B, Accidental.NONE);
		assertTrue(a.getTone().equals(tone));
		assertTrue(a.getChord().getIntervals().equals(intervals));
		assertTrue(a.getIntervals().toList().equals(intervals));
		
		a = Chord.valueOf(Tone.valueOf("D#"), ChordType.valueOf("sus2"));
		intervals.clear();
		intervals.add(Interval.valueOf(				  IntervalRange.UNISON));
		intervals.add(Interval.valueOf(Quality.MAJOR, IntervalRange.SECOND));
		intervals.add(Interval.valueOf(				  IntervalRange.FIFTH));
		tone = Tone.valueOf(NoteLetter.D, Accidental.SHARP);
		assertTrue(a.getTone().equals(tone));
		assertTrue(a.getChord().getIntervals().equals(intervals));
		assertTrue(a.getIntervals().toList().equals(intervals));
	}
}
