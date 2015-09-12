package com.atompacman.lereza.core.piece;

import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.atompacman.lereza.core.piece.Stack.NoteStatus;
import com.atompacman.lereza.core.solfege.Dynamic;
import com.atompacman.lereza.core.solfege.Pitch;
import com.atompacman.lereza.core.solfege.Value;
import com.atompacman.toolkat.module.AnomalyDescription;
import com.atompacman.toolkat.module.AnomalyDescription.Severity;

public class StackBuilder extends PieceComponentBuilder<Stack<TiedNote>> {

    //====================================== CONSTANTS ===========================================\\

    static final int   DEFAULT_VELOCITY = 80;
    static final Value DEFAULT_VALUE    = Value.QUARTER;



    //===================================== INNER TYPES ==========================================\\

    private enum Anomaly {

        @AnomalyDescription (
                name            = "Multiple note entry for same pitch",
                detailsFormat   = "Multiple notes with same pitch were added to the note stack",
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

    private List<TiedNote> startingNotes;
    private List<TiedNote> startedNotes;
    private List<Integer>  velocities;

    private Value   currValue;
    private int     currVelocity;
    private boolean currIsStarting;



    //======================================= METHODS ============================================\\

    //---------------------------------- PUBLIC CONSTRUCTOR --------------------------------------\\

    public StackBuilder() {
        this.startingNotes  = new LinkedList<>();
        this.startedNotes   = new LinkedList<>();
        this.velocities     = new LinkedList<>();

        this.currValue      = DEFAULT_VALUE;
        this.currVelocity   = DEFAULT_VELOCITY;
        this.currIsStarting = true;
    }


    //----------------------------------------- ADD ----------------------------------------------\\

    public StackBuilder add(TiedNote note, int velocity, boolean isStarting) {
        if (isStarting) {
            startingNotes.add(note);
            velocities.add(velocity);
        } else {
            startedNotes.add(note);
        }
        return this;
    }

    public StackBuilder add(Pitch pitch, Value value, int velocity, boolean isStarting) {
        return add(TiedNote.valueOf(pitch, value), velocity, isStarting);
    }

    public StackBuilder add(TiedNote note) {
        return add(note, currVelocity, currIsStarting);
    }

    public StackBuilder add(Pitch pitch) {
        return add(TiedNote.valueOf(pitch, currValue), currVelocity, currIsStarting);
    }

    public StackBuilder add(String pitch) {
        return add(TiedNote.valueOf(pitch, currValue), currVelocity, currIsStarting);
    }

    public StackBuilder value(Value value) {
        currValue = value;
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

    protected Stack<TiedNote> buildComponent() {
        // Create notes by status structure
        Map<NoteStatus, Set<TiedNote>> notesByStatus = createEmptyNoteByStatusStructure();

        // Add notes to structure
        for (TiedNote note : startingNotes) {
            notesByStatus.get(note.isTied() ? NoteStatus.STARTING_AND_TIED : 
                NoteStatus.STARTING_AND_UNTIED).add(note);
        }
        for (TiedNote note : startedNotes) {
            notesByStatus.get(note.isTied() ? NoteStatus.STARTED_AND_TIED : 
                NoteStatus.STARTED_AND_UNTIED).add(note);
        }

        // Compute note stack overall dynamic
        Dynamic dynamic = null;

        if (!startingNotes.isEmpty()) {
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
        try {
            return new Stack<TiedNote>(notesByStatus, dynamic);
        } catch (IllegalArgumentException e) {
            signal(Anomaly.MULTIPLE_NOTE_ENTRY_FOR_SAME_PITCH);
        }

        return new Stack<TiedNote>(createEmptyNoteByStatusStructure(), null);
    }
    
    private static Map<NoteStatus, Set<TiedNote>> createEmptyNoteByStatusStructure() {
        Map<NoteStatus, Set<TiedNote>> notesByStatus = new EnumMap<>(NoteStatus.class);
        for (NoteStatus status : NoteStatus.values()) {
            notesByStatus.put(status, new LinkedHashSet<>());
        }
        return notesByStatus;
    }


    //---------------------------------------- RESET ---------------------------------------------\\

    public void reset() {
        startingNotes.clear();
        startedNotes.clear();
        velocities.clear();
    }
}
