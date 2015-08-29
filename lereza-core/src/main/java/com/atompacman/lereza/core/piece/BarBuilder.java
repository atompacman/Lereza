package com.atompacman.lereza.core.piece;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

import com.atompacman.lereza.core.solfege.Pitch;
import com.atompacman.lereza.core.solfege.RythmicSignature;
import com.atompacman.lereza.core.solfege.Value;
import com.atompacman.toolkat.module.AnomalyDescription;
import com.atompacman.toolkat.module.AnomalyDescription.Severity;

public class BarBuilder extends PieceComponentBuilder<Bar<Stack<Note>>> {

    //===================================== INNER TYPES ==========================================\\

    private enum Anomaly {

        @AnomalyDescription (
                name         = "Timeunit outside bar limits",
                description  = "A note beginning or ending timeunit was outside bar limits",
                consequences = "Timeunit is brought back within the limits", 
                severity     = Severity.MODERATE)
        TIMEUNIT_OUTSIDE_BAR_LIMITS;
    }



    //======================================= FIELDS =============================================\\

    private final List<StackBuilder> builders;
    private final RythmicSignature   rythmicSign;

    private int currBegTU;
    private int currLenTU;
    private int currVelocity;



    //======================================= METHODS ============================================\\

    //---------------------------------- PUBLIC CONSTRUCTOR --------------------------------------\\

    public BarBuilder() {
        this(RythmicSignature.STANDARD_4_4);
    }

    public BarBuilder(RythmicSignature rythmicSign) {
        this.builders = new ArrayList<>();
        this.rythmicSign = rythmicSign;

        this.currBegTU = 0;
        this.currLenTU = Value.QUARTER.toTimeunit();
        this.currVelocity = StackBuilder.DEFAULT_VELOCITY;
    }


    //----------------------------------------- ADD ----------------------------------------------\\

    public BarBuilder add(Pitch pitch, int velocity, int begTU, int lengthTU, boolean isTied) {
        int endTU = begTU + lengthTU;
        checkTimeunit(begTU, "beginning");
        checkTimeunit(endTU, "ending");

        Note note = Note.valueOf(pitch, splitIntoValues(begTU, endTU));

        log(Level.TRACE, "Adding note %-16s from timeunit %4d to %4d", 
                note.toCompleteString(), begTU, endTU);

        builderAt(begTU).add(note, velocity, !isTied);

        for (int i = begTU + 1; i < endTU; ++i) {
            builderAt(i).add(note, velocity, false);
        }

        return this;
    }

    public BarBuilder add(Pitch pitch, int velocity, int begTU, int lengthTU) {
        return add(pitch, velocity, begTU, lengthTU, false);
    }

    public BarBuilder add(Pitch pitch) {
        return add(pitch, currVelocity, currBegTU, currLenTU, false);
    }

    public BarBuilder add(String pitch) {
        return add(Pitch.valueOf(pitch), currVelocity, currBegTU, currLenTU, false);
    }

    public BarBuilder pos(int timeunit) {
        this.currBegTU = timeunit;
        return this;
    }

    public BarBuilder length(int noteLenTU) {
        this.currLenTU = noteLenTU;
        return this;
    }

    public BarBuilder velocity(int velocity) {
        this.currVelocity = velocity;
        return this;
    }

    private StackBuilder builderAt(int begTU) {
        while (begTU >= builders.size()) {
            StackBuilder builder = new StackBuilder();
            registerSubmodule(builder);
            builders.add(builder);
        }
        return builders.get(begTU);
    }

    private int checkTimeunit(int timeunit, String tuName) {
        if (timeunit < 0) {
            signal(Anomaly.TIMEUNIT_OUTSIDE_BAR_LIMITS, "A negative "
                    + "%s timeunit (%d) was set back to 0", tuName, timeunit);
            return 0;
        }
        if (timeunit > rythmicSign.timeunitsInABar()) {
            signal(Anomaly.TIMEUNIT_OUTSIDE_BAR_LIMITS, "A %s timeunit "
                    + "(%d) that was greater than bar length (%d) was brought down to "
                    + "end of bar", tuName, timeunit, rythmicSign.timeunitsInABar());
            return rythmicSign.timeunitsInABar();
        }
        return timeunit;
    }

    private static List<Value> splitIntoValues(int noteStart, int noteEnd) {
        List<Value> values = new ArrayList<>();
        int noteLength = noteEnd - noteStart;

        for (int i = Value.values().length - 1; i >= 0; --i) {
            Value value = Value.values()[i];
            int valueLength = value.toTimeunit();

            if (valueLength > noteLength) {
                continue;
            }
            int valueStart = 0;
            while (valueStart < noteStart) {
                valueStart += valueLength;
            }
            int valueEnd = valueStart + valueLength;

            if (valueEnd > noteEnd) {
                continue;
            }
            if (noteStart < valueStart) {
                values.addAll(splitIntoValues(noteStart, valueStart));
            }
            values.add(value);

            if (valueEnd < noteEnd) {
                values.addAll(splitIntoValues(valueEnd, noteEnd));
            }
            break;
        }

        return values;
    }


    //---------------------------------------- BUILD ---------------------------------------------\\

    protected Bar<Stack<Note>> buildComponent() {
        List<Stack<Note>> noteStacks = new ArrayList<>();
        for (StackBuilder builder : builders) {
            noteStacks.add(builder.build());
        }
        return new Bar<Stack<Note>>(noteStacks, rythmicSign);
    }


    //---------------------------------------- RESET ---------------------------------------------\\

    protected void reset() {
        builders.clear();
    }
}
