package com.atompacman.lereza.common.solfege;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.lereza.common.solfege.quality.Quality;

public class CircleOfFifths {

	private static final Tone firstTone = Tone.valueOf("Fb");
	private static final Tone lastTone = Tone.valueOf("G#");

	
	private static List<Tone> tones;
	
	static {
		tones = new ArrayList<Tone>();
		
		Tone tone = firstTone;
		while (!tone.equals(lastTone)) {
			tones.add(tone);
			tone = tone.afterInterval(new Interval(IntervalRange.FIFTH));
		}
		tones.add(lastTone);
	}
	
	
	//------------ KEY ARMOR ------------\\

	public static Accidental accidentalOfKey(Key key) {
		Tone toneKey = switchToMajorKeyTone(key);
		
		if (tones.indexOf(toneKey) < middleOfCircle()) {
			return Accidental.FLAT;
		} else if (tones.indexOf(toneKey) > middleOfCircle()) {
			return Accidental.SHARP;
		} else {
			return null;
		}
	}
	
	public static int nbAccidentalsOfKey(Key key) {
		Tone toneKey = switchToMajorKeyTone(key);
		return Math.abs(distanceToMiddleOfCircle(toneKey));
	}
	
	
	//------------ DISTANCE TO MIDDLE OF CIRCLE ------------\\

	public static int distanceToMiddleOfCircle(Tone tone) {
		return tones.indexOf(tone) - middleOfCircle();
	}
	
	
	//------------ PRIVATE UTILS ------------\\

	private static Tone switchToMajorKeyTone(Key key) {
		if (key.getQuality() == Quality.MINOR) {
			return key.parallelKey().getTone();
		}
		return key.getTone();
	}
	
	private static int middleOfCircle() {
		return (tones.size() - 1) / 2;
	}
}
