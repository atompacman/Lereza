package com.atompacman.lereza.common.solfege;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.lereza.common.solfege.quality.AdvancedQuality;
import com.atompacman.lereza.common.solfege.quality.Quality;

public class CircleOfFifths {

	private static final Tone firstToneOfCircle = new Tone(NoteLetter.F, Accidental.FLAT);
	private static final Tone lastToneOfCircle = new Tone(NoteLetter.B, Accidental.SHARP);
	private static int middleOfCircle;

	private static List<Tone> circleOfFifths;
	private static List<Tone> orderOfSharps;
	private static List<Tone> orderOfFlats;

	
	//------------ IMMEDIATE INITIALIZATION ------------\\

	static {
		buildCircleOfFifths();
		buildOrderOfSharps();
		buildOrderOfFlats();
	}
	
	private static void buildCircleOfFifths() {
		circleOfFifths = new ArrayList<Tone>();
		Interval fifth = new Interval(IntervalRange.FIFTH);
		Tone tone = firstToneOfCircle;
		
		while (!tone.equals(lastToneOfCircle)) {
			circleOfFifths.add(tone);
			tone = tone.afterInterval(fifth);
		}
		circleOfFifths.add(lastToneOfCircle);

		middleOfCircle = circleOfFifths.indexOf(new Tone(NoteLetter.C));
	}
	
	private static void buildOrderOfSharps() {
		orderOfSharps = new ArrayList<Tone>();
		Interval fifth = new Interval(IntervalRange.FIFTH);
		Tone tone = new Tone(NoteLetter.F, Accidental.SHARP);

		for (;;) {
			orderOfSharps.add(tone);
			if (orderOfSharps.size() == DiatonicTones.IN_OCTAVE) {
				break;
			}
			tone = tone.afterInterval(fifth);
		}
	}

	private static void buildOrderOfFlats() {
		orderOfFlats = new ArrayList<Tone>();
		Interval descFifth = 
				new Interval(Direction.DESCENDING, AdvancedQuality.PERFECT, IntervalRange.FIFTH);
		Tone tone = new Tone(NoteLetter.B, Accidental.FLAT);

		for (;;) {
			orderOfFlats.add(tone);
			if (orderOfFlats.size() == DiatonicTones.IN_OCTAVE) {
				break;
			}
			tone = tone.afterInterval(descFifth);
		}
	}
	

	//------------ KEY ARMOR ------------\\

	public static Accidental accidentalOfKey(Key key) {
		Tone toneKey = switchToMajorKeyTone(key);
		int posOfKeyInCircle = circleOfFifths.indexOf(toneKey);
		
		if (posOfKeyInCircle < middleOfCircle) {
			return Accidental.FLAT;
		} else if (posOfKeyInCircle > middleOfCircle) {
			return Accidental.SHARP;
		} else {
			return Accidental.NONE;
		}
	}

	public static int nbAccidentalsOfKey(Key key) {
		Tone toneKey = switchToMajorKeyTone(key);
		return Math.abs(distanceToMiddleOfCircle(toneKey));
	}

	
	//------------ POSITION IN ORDERS ------------\\

	public static int positionInOrderOfSharps(Tone tone) {
		int pos = orderOfSharps.indexOf(tone);
		if (pos == -1) {
			throw new IllegalArgumentException("The position of a tone in the order of sharps can "
					+ "only be bard for a sharp tone (Got \"" + tone.toString() + "\").");
		}
		return pos;
	}
	
	public static int positionInOrderOfFlats(Tone tone) {
		int pos = orderOfFlats.indexOf(tone);
		if (pos == -1) {
			throw new IllegalArgumentException("The position of a tone in the order of flats can "
					+ "only be bard for a flat tone (Got \"" + tone.toString() + "\").");
		}
		return pos;
	}
	
	
	//------------ PRIVATE UTILS ------------\\

	private static int distanceToMiddleOfCircle(Tone tone) {
		return circleOfFifths.indexOf(tone) - middleOfCircle;
	}
	
	private static Tone switchToMajorKeyTone(Key key) {
		if (key.getQuality() == Quality.MINOR) {
			return key.parallelKey().getTone();
		}
		return key.getTone();
	}
}
