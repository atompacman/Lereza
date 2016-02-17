package com.atompacman.lereza.core.theory;

import com.atompacman.toolkat.misc.EnumCompositeObjectConstructor;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Pitch {

    //
    //  ~  CONSTANTS  ~  //
    //

    private static final EnumCompositeObjectConstructor<AutoValue_Pitch> ECOC = 
            new EnumCompositeObjectConstructor<AutoValue_Pitch>(AutoValue_Pitch.class);


    //
    //  ~  FIELDS  ~  //
    //

    public abstract Tone   getTone();
    public abstract Octave getOctave();
    

    //
    //  ~  INIT  ~  //
    //

    public static Pitch of(Tone tone, Octave octave) {
        return new AutoValue_Pitch(tone, octave);
    }

    public static Pitch of(NoteLetter letter, Accidental accidental, Octave octave) {
        return new AutoValue_Pitch(Tone.of(letter, accidental), octave);
    }

    public static Pitch of(String repres) {
        return ECOC.newInstance(repres);
    }

    public static Pitch thatIsMoreCommonForHexValue(byte hexNote) {
        Tone tone = Tone.thatIsMoreCommonForSemitoneValue(hexNote);
        Octave octave = Octave.fromHex(hexNote);
        return new AutoValue_Pitch(tone, octave);
    }


    //
    //  ~  VALUE  ~  //
    //

    public int semitoneValue() {
        return getOctave().semitoneValue() + getTone().semitoneValue();
    }

    public int diatonicToneValue() {
        return getOctave().diatonicToneValue() + getTone().diatonicToneValue();
    }

    //
    //  ~  SERIALIZATION  ~  //
    //

    @Override
    public String toString() {
        return toStaccato();
    }

    public String toStaccato() {
        return getTone().toString() + getOctave().toString();
    }
}
