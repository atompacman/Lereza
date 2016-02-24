package com.atompacman.lereza.core.theory;

import java.util.Optional;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Note {

    //
    //  ~  FIELDS  ~  //
    //

    public abstract Pitch             getPitch();
    public abstract RythmnValue       getRythmnValue();
    public abstract Optional<Dynamic> getDynamic();
    

    //
    //  ~  INIT  ~  //
    //

    public static Note of(String pitch, RythmnValue value) {
        return new AutoValue_Note(Pitch.of(pitch), value, Optional.empty());
    }

    public static Note of(Pitch pitch, RythmnValue value) {
        return new AutoValue_Note(pitch, value, Optional.empty());
    }

    public static Note of(Pitch pitch, RythmnValue value, Dynamic dynamic) {
        return new AutoValue_Note(pitch, value, Optional.of(dynamic));
    }
    

    //
    //  ~  SERIALIZATION  ~  //
    //

    @Override
    public String toString() {
        return getPitch().toString();
    }

    public String toStaccato() {
        StringBuilder sb = new StringBuilder();
        sb.append(getPitch().toStaccato());
        sb.append(getRythmnValue().toStaccato());
        Optional<Dynamic> dynamic = getDynamic();
        if (dynamic.isPresent()) {
            sb.append('a').append(dynamic.get().getVelocity());
        }
        return sb.toString();
    }
}