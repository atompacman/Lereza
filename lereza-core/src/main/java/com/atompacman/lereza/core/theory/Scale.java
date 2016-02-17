package com.atompacman.lereza.core.theory;

import java.util.List;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Scale {

    //
    //  ~  FIELDS  ~  //
    //

    public abstract Tone      getTone();
    public abstract ScaleType getType();


    //
    //  ~  INIT  ~  //
    //

    public static Scale valueOf(Tone tone, ScaleType type) {
        return new AutoValue_Scale(tone, type);
    }


    //
    //  ~  GETTERS  ~  //
    //

    public Key getKey() {
        return Key.of(getTone(), getQuality());
    }

    public Quality getQuality() {
        if (getType().name().contains(Quality.MAJOR.name())) {
            return Quality.MAJOR;
        } else if (getType().name().contains(Quality.MINOR.name())) {
            return Quality.MINOR;
        }
        throw new IllegalStateException("Scaletype is neither major nor minor.");
    }


    //
    //  ~  CONTAINS  ~  //
    //

    public boolean contains(Tone tone) {
        Direction direction = Direction.ASCENDING;
        if (getTone().equals(tone)) {
            direction = Direction.STRAIGHT;
        }

        Interval interval;
        try {
            interval = Interval.between(getTone(), direction, tone).ascending();
        } catch (IllegalArgumentException e) {
            return false;
        }

        int diatonicTonesDelta = DiatonicTones.between(getTone(), Direction.ASCENDING, tone);
        ScaleDegree targetDegree = ScaleDegree.values()[diatonicTonesDelta];

        return getType().intervalFromRootTo(targetDegree).equals(interval);
    }

    public boolean contains(List<Tone> tones) {
        for (Tone tone : tones) {
            if (!contains(tone)) {
                return false;
            }
        }
        return true;
    }


    //
    //  ~  SERIALIZATION  ~  //
    //

    public String toString() {
        return getKey().toString() + " scale";
    }
}
