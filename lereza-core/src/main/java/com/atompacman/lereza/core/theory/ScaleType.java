package com.atompacman.lereza.core.theory;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.List;

public enum ScaleType {

    MAJOR          (0, 2, 4, 5, 7, 9, 11),

    MELODIC_MINOR  (0, 2, 3, 5, 7, 9, 11),

    HARMONIC_MINOR (0, 2, 3, 5, 7, 8, 11),

    NATURAL_MINOR  (0, 2, 3, 5, 7, 8, 10);

    
    //
    //  ~  CONSTANTS  ~  //
    //

    //TODO put that back at 4 one everything in the package works + double flats and sharps implem.
    private static final int NB_INTERVAL_IN_DEGREES = 3;

    
    //
    //  ~  FIELDS  ~  //
    //

    
    private final List<Degree> degrees;
    private final Integer[]    degreeSemitones;


    //
    //  ~  INIT  ~  //
    //

    private ScaleType(Integer... semitones) {
        this.degreeSemitones = semitones;

        checkArgument(degreeSemitones.length == DiatonicTones.IN_OCTAVE, "A scaletype "
                + "must be built with " + DiatonicTones.IN_OCTAVE + " semitone values");
        degrees = new ArrayList<Degree>();

        for (int i = 0; i < DiatonicTones.IN_OCTAVE; ++i) {
            List<Interval> intervals = new ArrayList<Interval>();

            for (int j = 0; j < NB_INTERVAL_IN_DEGREES; ++j) {
                int rootNoteSemitones = degreeSemitones[i];
                int intervalSemitones = degreeSemitones[DiatonicTones.normalize(i + 2*j)];
                int semitoneDelta = intervalSemitones - rootNoteSemitones;
                int diatonicToneDelta = 2*j;
                if (semitoneDelta < 0) {
                    semitoneDelta += Semitones.IN_OCTAVE;
                }
                intervals.add(Interval.of(semitoneDelta, diatonicToneDelta));
            }
            ScaleDegree degree = ScaleDegree.values()[i];

            degrees.add(Degree.of(degree, ChordType.of(intervals)));
        }
    }

    public static ScaleType of(Quality quality) {
        if (quality == Quality.MAJOR) {
            return MAJOR;
        } else if (quality == Quality.MINOR) {
            return NATURAL_MINOR;
        }
        throw new IllegalArgumentException("\"" + quality.toString() + 
                "\" is not a valid quality for a scaletype.");
    }


    //
    //  ~  GETTERS  ~  //
    //

    public Degree getDegree(ScaleDegree degree) {
        return degrees.get(degree.ordinal());
    }
    
    
    //
    //  ~  INTERVAL  ~  //
    //

    public Interval intervalFromRootTo(ScaleDegree degree) {
        return Interval.of(degreeSemitones[degree.ordinal()], degree.ordinal());
    }
}
