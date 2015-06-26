package com.atompacman.lereza.core.piece;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.atompacman.lereza.core.solfege.Dynamic;
import com.atompacman.lereza.core.solfege.Pitch;
import com.atompacman.lereza.core.solfege.Value;
import com.atompacman.toolkat.module.Anomaly;
import com.atompacman.toolkat.module.Anomaly.Impact;
import com.atompacman.toolkat.module.Anomaly.Recoverability;

public class StackBuilder extends PieceComponentBuilder<Stack<Note>> {

    //====================================== CONSTANTS ===========================================\\

    static final int   DEFAULT_VELOCITY = 100;
    static final Value DEFAULT_VALUE    = Value.QUARTER;



    //===================================== INNER TYPES ==========================================\\

    private class NoteEntry {

        //===================================== FIELDS ===========================================\\

        public Pitch   pitch;
        public Value   value;
        public int     velocity;
        public boolean isTied;



        //===================================== METHODS ==========================================\\

        //-------------------------------- PUBLIC CONSTRUCTOR ------------------------------------\\

        public NoteEntry(Pitch pitch, Value value, int velocity, boolean isTied) {
            this.pitch    = pitch;
            this.value    = value;
            this.velocity = velocity;
            this.isTied   = isTied;
        }
    }

    private enum AN {

        @Anomaly (
                name            = "Multiple note entry for same pitch", 
                description     = "More than one note has been registered for a specific pitch",
                consequences    = "Ignoring potentially important data", 
                impact          = Impact.MINIMAL, 
                recoverability  = Recoverability.IMPOSSIBLE)
        MULTIPLE_NOTE_ENTRY_FOR_SAME_PITCH,
        
        @Anomaly (
                name            = "Note velocity not between 0 and 127", 
                description     = "The velocity of a note is not within the range of a byte",
                consequences    = "Velocity is brought back in the interval", 
                impact          = Impact.MINIMAL, 
                recoverability  = Recoverability.TRIVIAL)
        VELOCITY_NOT_BETWEEN_0_AND_127;
    }



    //======================================= FIELDS =============================================\\

    private final List<NoteEntry> startingNotes;
    private final List<NoteEntry> startedNotes;

    private Value   currValue;
    private int     currVelocity;
    private boolean currIsTied;



    //======================================= METHODS ============================================\\

    //---------------------------------- PUBLIC CONSTRUCTOR --------------------------------------\\

    public StackBuilder() {
        this.startingNotes  = new LinkedList<>();
        this.startedNotes   = new LinkedList<>();
        
        this.currValue      = DEFAULT_VALUE;
        this.currVelocity   = DEFAULT_VELOCITY;
        this.currIsTied     = false;
    }


    //----------------------------------------- ADD ----------------------------------------------\\

    public StackBuilder add(Pitch pitch, Value value, int velocity, boolean isTied) {
        startingNotes.add(new NoteEntry(pitch, value, velocity, isTied));
        return this;
    }

    public StackBuilder add(Pitch pitch) {
        return add(pitch, currValue, currVelocity, currIsTied);
    }

    public StackBuilder addStarted(Pitch pitch, Value value, int velocity, boolean isTied) {
        startedNotes.add(new NoteEntry(pitch, value, velocity, isTied));
        return this;
    }

    public StackBuilder addStarted(Pitch pitch) {
        return addStarted(pitch, currValue, currVelocity, currIsTied);
    }

    public StackBuilder value(Value value) {
        this.currValue = value;
        return this;
    }

    public StackBuilder velocity(int velocity) {
        this.currVelocity = velocity;
        return this;
    }

    public StackBuilder isTied(boolean isTied) {
        this.currIsTied = isTied;
        return this;
    }


    //---------------------------------------- BUILD ---------------------------------------------\\

    protected Stack<Note> buildComponent() {
        // Create starting notes map
        Map<Pitch, NoteEntry> startingNotesMap = new HashMap<>();
        for (NoteEntry startingEntry : startingNotes) {
            putEntryInMap(startingEntry, startingNotesMap, "starting");
        }
        
        // Create started notes map
        Map<Pitch, NoteEntry> startedNotesMap = new HashMap<>();
        for (NoteEntry startedEntry : startedNotes) {
            putEntryInMap(startedEntry, startedNotesMap, "started");
            if (startingNotesMap.remove(startedEntry.pitch) != null) {
                signal(AN.MULTIPLE_NOTE_ENTRY_FOR_SAME_PITCH, "A started note of pitch " + 
                    startedEntry.pitch.toString() +" was already added to the starting note stack");
            }
        }
        
        Dynamic dynamic = null;
        
        if (!startingNotesMap.isEmpty()) {
            // Compute note stack overall dynamic
            int velocitySum = 0;
            for (NoteEntry entry : startingNotesMap.values()) {
                if (entry.velocity < 0) {
                    entry.velocity = 0;
                    signal(AN.VELOCITY_NOT_BETWEEN_0_AND_127, "Negative note velocity");
                }
                if (entry.velocity >= 128) {
                    entry.velocity = 127;
                    signal(AN.VELOCITY_NOT_BETWEEN_0_AND_127, "Note velocity above 127");
                }
                velocitySum += entry.velocity;
            }
            double avg = (double) velocitySum / (double) startingNotesMap.size();
            dynamic = Dynamic.valueOf((byte) Math.rint(avg));
        }
        
        // Create definitive stack data structure
        return new Stack<Note>(toNoteMap(startingNotesMap), toNoteMap(startedNotesMap), dynamic);
    }

    private void putEntryInMap(NoteEntry entry, Map<Pitch, NoteEntry> map, String mapName) {
        if (map.put(entry.pitch, entry) != null) {
            signal(AN.MULTIPLE_NOTE_ENTRY_FOR_SAME_PITCH, "A note of pitch " + entry.pitch 
                    + " was already added to the " + mapName + " note stack");
        }
    }

    private static Map<Pitch, Note> toNoteMap(Map<Pitch, NoteEntry> entries) {
        Map<Pitch, Note> notes = new HashMap<>();
        for (NoteEntry entry : entries.values()) {
            notes.put(entry.pitch, Note.valueOf(entry.pitch, entry.value, entry.isTied));
        }
        return notes;
    }


    //---------------------------------------- RESET ---------------------------------------------\\

    public void reset() {
        startingNotes.clear();
        startedNotes.clear();
    }
}
