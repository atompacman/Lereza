package com.atompacman.lereza.core.piece2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.atompacman.lereza.core.piece.timeline.TimeunitToBarConverter;
import com.atompacman.lereza.core.theory.Pitch;
import com.atompacman.toolkat.annotations.Implement;
import com.atompacman.toolkat.task.TaskLogger;

public abstract class PartBuilder<T extends Part<K>, K extends Bar<?>> 
    extends Builder<T> {

    //
    //  ~  FIELDS  ~  //
    //

    // Sub-builders
    private final List<BarBuilder<K,?>> builders;

    // Lifetime
    private final TimeunitToBarConverter tuToBar;

    // Temporary builder parameters
    private int  currBegTU;
    private int  currLenTU;
    private byte currVelocity;


    //
    //  ~  INIT  ~  //
    //

    protected PartBuilder(TimeunitToBarConverter tuToBar, Optional<TaskLogger> taskLogger) {
        super(taskLogger);

        // Sub-builders
        this.builders = new ArrayList<>();

        // Lifetime
        this.tuToBar = tuToBar;

        // Temporary builder parameters
        reset();
    }


    //
    //  ~  ADD  ~  //
    //

    public PartBuilder<T,K> add(Pitch pitch, byte velocity, int begTU, int lengthTU) {
        int tuInBar = tuToBar.getBarLengthTU(begTU);
        int barPosTU = begTU - tuToBar.convertBarToTu(tuToBar.convertTuToBar(begTU));
        int actualLen = lengthTU;

        // Check if note spans more than one bar
        if (barPosTU + lengthTU > tuInBar) {
            actualLen = tuInBar - barPosTU;
        }

        // Add first untied note
        log(begTU, actualLen);
        Note untiedNote = builderAt(begTU).add(pitch, velocity, barPosTU, actualLen, null);
        lengthTU -= actualLen;

        // Add tied notes
        while (lengthTU != 0) {
            begTU += actualLen;
            tuInBar = tuToBar.getBarLengthTU(begTU);
            if (lengthTU > tuInBar) {
                actualLen = tuInBar;
            } else {
                actualLen = lengthTU;
            }
            log(begTU, actualLen);
            untiedNote = builderAt(begTU).add(pitch, velocity, 0, actualLen, untiedNote);
            lengthTU -= actualLen;
        }

        return this;
    }

    public PartBuilder<T,K> add(Pitch pitch, int begTU, int lengthTU) {
        return add(pitch, currVelocity, begTU, lengthTU);
    }

    public PartBuilder<T,K> add(Pitch pitch, int begTU) {
        return add(pitch, currVelocity, begTU, currLenTU);
    }

    public PartBuilder<T,K> add(Pitch pitch) {
        return add(pitch, currVelocity, currBegTU, currLenTU);
    }

    public PartBuilder<T,K> pos(int timeunit) {
        this.currBegTU = timeunit;
        return this;
    }

    public PartBuilder<T,K> length(int noteLenTU) {
        this.currLenTU = noteLenTU;
        return this;
    }

    public PartBuilder<T,K> velocity(byte velocity) {
        this.currVelocity = velocity;
        return this;
    }

    private BarBuilder<K,?> builderAt(int timeunit) {
        int bar = tuToBar.convertTuToBar(timeunit);
        for (int i = builders.size(); i <= bar; ++i) {
            builders.add(instantiateSubBuilder(tuToBar.getBarLengthTUFromBar(i), taskLogger));
        }
        return builders.get(bar);
    }

    protected abstract BarBuilder<K,?> instantiateSubBuilder(int barLengthTU,TaskLogger taskLogger);

    private void log(int begTU, int lengthTU) {
        taskLogger.log(1, "%37s | Bar: %3d | Beg: %4d | End: %4d |", "", 
                tuToBar.convertTuToBar(begTU), begTU, begTU + lengthTU);
    }


    //
    //  ~  BUILD  ~  //
    //

    @Implement
    public T buildStructure() {
        List<K> bars = new LinkedList<>();
        for (BarBuilder<K, ?> builder : builders) {
            bars.add(builder.build());
        }
        return instantiateStructure(bars);
    }

    protected abstract T instantiateStructure(List<K> bars);

    @Implement
    protected void reset() {
        currBegTU    = 0;
        currLenTU    = 32;
        currVelocity = 100;
        builders.clear();
    }
}
