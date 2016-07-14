package com.fxguild.lereza.common.theory;

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

    public static Scale of(Tone tone, ScaleType type) {
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
        Degree targetDegree = Degree.values()[diatonicTonesDelta];

        return getType().intervalFromRootTo(targetDegree).equals(interval);
    }

    public boolean containsAll(List<Tone> tones) {
        return tones.stream().allMatch(t -> contains(t));
    }


    //
    //  ~  SERIALIZATION  ~  //
    //

    @Override
    public String toString() {
        return getKey().toString() + " scale";
    }
}
