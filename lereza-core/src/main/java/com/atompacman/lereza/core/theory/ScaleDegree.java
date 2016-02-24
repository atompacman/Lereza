package com.atompacman.lereza.core.theory;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ScaleDegree {

    //
    //  ~  FIELDS  ~  //
    //

    public abstract Degree    getDegreeOnScale();
    public abstract ChordType getChordType();

    
    //
    //  ~  INIT  ~  //
    //

    public static ScaleDegree of(String str) {
        checkArgument(!str.isEmpty(), "Degree representation must not be null or empty.");

        Quality quality = null;
        String firstChar = str.substring(0, 1);
        int separation;

        if (firstChar.toUpperCase().equals(firstChar)) {
            quality    = Quality.MAJOR;
            separation = Math.max(str.lastIndexOf('I'), str.lastIndexOf('V')) + 1;
        } else {
            quality    = Quality.MINOR;
            separation = Math.max(str.lastIndexOf('i'), str.lastIndexOf('v')) + 1;
        }
        
        Degree degree = Degree.valueOf(str.substring(0, separation).toUpperCase());

        String chordRepres = str.substring(separation);
        if (quality == Quality.MINOR) {
            chordRepres = "m" + chordRepres;
        }

        return new AutoValue_ScaleDegree(degree, ChordType.of(chordRepres));
    }

    public static ScaleDegree of(Degree degreeOnScale, ChordType chord) {
        return new AutoValue_ScaleDegree(degreeOnScale, chord);
    }
}
