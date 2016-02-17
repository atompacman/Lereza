package com.atompacman.lereza.core.theory;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Degree {

    //
    //  ~  FIELDS  ~  //
    //

    public abstract ScaleDegree getDegreeOnScale();
    public abstract ChordType   getChordType();

    
    //
    //  ~  INIT  ~  //
    //

    public static Degree of(String str) {
        checkArgument(!str.isEmpty(), "Degree representation must not be null or empty.");

        Quality quality = null;
        String firstChar = str.substring(0, 1);
        int separation;

        if (firstChar.toUpperCase().equals(firstChar)) {
            quality = Quality.MAJOR;
            int indexOfI = str.lastIndexOf('I');
            int indexOfV = str.lastIndexOf('V');
            separation = Math.max(indexOfI, indexOfV) + 1;
        } else {
            quality = Quality.MINOR;
            int indexOfi = str.lastIndexOf('i');
            int indexOfv = str.lastIndexOf('v');
            separation = Math.max(indexOfi, indexOfv) + 1;
        }
        
        ScaleDegree degree = ScaleDegree.valueOf(str.substring(0, separation).toUpperCase());

        String chordRepres = str.substring(separation);
        if (quality == Quality.MINOR) {
            chordRepres = "m" + chordRepres;
        }

        return new AutoValue_Degree(degree, ChordType.of(chordRepres));
    }

    public static Degree of(ScaleDegree degreeOnScale, ChordType chord) {
        return new AutoValue_Degree(degreeOnScale, chord);
    }
}
