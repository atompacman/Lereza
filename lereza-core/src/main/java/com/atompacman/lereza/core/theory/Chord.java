package com.atompacman.lereza.core.theory;

import static com.google.common.base.Preconditions.*;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Chord {

    //
    //  ~  FIELDS  ~  //
    //

    public abstract Tone      getTone();
    public abstract ChordType getChordType();


    //
    //  ~  INIT  ~  //
    //

    public static Chord of(String str) {
        checkArgument(!str.isEmpty(), "String representation can't be empty.");

        Tone      tone = null;
        ChordType chord = null;
        
        if (str.length() > 1 && (str.charAt(1) == 'b' || str.charAt(1) == '#')) {
            tone  = Tone.of(str.substring(0, 2));
            chord = ChordType.of(str.substring(2));
        } else {
            tone  = Tone.of(str.substring(0, 1));
            chord = ChordType.of(str.substring(1));
        }

        return new AutoValue_Chord(tone, chord);
    }

    public static Chord of(Tone tone, ChordType chord) {
        return new AutoValue_Chord(tone, chord);
    }

    
    //
    //  ~  SERIALIZATION  ~  //
    //

    @Override
    public String toString() {
        return getTone().toString() + getChordType().toString();
    }
}
