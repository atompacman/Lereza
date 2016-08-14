package com.atompacman.lereza.core.theory;

import static com.google.common.base.Preconditions.checkArgument;

public final class CircleOfFifths {

    //
    //  ~  STATIC FIELDS  ~  //
    //
    
    private static Tone[] circleOfFifths;


    //
    //  ~  INIT  ~  //
    //

    private CircleOfFifths() {
        
    }
    
    static {
        createCircle();
    }
    
    private static void createCircle() {
        circleOfFifths = new Tone[Accidental.values().length * NoteLetter.values().length];

        for (Accidental accidental : Accidental.values()) {
            for (NoteLetter noteLetter : NoteLetter.values()) {
                Tone tone = Tone.of(noteLetter, accidental);
                circleOfFifths[circlePosToArrayPos(positionOf(tone))] = tone;
            }
        }
    }


    //
    //  ~  KEY ARMOR  ~  //
    //

    public static Accidental accidentalOfKey(Key key) {
        return Accidental.of(Integer.signum(positionOf(switchToMajorKeyTone(key))));
    }

    public static int numAccidentalsOfKey(Key key) {
        return Math.abs(positionOf(switchToMajorKeyTone(key)));
    }

    private static Tone switchToMajorKeyTone(Key key) {
        return (key.getQuality() == Quality.MINOR ? key.relativeKey() : key).getTone();
    }
    

    //
    //  ~  POSITION IN ORDERS  ~  //
    //

    public static int positionOf(Tone tone) {
        int pos = basicPositionInOrderOfSharps(tone.getNote()) - 1;
        pos += tone.getAlteration().semitoneAlteration() * DiatonicTones.IN_OCTAVE;
        return pos;
    }

    public static int positionInOrderOfSharps(Tone tone) {
        checkArgument(tone.getAlteration() == Accidental.SHARP, 
                String.format("The position of a tone in the order of sharps can "
                        + "only be found for a sharp tone (Got \"%s\").", tone));
        return basicPositionInOrderOfSharps(tone.getNote());
    }

    public static int positionInOrderOfFlats(Tone tone) {
        checkArgument(tone.getAlteration() == Accidental.FLAT, 
                String.format("The position of a tone in the order of flats can "
                        + "only be found for a flat tone (Got \"%s\").", tone));
        return DiatonicTones.IN_OCTAVE - basicPositionInOrderOfSharps(tone.getNote()) - 1;
    }

    protected static int basicPositionInOrderOfSharps(NoteLetter noteLetter) {
        return ((noteLetter.ordinal() * 2) + 1) % DiatonicTones.IN_OCTAVE;
    }


    //
    //  ~  TONE AT POSITION  ~  //
    //

    public static Tone toneAtPosition(int posInCircle) {
        checkArgument(isAValidPositionInCircle(posInCircle), posInCircle + 
                " is not a valid position in the circle of fifths.");
        return circleOfFifths[circlePosToArrayPos(posInCircle)];
    }

    private static int circlePosToArrayPos(int posInCircle) {
        return posInCircle + DiatonicTones.IN_OCTAVE + 1;
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

    private static int arrayPosToCirclePos(int posInArray) {
        return posInArray - DiatonicTones.IN_OCTAVE - 1;
    }
}
