package com.atompacman.lereza.core.solfege;


public class CircleOfFifths {
	
	private static Tone[] circleOfFifths;
	
	static {
		createCircle();
	}
	
	
	//------------ CREATE CIRCLE ------------\\

	private static void createCircle() {
		int nbPossibleTones = Accidental.values().length * NoteLetter.values().length;
		circleOfFifths = new Tone[nbPossibleTones];
		
		for (Accidental accidental : Accidental.values()) {
			for (NoteLetter noteLetter : NoteLetter.values()) {
				Tone tone = Tone.valueOf(noteLetter, accidental);
				circleOfFifths[circlePosToArrayPos(positionOf(tone))] = tone;
			}	
		}
	}
	
	
	//------------ KEY ARMOR ------------\\

	public static Accidental accidentalOfKey(Key key) {
		Tone toneKey = switchToMajorKeyTone(key);
		int sign = Integer.signum(positionOf(toneKey));
		return Accidental.fromSemitoneAlteration(sign);
	}

	public static int nbAccidentalsOfKey(Key key) {
		Tone toneKey = switchToMajorKeyTone(key);
		return Math.abs(positionOf(toneKey));
	}

	
	//------------ POSITION IN ORDERS ------------\\
	
	public static int positionOf(Tone tone) {
		int pos = basicPositionInOrderOfSharps(tone.getNote()) - 1;
		pos += tone.getAlteration().semitoneAlteration() * DiatonicTones.IN_OCTAVE;
		return pos;
	}

	public static int positionInOrderOfSharps(Tone tone) {
		if (tone.getAlteration() != Accidental.SHARP) {
			throw new IllegalArgumentException("The position of a tone"
					+ " in the order of sharps can only be found for a sharp " 
					+ "tone (Got \"" + tone.toString() + "\").");
		}
		return basicPositionInOrderOfSharps(tone.getNote());
	}
	
	public static int positionInOrderOfFlats(Tone tone) {
		if (tone.getAlteration() != Accidental.FLAT) {
			throw new IllegalArgumentException("The position of a tone"
					+ " in the order of flats can only be found for a flat " 
					+ "tone (Got \"" + tone.toString() + "\").");
		}
		return DiatonicTones.IN_OCTAVE - basicPositionInOrderOfSharps(tone.getNote()) - 1;
	}
	
	
	//------------ TONE AT POSITION ------------\\

	public static Tone toneAtPosition(int posInCircle) {
		if (!isAValidPositionInCircle(posInCircle)) {
			throw new IllegalArgumentException(posInCircle + " is not "
					+ "a valid position in the circle of fifths.");
		}
		return circleOfFifths[circlePosToArrayPos(posInCircle)];
	}
	
	public static boolean isAValidPositionInCircle(int posInCircle) {
		return posInCircle >= minPosInCircle() && posInCircle < maxPosInCircle();
	}

	public static int minPosInCircle() {
		return arrayPosToCirclePos(0);
	}
	
	public static int maxPosInCircle() {
		return arrayPosToCirclePos(circleOfFifths.length - 1);
	}
	
	
	//------------ PRIVATE UTILS ------------\\

	protected static int basicPositionInOrderOfSharps(NoteLetter noteLetter) {
		return ((noteLetter.ordinal() * 2) + 1) % DiatonicTones.IN_OCTAVE;
	}
	
	private static int circlePosToArrayPos(int posInCircle) {
		return posInCircle + DiatonicTones.IN_OCTAVE + 1;
	}
	
	private static int arrayPosToCirclePos(int posInArray) {
		return posInArray - DiatonicTones.IN_OCTAVE - 1;
	}
	
	private static Tone switchToMajorKeyTone(Key key) {
		if (key.getQuality() == Quality.MINOR) {
			key = key.parallelKey();
		}
		return key.getTone();
	}
}
