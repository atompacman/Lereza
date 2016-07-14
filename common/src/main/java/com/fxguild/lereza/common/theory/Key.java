package com.fxguild.lereza.common.theory;

import java.util.List;

import com.fxguild.common.EnumCompositeObjectConstructor;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Key {

    //
    //  ~  CONSTANTS  ~  //
    //

    private static final EnumCompositeObjectConstructor<Key> ECOC = 
            EnumCompositeObjectConstructor.of(Key.class);
    
    
    //
    //  ~  FIELDS  ~  //
    //

    public abstract Tone    getTone();
    public abstract Quality getQuality();
    
    
    //
    //  ~  INIT  ~  //
    //

    public static Key of(Tone tone) {
        return new AutoValue_Key(tone, Quality.MAJOR);
    }

    public static Key of(Tone tone, Quality quality) {
        return new AutoValue_Key(tone, quality);
    }

    public static Key of(NoteLetter letter) {
        return new AutoValue_Key(Tone.of(letter), Quality.MAJOR);
    }

    public static Key of(NoteLetter letter, Accidental accidental, Quality quality) {
        return new AutoValue_Key(Tone.of(letter, accidental), quality);
    }

    public static Key of(String str) {
        return ECOC.parse(str);
    }


    //
    //  ~  CORRESPONDING SCALE  ~  //
    //

    public Scale correspondingScale() {
        return Scale.of(getTone(), ScaleType.of(getQuality()));
    }


    //
    //  ~  CONTAINS  ~  //
    //

    public boolean contains(Tone tone) {
        return correspondingScale().contains(tone);
    }

    public boolean contains(List<Tone> tones) {
        for (Tone tone : tones) {
            if (!correspondingScale().contains(tone)) {
                return false;
            }
        }
        return true;
    }


    //
    //  ~  ARMOR  ~  //
    //
    
    public Accidental accidental() {
        return CircleOfFifths.accidentalOfKey(this);
    }

    public int numAccidentals() {
        return CircleOfFifths.numAccidentalsOfKey(this);
    }


    //
    //  ~  RELATIVE KEY  ~  //
    //

    public Key relativeKey() {
        Direction directionOfMinorThird = null;
        Quality quality;

        if (getQuality() == Quality.MINOR) {
            directionOfMinorThird = Direction.ASCENDING;
            quality = Quality.MAJOR;
        } else {
            directionOfMinorThird = Direction.DESCENDING;
            quality = Quality.MINOR;
        }

        Interval intervalToParallel = Interval.of(directionOfMinorThird, 
                Quality.MINOR, IntervalRange.THIRD);

        return new AutoValue_Key(getTone().afterInterval(intervalToParallel), quality);
    }


    //
    //  ~  SERIALIZATION  ~  //
    //

    @Override
    public String toString() {
        return getTone().toString() + " " + getQuality().name().toLowerCase();
    }
}
