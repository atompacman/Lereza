package com.atompacman.lereza.core.piece;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

import com.atompacman.lereza.core.solfege.Pitch;
import com.atompacman.lereza.core.solfege.RythmicSignature;
import com.atompacman.lereza.core.solfege.Value;
import com.atompacman.toolkat.module.Anomaly;
import com.atompacman.toolkat.module.Anomaly.Impact;
import com.atompacman.toolkat.module.Anomaly.Recoverability;

public class BarBuilder extends PieceComponentBuilder<Bar<Stack<Note>>> {

    //===================================== INNER TYPES ==========================================\\

    private enum AN {

        @Anomaly (
                name            = "Timeunit outside bar limits", 
                description     = "A note beginning or ending timeunit was outside bar limits",
                consequences    = "Timeunit is brought back within the limits", 
                impact          = Impact.MODERATE, 
                recoverability  = Recoverability.IMPOSSIBLE)
        TIMEUNIT_OUTSIDE_BAR_LIMITS;
    }



    //======================================= FIELDS =============================================\\

    private final List<StackBuilder> builders;
    private final RythmicSignature   rythmicSign;

    private int     currBegTU;
    private int     currLenTU;
    private int     currVelocity;
    private boolean currIsTied;



    //======================================= METHODS ============================================\\

    //---------------------------------- PUBLIC CONSTRUCTOR --------------------------------------\\

    public BarBuilder() {
        this(RythmicSignature.STANDARD_4_4);
    }

    public BarBuilder(RythmicSignature rythmicSign) {
        this.builders = new ArrayList<>();
        for (int i = 0; i < rythmicSign.timeunitsInABar(); ++i) {
            builders.add(createSubmodule(StackBuilder.class));
        }
        this.rythmicSign = rythmicSign;
    }


    //----------------------------------------- ADD ----------------------------------------------\\

    public BarBuilder add(Pitch pitch, int velocity, int begTU, int lengthTU, boolean isTied) {
        checkTimeunit(begTU, "beginning");
        checkTimeunit(begTU + lengthTU, "ending");

        for (Value value : splitIntoValues(begTU, begTU + lengthTU)) {
            String noteStr = Note.valueOf(pitch, value, isTied).toCompleteString();
            log(Level.TRACE, "Adding note %4s at timeunit %4d", noteStr, begTU);

            builders.get(begTU).add(pitch, value, velocity, isTied);

            for (int j = begTU + 1; j < value.toTimeunit(); ++j) {
                builders.get(j).addStarted(pitch, value, velocity, isTied);
            }
            begTU += value.toTimeunit();
        }
        return this;
    }

    private int checkTimeunit(int timeunit, String tuName) {
        if (timeunit < 0) {
            signal(AN.TIMEUNIT_OUTSIDE_BAR_LIMITS, "A negative "
                    + "%s timeunit (%d) was set back to 0", tuName, timeunit);
            return 0;
        }
        if (timeunit >= rythmicSign.timeunitsInABar()) {
            signal(AN.TIMEUNIT_OUTSIDE_BAR_LIMITS, "A %s timeunit "
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

    public BarBuilder add(Pitch pitch, int begTU, int lengthTU) {
        return add(pitch, currVelocity, begTU, lengthTU, currIsTied);
    }

    public BarBuilder add(Pitch pitch, int begTU) {
        return add(pitch, currVelocity, begTU, currLenTU, currIsTied);
    }

    public BarBuilder add(Pitch pitch) {
        return add(pitch, currVelocity, currBegTU, currLenTU, currIsTied);
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

    public BarBuilder isTied(boolean isTied) {
        this.currIsTied = isTied;
        return this;
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
