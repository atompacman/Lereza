package com.atompacman.lereza.core.piece2;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.atompacman.lereza.core.Builder;
import com.atompacman.lereza.core.theory.Pitch;
import com.atompacman.lereza.core.theory.TimeSignature;
import com.atompacman.toolkat.annotations.Implement;
import com.atompacman.toolkat.task.Anomaly.Description;
import com.atompacman.toolkat.task.TaskLogger;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public final class BarBuilder extends Builder<PolyphonicBar> {

    //
    //  ~  INNER TYPES  ~  //
    //

    final static class NoteEntry {
        
        //
        //  ~  FIELDS  ~  //
        //

        public final Pitch pitch; 
        public final byte  velocity;
        public final int   lengthTU;

        
        //
        //  ~  INIT  ~  //
        //

        public NoteEntry(Pitch pitch, byte velocity, int lengthTU) {
            this.pitch    = pitch;
            this.velocity = velocity;
            this.lengthTU = lengthTU;
        }
    }
    
    private enum Anomaly {
        
        @Description (name = "Simultaneous notes with the same pitch")
        SIMULTANEOUS_NOTES_WITH_SAME_PITCH,

        @Description (name = "Note beginning time out of bar scope")
        NOTE_BEG_OUT_OF_SCOPE;
    }
    
    
    //
    //  ~  FIELDS  ~  //
    //

    // Lifetime
    private final TimeSignature timeSig;
    
    // Data for build
    private final Multimap<Integer, NoteEntry> entries;
    
    // Temporary builder parameters
    private int  currBegTU;
    private int  currLenTU;
    private byte currVelocity;


    //
    //  ~  INIT  ~  //
    //

    public static BarBuilder of() {
        return new BarBuilder(TimeSignature.STANDARD_4_4, Optional.empty());
    }
    
    public static BarBuilder of(TimeSignature timeSig) {
        return new BarBuilder(timeSig, Optional.empty());
    }
    
    public static BarBuilder of(TimeSignature timeSig, TaskLogger taskLogger) {
        return new BarBuilder(timeSig, Optional.of(taskLogger));
    }
    
    private BarBuilder(TimeSignature timeSig, Optional<TaskLogger> taskLogger) {
        super(taskLogger);
        
        // Lifetime
        this.timeSig = timeSig;
        
        // Data for build
        this.entries = ArrayListMultimap.create();

        // Temporary builder parameters
        reset();
    }


    //
    //  ~  ADD  ~  //
    //

    public BarBuilder add(Pitch pitch, byte velocity, int begTU, int lengthTU) {
        entries.put(begTU, new NoteEntry(pitch, velocity, lengthTU));
        return this;
    }

    public BarBuilder add(byte hexNote, byte velocity, int begTU, int lengthTU) {
        return add(Pitch.thatIsMoreCommonForHexValue(hexNote), velocity, begTU, lengthTU);
    }

    public BarBuilder add(Pitch pitch, int begTU, int lengthTU) {
        return add(pitch, currVelocity, begTU, lengthTU);
    }

    public BarBuilder add(Pitch pitch, int begTU) {
        return add(pitch, currVelocity, begTU, currLenTU);
    }

    public BarBuilder add(Pitch pitch) {
        return add(pitch, currVelocity, currBegTU, currLenTU);
    }

    public BarBuilder pos(int timeunit) {
        this.currBegTU = timeunit;
        return this;
    }

    public BarBuilder length(int noteLenTU) {
        this.currLenTU = noteLenTU;
        return this;
    }

    public BarBuilder velocity(byte velocity) {
        this.currVelocity = velocity;
        return this;
    }


    //
    //  ~  BUILD  ~  //
    //

    @Implement
    public PolyphonicBar buildImpl() {
        
    }

    @Implement
    public void reset() {
        entries.clear();
        
        currBegTU    = 0;
        currLenTU    = PolyphonicNoteNode.DEFAULT_VALUE.toTimeunit();
        currVelocity = PolyphonicNoteNode.DEFAULT_DYNAMIC_MARKER.getMinimumVelocity();
    }
}
