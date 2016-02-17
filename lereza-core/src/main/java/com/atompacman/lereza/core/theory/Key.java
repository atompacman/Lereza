package com.atompacman.lereza.core.theory;

import java.util.List;

import com.atompacman.toolkat.misc.EnumCompositeObjectConstructor;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Key {

    //
    //  ~  CONSTANTS  ~  //
    //

    private static final EnumCompositeObjectConstructor<AutoValue_Key> ECOC = 
            new EnumCompositeObjectConstructor<AutoValue_Key>(AutoValue_Key.class);
    
    
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
        return ECOC.newInstance(str);
    }


    //
    //  ~  CORRESPONDING SCALE  ~  //
    //

    public Scale correspondingScale() {
        return Scale.valueOf(getTone(), ScaleType.of(getQuality()));
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

    public int nbAccidentals() {
        return CircleOfFifths.nbAccidentalsOfKey(this);
    }


    //
    //  ~  PARALLEL KEY  ~  //
    //

    public Key parallelKey() {
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
