package atompacman.lereza.common.solfege;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import com.atompacman.lereza.common.solfege.Direction;
import com.atompacman.lereza.common.solfege.Interval;
import com.atompacman.lereza.common.solfege.IntervalRange;
import com.atompacman.lereza.common.solfege.Pitch;
import com.atompacman.lereza.common.solfege.quality.AdvancedQuality;
import com.atompacman.lereza.common.solfege.quality.Quality;

public class TestInterval {

	@Test (expected=IllegalArgumentException.class)
	public void testQualityTypeVerification() {
		@SuppressWarnings("unused")
		Interval a = new Interval(Quality.MAJOR, IntervalRange.FIFTH);
	}

	@Test (expected=IllegalArgumentException.class)
	public void testUnisonDirectionVerification() {
		@SuppressWarnings("unused")
		Interval a = new Interval(Direction.ASCENDING, Quality.MAJOR, IntervalRange.UNISON);
	}

	@Test (expected=IllegalArgumentException.class)
	public void testMaxFromSemitoneValueVerification() {
		Interval.fromSemitoneValue(44);
	}

	@Test
	public void testAllSemitoneValues() {
		Map<Integer, Interval> goodValues = new HashMap<Integer, Interval>();

		for (IntervalRange range : IntervalRange.values()) {
			if (range.getQualityType().equals(Quality.class)) {
				Interval a = new Interval(Quality.MINOR, range);
				Interval b = new Interval(Quality.MAJOR, range);
				goodValues.put(a.semitoneValue(), a);
				goodValues.put(b.semitoneValue(), b);
			} else {
				Interval c = new Interval(AdvancedQuality.PERFECT, range);
				goodValues.put(c.semitoneValue(), c);

				if (range == IntervalRange.FIFTH || range == IntervalRange.TWELVTH) {
					Interval d = new Interval(AdvancedQuality.DIMINISHED, range);
					goodValues.put(d.semitoneValue(), d);
				}
			}
		}
		for (Entry<Integer, Interval> entry : goodValues.entrySet()) {
			int semitones = entry.getKey();
			Interval ref = entry.getValue();
			Interval res = Interval.fromSemitoneValue(semitones);
			assertTrue(ref.equals(res));
		}
	}

	@Test
	public void testGetSimpleInterval() {
		Pitch a = Pitch.valueOf("A5");
		Pitch b = Pitch.valueOf("E6");
		Interval c = new Interval(IntervalRange.FIFTH);
		assertTrue(Interval.getSimpleInterval(a, b).equals(c));
		
		a = Pitch.valueOf("G2");
		b = Pitch.valueOf("D#2");
		c = new Interval(Direction.DESCENDING, Quality.MAJOR, IntervalRange.THIRD);
		assertTrue(Interval.getSimpleInterval(a, b).equals(c));
		
		a = Pitch.valueOf("C2");
		b = Pitch.valueOf("C2");
		c = new Interval(IntervalRange.UNISON);
		assertTrue(Interval.getSimpleInterval(a, b).equals(c));
		
		a = Pitch.valueOf("B4");
		b = Pitch.valueOf("F6");
		c = new Interval(AdvancedQuality.DIMINISHED, IntervalRange.TWELVTH);
		assertTrue(Interval.getSimpleInterval(a, b).equals(c));
	}
}
