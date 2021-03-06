package com.atompacman.lereza.core.piece;

import com.atompacman.lereza.core.piece.timeline.TimeunitToBarConverter;
import com.atompacman.lereza.core.theory.Pitch;
import com.atompacman.lereza.core.theory.TimeSignature;
import com.atompacman.toolkat.Builder;
import com.atompacman.toolkat.annotations.Implement;
import com.atompacman.toolkat.task.AnomalyDescription;
import com.atompacman.toolkat.task.AnomalyDescription.Severity;
import com.atompacman.toolkat.task.TaskMonitor;

public final class BarBuilder extends Builder<PolyphonicBar> {
    
    //
    //  ~  INNER TYPES  ~  //
    //
    
    private enum Anomaly {
        
        @AnomalyDescription (name         = "Note is out of bar scope", 
                             consequences = "Ignoring notes on additional bars",
                             severity     = Severity.MODERATE)
        NOTE_OUT_OF_BAR
    }
    
    
    //
    //  ~  FIELDS  ~  //
    //

    private final PartBuilder builder;


    //
    //  ~  INIT  ~  //
    //

    public static BarBuilder of() {
        return new BarBuilder(TimeSignature.STANDARD_4_4);
    }
    
    public static BarBuilder of(TimeSignature timeSig) {
        return new BarBuilder(timeSig);
    }
    
    private BarBuilder(TimeSignature timeSig) {       
        TimeunitToBarConverter t2b = TimeunitToBarConverter.of(timeSig, timeSig.timeunitsInABar());
        this.builder = new PartBuilder(t2b);
    }


    //
    //  ~  ADD  ~  //
    //

    public BarBuilder add(Pitch pitch, byte velocity, int begTU, int lengthTU) {
        builder.add(pitch, velocity, begTU, lengthTU);
        return this;
    }

    public BarBuilder add(byte hexNote, byte velocity, int begTU, int lengthTU) {
        builder.add(hexNote, velocity, begTU, lengthTU);
        return this;
    }

    public BarBuilder add(Pitch pitch, int begTU, int lengthTU) {
        builder.add(pitch, begTU, lengthTU);
        return this;
    }

    public BarBuilder add(Pitch pitch, int begTU) {
        builder.add(pitch, begTU);
        return this;
    }

    public BarBuilder add(Pitch pitch) {
        builder.add(pitch);
        return this;
    }

    public BarBuilder pos(int timeunit) {
        builder.pos(timeunit);
        return this;
    }

    public BarBuilder length(int noteLenTU) {
        builder.length(noteLenTU);
        return this;
    }

    public BarBuilder velocity(byte velocity) {
        builder.velocity(velocity);
        return this;
    }


    //
    //  ~  BUILD  ~  //
    //

    @Implement
    protected PolyphonicBar buildImpl(TaskMonitor monitor) {
        PolyphonicPart part = builder.build(monitor);
        if (part.numBars() > 1) {
            monitor.signal(Anomaly.NOTE_OUT_OF_BAR);
        }
        return part.getBar(0);
    }
    
    @Implement
    public void reset() {
        builder.reset();
    }
}
