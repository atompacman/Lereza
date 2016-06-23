package com.atompacman.lereza.core.piece;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.atompacman.lereza.core.theory.Note;
import com.atompacman.lereza.core.theory.Pitch;
import com.atompacman.lereza.core.theory.RythmnValue;
import com.atompacman.toolkat.annotations.Implement;
import com.atompacman.toolkat.task.TaskLogger;

public final class BarSliceBuilder extends Builder<BarSlice<? extends NoteNode>> {

    //
    //  ~  CONSTANTS  ~  //
    //

    private static final int         DEFAULT_VELOCITY = 80;
    private static final RythmnValue DEFAULT_VALUE    = RythmnValue.QUARTER;

    
    //
    //  ~  FIELDS  ~  //
    //

    // Temporary builder data
    private List<AbstractNoteNode<?>> startingNodes;
    private List<AbstractNoteNode<?>> startedNodes;
    
    // Temporary builder parameters
    private RythmnValue currValue;
    private int         currVelocity;
    private boolean     currIsStarting;
    
    
    //
    //  ~  INIT  ~  //
    //

    public BarSliceBuilder(Optional<TaskLogger> taskLogger) {
        super(taskLogger);
        
        // Temporary builder data
        reset();
        
        // Temporary builder parameters
        this.currValue      = DEFAULT_VALUE;
        this.currVelocity   = DEFAULT_VELOCITY;
        this.currIsStarting = true;
    }
    
    
    //
    //  ~  ADD  ~  //
    //
        
    public BarSliceBuilder add(AbstractNoteNode<?> node, int velocity, boolean isStarting) {
        (isStarting && !node.isTied() ? startingNodes : startedNodes).add(node);
        return this;
    }

    public BarSliceBuilder add(Pitch pitch, RythmnValue value,int velocity,boolean isStarting){
        return add(Note.of(pitch, value), velocity, isStarting);
    }

    public BarSliceBuilder add(Note note) {
        return add(note, currVelocity, currIsStarting);
    }

    public BarSliceBuilder add(Pitch pitch) {
        return add(Note.of(pitch, currValue), currVelocity, currIsStarting);
    }

    public BarSliceBuilder add(String pitch) {
        return add(Note.of(pitch, currValue), currVelocity, currIsStarting);
    }

    public BarSliceBuilder value(RythmnValue value) {
        currValue = value;
        return this;
    }

    public BarSliceBuilder velocity(int velocity) {
        this.currVelocity = velocity;
        return this;
    }

    public BarSliceBuilder isStarting(boolean isStarting) {
        this.currIsStarting = isStarting;
        return this;
    }

    
    //
    //  ~  BUILD  ~  //
    //
    
    @Implement
    protected BarSlice<? extends NoteNode> buildImpl() {
        return null;
    }

    @Implement
    public void reset() {
        startingNodes = new LinkedList<>();
        startedNodes  = new LinkedList<>();
    }


}
