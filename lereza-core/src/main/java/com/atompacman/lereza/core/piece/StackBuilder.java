package com.atompacman.lereza.core.piece;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.atompacman.lereza.core.solfege.Dynamic;
import com.atompacman.lereza.core.solfege.Pitch;
import com.atompacman.lereza.core.solfege.Value;
import com.atompacman.toolkat.module.AnomalyDescription;
import com.atompacman.toolkat.module.AnomalyDescription.Severity;

public class StackBuilder extends PieceComponentBuilder<Stack<Note>> {

    //====================================== CONSTANTS ===========================================\\

    static final int         DEFAULT_VELOCITY = 100;
    static final List<Value> DEFAULT_VALUES   = Arrays.asList(Value.QUARTER);



    //===================================== INNER TYPES ==========================================\\

    private enum Anomaly {

        @AnomalyDescription (
                name            = "Multiple note entry for same pitch",
                detailsFormat   = "A note of pitch \"%s\" was already added to the %s note stack",
                description     = "More than one note has been registered for a specific pitch",
                consequences    = "Ignoring potentially important data", 
                severity        = Severity.MINIMAL)
        MULTIPLE_NOTE_ENTRY_FOR_SAME_PITCH,

        @AnomalyDescription (
                name            = "Note velocity not between 0 and 127", 
                description     = "The velocity of a note is not within the range of a byte",
                consequences    = "Velocity is brought back in the interval", 
                severity        = Severity.MINIMAL)
        VELOCITY_NOT_BETWEEN_0_AND_127;
    }



    //======================================= FIELDS =============================================\\

    private final List<Note>    startingNotes;
    private final List<Note>    startedNotes;
    private final List<Integer> velocities;

    private List<Value> currValues;
    private int         currVelocity;
    private boolean     currIsStarting;


    //======================================= METHODS ============================================\\

    //---------------------------------- PUBLIC CONSTRUCTOR --------------------------------------\\

    public StackBuilder() {
        this.startingNotes  = new LinkedList<>();
        this.startedNotes   = new LinkedList<>();
        this.velocities     = new LinkedList<>();

        this.currValues     = DEFAULT_VALUES;
        this.currVelocity   = DEFAULT_VELOCITY;
        this.currIsStarting = true;
    }


    //----------------------------------------- ADD ----------------------------------------------\\

    public StackBuilder add(Note note, int velocity, boolean isStarting) {
        if (isStarting) {
            startingNotes.add(note);
            velocities.add(velocity);
        } else {
            startedNotes.add(note);
        }
        return this;
    }

    public StackBuilder add(Pitch pitch, List<Value> values, int velocity, boolean isStarting) {
        return add(Note.valueOf(pitch, values), velocity, isStarting);
    }

    public StackBuilder add(Note note) {
        return add(note, currVelocity, currIsStarting);
    }

    public StackBuilder add(Pitch pitch) {
        return add(Note.valueOf(pitch, currValues), currVelocity, currIsStarting);
    }

    public StackBuilder add(String pitch) {
        return add(Note.valueOf(pitch, currValues), currVelocity, currIsStarting);
    }

    public StackBuilder values(List<Value> values) {
        currValues = values;
        return this;
    }

    public StackBuilder value(Value value) {
        currValues = Arrays.asList(value);
        return this;
    }

    public StackBuilder velocity(int velocity) {
        this.currVelocity = velocity;
        return this;
    }

    public StackBuilder isStarting(boolean isStarting) {
        this.currIsStarting = isStarting;
        return this;
    }


    //---------------------------------------- BUILD ---------------------------------------------\\

    protected Stack<Note> buildComponent() {
        // Create starting notes map
        Map<Pitch, Note> startingNotesMap = new LinkedHashMap<>();
        for (Note note : startingNotes) {
            if (startingNotesMap.put(note.getPitch(), note) != null) {
                signal(Anomaly.MULTIPLE_NOTE_ENTRY_FOR_SAME_PITCH, note.getPitch(), "starting");
            }
        }

        // Create started notes map
        Map<Pitch, Note> startedNotesMap = new LinkedHashMap<>();
        for (Note note : startedNotes) {
            if (startedNotesMap.put(note.getPitch(), note) != null) {
                signal(Anomaly.MULTIPLE_NOTE_ENTRY_FOR_SAME_PITCH, note.getPitch(), "started");
            }
            if (startingNotesMap.remove(note.getPitch()) != null) {
                signal(Anomaly.MULTIPLE_NOTE_ENTRY_FOR_SAME_PITCH, note.getPitch(), "starting");
            }
        }

        Dynamic dynamic = null;

        if (!startingNotesMap.isEmpty()) {
            // Compute note stack overall dynamic
            int velocitySum = 0;
            for (int velocity : velocities) {
                if (velocity < 0) {
                    velocity = 0;
                    signal(Anomaly.VELOCITY_NOT_BETWEEN_0_AND_127, "Negative note velocity");
                }
                if (velocity >= 128) {
                    velocity = 127;
                    signal(Anomaly.VELOCITY_NOT_BETWEEN_0_AND_127, "Note velocity above 127");
                }
                velocitySum += velocity;
            }
            double avg = (double) velocitySum / (double) velocities.size();
            dynamic = Dynamic.valueOf((byte) Math.rint(avg));
        }

        // Create definitive stack data structure
        return new Stack<Note>(startingNotesMap, startedNotesMap, dynamic);
    }


    //---------------------------------------- RESET ---------------------------------------------\\

    public void reset() {
        startingNotes.clear();
        startedNotes.clear();
    }
}
