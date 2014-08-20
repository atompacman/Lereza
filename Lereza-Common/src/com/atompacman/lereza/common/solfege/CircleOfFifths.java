package com.atompacman.lereza.common.solfege;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.lereza.common.solfege.quality.Quality;

public class CircleOfFifths {

	private static final Tone firstTone = Tone.valueOf("Cb");
	private static final Tone lastTone = Tone.valueOf("C#");

	
	private static List<Tone> tones;
	
	static {
		tones = new ArrayList<Tone>();
		
		Tone tone = firstTone;
		while (!tone.equals(lastTone)) {
			tones.add(tone);
			tone = tone.afterInterval(new Interval(IntervalRange.FIFTH));
			if (tones.size() < Tone.NB_SEMITONES_IN_OCTAVE) {
				tone.switchToAlteration(Accidental.FLAT);
			} else {
				tone.switchToAlteration(Accidental.SHARP);
			}
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
		return Math.abs(middleOfCircle() - tones.indexOf(toneKey));
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
