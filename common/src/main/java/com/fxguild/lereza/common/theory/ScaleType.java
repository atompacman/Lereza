package com.fxguild.lereza.common.theory;

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

    //TODO set to 4 once double flats and double sharps are implemented
    private static final int NB_INTERVAL_IN_DEGREES = 3;

    
    //
    //  ~  FIELDS  ~  //
    //

    
    private final List<ScaleDegree> degrees;
    private final Integer[]         degreeSemitones;


    //
    //  ~  INIT  ~  //
    //

    private ScaleType(Integer...semitones) {
        checkArgument(semitones.length == DiatonicTones.IN_OCTAVE, "Scaletype \"%s\" must" + 
                " be built with %d semitone values", name(), DiatonicTones.IN_OCTAVE); 

        this.degreeSemitones = semitones;
        this.degrees         = new ArrayList<>(DiatonicTones.IN_OCTAVE);


        for (int i = 0; i < DiatonicTones.IN_OCTAVE; ++i) {
            List<Interval> intervals = new ArrayList<>(NB_INTERVAL_IN_DEGREES);

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
            degrees.add(ScaleDegree.of(Degree.values()[i], ChordType.of(intervals)));
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

    public ScaleDegree getDegree(Degree degree) {
        return degrees.get(degree.ordinal());
    }
    
    
    //
    //  ~  INTERVAL  ~  //
    //

    public Interval intervalFromRootTo(Degree degree) {
        return Interval.of(degreeSemitones[degree.ordinal()], degree.ordinal());
    }
}
