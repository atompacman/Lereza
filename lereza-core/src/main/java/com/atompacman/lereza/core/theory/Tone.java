package com.atompacman.lereza.core.theory;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.List;

import com.atompacman.toolkat.misc.EnumCompositeObjectConstructor;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Tone {

    //
    //  ~  CONSTANTS  ~  //
    //

    private static EnumCompositeObjectConstructor<AutoValue_Tone> ECOC = 
            new EnumCompositeObjectConstructor<AutoValue_Tone>(AutoValue_Tone.class);


    //
    //  ~  FIELDS  ~  //
    //
    public abstract NoteLetter getNote();
    public abstract Accidental getAlteration();


    //
    //  ~  INIT  ~  //
    //

    public static Tone of(NoteLetter note) {
        return new AutoValue_Tone(note, Accidental.NONE);
    }

    public static Tone of(NoteLetter note, Accidental alteration) {
        return new AutoValue_Tone(note, alteration);
    }

    public static Tone of(String str) {
        return ECOC.newInstance(str);
    }

    public static Tone of(int semitoneValue, int diatonicToneValue) {
        semitoneValue = Semitones.normalize(semitoneValue);
        NoteLetter note = NoteLetter.fromDiatonicToneValue(diatonicToneValue);
        return fromNoteAndSemitoneValue(note, semitoneValue);
    }

    public static Tone thatIsMoreCommonForSemitoneValue(int semitoneValue) {
        List<Tone> possibleTones = withSemitoneValueOf(semitoneValue);
        if (possibleTones.size() == 1) {
            return possibleTones.get(0);
        }
        Tone a = possibleTones.get(0);
        Tone b = possibleTones.get(1);

        int posA = 0;
        int posB = 0;;

        switch (a.getAlteration()) {
        case FLAT:  posA = CircleOfFifths.positionInOrderOfFlats(a);  break;
        case SHARP: posA = CircleOfFifths.positionInOrderOfSharps(a); break;
        case NONE:  posA = 0;                                         break;
        }
        switch (b.getAlteration()) {
        case FLAT:  posB = CircleOfFifths.positionInOrderOfFlats(b);  break;
        case SHARP: posB = CircleOfFifths.positionInOrderOfSharps(b); break;
        case NONE:  posB = 0;                                         break;
        }

        if (Math.abs(posA) == Math.abs(posB)) {
            if (posA < posB) {
                return a;
            } else {
                return b;
            }
        } else if (Math.abs(posA) < Math.abs(posB)) {
            return a;
        } else {
            return b;
        }
    }

    public static List<Tone> withSemitoneValueOf(int semitoneValue) {
        semitoneValue = Semitones.normalize(semitoneValue);
        List<NoteLetter> possibleNotes = NoteLetter.withSemitoneValue(semitoneValue);
        List<Tone> possibleTones = new ArrayList<Tone>();

        for (NoteLetter possibleNote : possibleNotes) {
            possibleTones.add(fromNoteAndSemitoneValue(possibleNote, semitoneValue));
        }
        return possibleTones;
    }

    protected static Tone fromNoteAndSemitoneValue(NoteLetter note, int semitoneValue) {
        checkArgument(note.canBeAssignedFrom(semitoneValue), String.format("Note \"%s\" "
                + "can be assigned from semitone value \"%d\".", note, semitoneValue));
        
        int semitoneAlteration = semitoneValue - note.basicSemitoneValue();
        if (semitoneAlteration == Semitones.IN_OCTAVE - 1) {
            semitoneAlteration -= Semitones.IN_OCTAVE;
        } else if (semitoneAlteration == - (Semitones.IN_OCTAVE - 1)) {
            semitoneAlteration += Semitones.IN_OCTAVE;
        }
        return new AutoValue_Tone(note, Accidental.of(semitoneAlteration));
    }


    //
    //  ~  WITH SWITCHED ALTERATION  ~  //
    //

    public Tone withSwitchedAlteration() {
        switch (getAlteration()) {
        case FLAT:
            return new AutoValue_Tone(getNote().getPrevious(), Accidental.SHARP);
        case SHARP:
            return new AutoValue_Tone(getNote().getNext(), Accidental.FLAT);
        default: 
            return null;
        }
    }

    public Tone withAlteration(Accidental accidental) {
        return getAlteration() == accidental ? this : withSwitchedAlteration();
    }


    //
    //  ~  AFTER INTERVAL  ~  //
    //

    public Tone afterInterval(Interval interval) {
        int finalSemitone = semitoneValue() + interval.semitoneValue();
        int finalDiatonicTone = diatonicToneValue() + interval.diatonicToneValue();
        return of(finalSemitone, finalDiatonicTone);
    }


    //
    //  ~  VALUE  ~  //
    //

    public int diatonicToneValue() {
        return getNote().ordinal();
    }

    public int semitoneValue() {
        return getNote().basicSemitoneValue() + getAlteration().semitoneAlteration();
    }


    //
    //  ~  SERIALIZATION  ~  //
    //
    
    @Override
    public String toString() {
        return getNote().name() + getAlteration().toString();
    }
}
