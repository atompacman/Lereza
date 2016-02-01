package com.atompacman.lereza.core.piece;

import java.util.LinkedList;
import java.util.List;

import com.atompacman.lereza.core.piece.AbstractNoteStack.NoteStatus;
import com.atompacman.lereza.core.solfege.Dynamic;
import com.atompacman.lereza.core.solfege.Pitch;
import com.atompacman.lereza.core.solfege.Value;
import com.atompacman.toolkat.module.AnomalyDescription;
import com.atompacman.toolkat.module.BaseModule;
import com.atompacman.toolkat.module.AnomalyDescription.Severity;

public abstract class AbstractNoteStackBuilder<T extends AbstractNoteStack> 
extends PieceComponentBuilder<T> {

    //====================================== CONSTANTS ===========================================\\

    static final int   DEFAULT_VELOCITY = 80;
    static final Value DEFAULT_VALUE    = Value.QUARTER;



    //===================================== INNER TYPES ==========================================\\

    private enum BaseAnomaly {
        
        @AnomalyDescription (
                name         = "Note velocity not between 0 and 127", 
                description  = "The velocity of a note is not within the range of a byte",
                consequences = "Velocity is brought back in the interval", 
                severity     = Severity.MINIMAL)
        VELOCITY_NOT_BETWEEN_0_AND_127;
    }



    //=================================== ABSTRACT METHODS =======================================\\

    protected abstract void addNote(Note note, NoteStatus status);
    protected abstract T buildComponent(Dynamic dynamic);
    protected abstract void resetChild();
    
    
    
    //======================================= FIELDS =============================================\\

    private List<Integer> velocities;

    private Value   currValue;
    private int     currVelocity;
    private boolean currIsStarting;



    //======================================= METHODS ============================================\\

    //------------------------------------- CONSTRUCTORS -----------------------------------------\\

    protected AbstractNoteStackBuilder(BaseModule parentModule) {
        super(parentModule);
        
        this.velocities     = new LinkedList<>();

        this.currValue      = DEFAULT_VALUE;
        this.currVelocity   = DEFAULT_VELOCITY;
        this.currIsStarting = true;
    }


    //----------------------------------------- ADD ----------------------------------------------\\

    public AbstractNoteStackBuilder<T> add(Note note, int velocity, boolean isStarting) {
        NoteStatus status = note.isTied() ? 
            isStarting ? NoteStatus.STARTING_AND_TIED   : NoteStatus.STARTED_AND_TIED :
            isStarting ? NoteStatus.STARTING_AND_UNTIED : NoteStatus.STARTED_AND_UNTIED;
        addNote(note, status);
        velocities.add(velocity);
        return this;
    }

    public AbstractNoteStackBuilder<T> add(Pitch pitch,Value value,int velocity,boolean isStarting){
        return add(Note.valueOf(pitch, value), velocity, isStarting);
    }

    public AbstractNoteStackBuilder<T> add(Note note) {
        return add(note, currVelocity, currIsStarting);
    }

    public AbstractNoteStackBuilder<T> add(Pitch pitch) {
        return add(Note.valueOf(pitch, currValue), currVelocity, currIsStarting);
    }

    public AbstractNoteStackBuilder<T> add(String pitch) {
        return add(Note.valueOf(pitch, currValue), currVelocity, currIsStarting);
    }

    public AbstractNoteStackBuilder<T> value(Value value) {
        currValue = value;
        return this;
    }

    public AbstractNoteStackBuilder<T> velocity(int velocity) {
        this.currVelocity = velocity;
        return this;
    }

    public AbstractNoteStackBuilder<T> isStarting(boolean isStarting) {
        this.currIsStarting = isStarting;
        return this;
    }


    //---------------------------------------- BUILD ---------------------------------------------\\

    protected T buildComponent() {
        // Compute note stack overall dynamic
        Dynamic dynamic = null;

        if (!velocities.isEmpty()) {
            int velocitySum = 0;
            for (int velocity : velocities) {
                if (velocity < 0) {
                    velocity = 0;
                    signal(BaseAnomaly.VELOCITY_NOT_BETWEEN_0_AND_127, "Negative note velocity");
                }
                if (velocity >= 128) {
                    velocity = 127;
                    signal(BaseAnomaly.VELOCITY_NOT_BETWEEN_0_AND_127, "Note velocity above 127");
                }
                velocitySum += velocity;
            }
            double avg = (double) velocitySum / (double) velocities.size();
            dynamic = Dynamic.valueOf((byte) Math.rint(avg));
        }
        
        // Create definitive stack data structure
        return buildComponent(dynamic);
    }

    
    //---------------------------------------- RESET ---------------------------------------------\\

    protected void reset() {
        velocities.clear();
        resetChild();
    }
}
