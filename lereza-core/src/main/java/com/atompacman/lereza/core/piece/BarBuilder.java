package com.atompacman.lereza.core.piece;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

import com.atompacman.lereza.core.solfege.Pitch;
import com.atompacman.lereza.core.solfege.TimeSignature;
import com.atompacman.lereza.core.solfege.Value;
import com.atompacman.toolkat.module.AnomalyDescription;
import com.atompacman.toolkat.module.AnomalyDescription.Severity;

public class BarBuilder extends PieceComponentBuilder<Bar> {

    //===================================== INNER TYPES ==========================================\\

    private enum Anomaly {

        @AnomalyDescription (
                name         = "Timeunit outside bar limits",
                description  = "A note beginning or ending timeunit was outside bar limits",
                consequences = "Timeunit is brought back within the limits", 
                severity     = Severity.MODERATE)
        TIMEUNIT_OUTSIDE_BAR_LIMITS,
        
        @AnomalyDescription (
                name         = "Tied note not added at bar's beginning",
                description  = "A tied note was added elsewhere than bar's beginning",
                consequences = "Note is not tied", 
                severity     = Severity.MODERATE)
        TIED_NOTE_NOT_AT_BEG_OF_BAR;
    }



    //======================================= FIELDS =============================================\\

    private final List<NoteStackBuilder> builders;
    private final TimeSignature          timeSign;

    private int currBegTU;
    private int currLenTU;
    private int currVelocity;



    //======================================= METHODS ============================================\\

    //---------------------------------- PUBLIC CONSTRUCTOR --------------------------------------\\

    public BarBuilder() {
        this(TimeSignature.STANDARD_4_4);
    }

    public BarBuilder(TimeSignature timeSign) {
        this.builders = new ArrayList<>();
        this.timeSign = timeSign;

        this.currBegTU = 0;
        this.currLenTU = Value.QUARTER.toTimeunit();
        this.currVelocity = NoteStackBuilder.DEFAULT_VELOCITY;
        
        // Initialize stack builders
        for (int i = 0 ; i < timeSign.timeunitsInABar(); ++i) {
            NoteStackBuilder builder = new NoteStackBuilder();
            registerSubmodule(builder);
            builders.add(builder);
        }
    }


    //----------------------------------------- ADD ----------------------------------------------\\

    Note add(Pitch pitch, int velocity, int begTU, int lengthTU, Note beforeTie) {
        // Check that note timeunit range is within bar limits
        int endTU = begTU + lengthTU;
        checkTimeunit(begTU, "beginning");
        checkTimeunit(endTU, "ending");

        // Check that a tied note is always at the beginning of the bar
        if (beforeTie != null && begTU != 0) {
            signal(Anomaly.TIED_NOTE_NOT_AT_BEG_OF_BAR);
            beforeTie = null;
        }
        
        // Split note timeunit range into simple values
        for (Value value : splitIntoValues(begTU, endTU)) {
            // Create note
            Note note = Note.valueOf(pitch, value);
            endTU = begTU + value.toTimeunit();
            
            // Tie note if needed
            if (beforeTie != null) {
                beforeTie.tieTo(note);
            }

            // Log
            log(Level.TRACE, "Adding note %-16s from timeunit %4d to %4d", 
                    note.toStaccato((byte)velocity), begTU, endTU);
            
            // Add starting note to the first note stack
            builders.get(begTU).add(note, velocity, true);

            // Add started note to the other stacks
            for (int i = begTU + 1; i < endTU; ++i) {
                builders.get(i).add(note, velocity, false);
            }
            
            // Update note beginning timeunit and note before tie
            begTU += value.toTimeunit();
            beforeTie = note;
        }

        return beforeTie;
    }

    public BarBuilder add(Pitch pitch, int velocity, int begTU, int lengthTU) {
        add(pitch, velocity, begTU, lengthTU, null);
        return this;
    }

    public BarBuilder add(Pitch pitch) {
        add(pitch, currVelocity, currBegTU, currLenTU, null);
        return this;
    }

    public BarBuilder add(String pitch) {
        add(Pitch.valueOf(pitch), currVelocity, currBegTU, currLenTU, null);
        return this;
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

    private int checkTimeunit(int timeunit, String tuName) {
        if (timeunit < 0) {
            signal(Anomaly.TIMEUNIT_OUTSIDE_BAR_LIMITS, "A negative "
                    + "%s timeunit (%d) was set back to 0", tuName, timeunit);
            return 0;
        }
        if (timeunit > timeSign.timeunitsInABar()) {
            signal(Anomaly.TIMEUNIT_OUTSIDE_BAR_LIMITS, "A %s timeunit "
                    + "(%d) that was greater than bar length (%d) was brought down to "
                    + "end of bar", tuName, timeunit, timeSign.timeunitsInABar());
            return timeSign.timeunitsInABar();
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

    protected Bar buildComponent() {
        List<NoteStack> noteStacks = new ArrayList<>();
        for (NoteStackBuilder builder : builders) {
            noteStacks.add(builder.build());
        }
        return new Bar(noteStacks, timeSign);
    }


    //---------------------------------------- RESET ---------------------------------------------\\

    protected void reset() {
        builders.clear();
    }
}
