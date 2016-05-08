package com.atompacman.lereza.core.piece;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.atompacman.lereza.core.piece.timeline.InfiniteTimeunitToBarConverter;
import com.atompacman.lereza.core.piece.timeline.TimeunitToBarConverter;
import com.atompacman.lereza.core.theory.Dynamic;
import com.atompacman.lereza.core.theory.Note;
import com.atompacman.lereza.core.theory.Pitch;
import com.atompacman.lereza.core.theory.RythmnValue;
import com.atompacman.lereza.core.theory.TimeSignature;
import com.atompacman.toolkat.Builder;
import com.atompacman.toolkat.annotations.Implement;
import com.atompacman.toolkat.task.Anomaly.Description;
import com.atompacman.toolkat.task.TaskLogger;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public final class PartBuilder extends Builder<PolyphonicPart> {
    
    //
    //  ~  FIELDS  ~  //
    //

    // Lifetime
    private final TimeunitToBarConverter tuToBar;

    // Data for build
    private final Multimap<Integer, NoteEntry> entries;

    // Temporary builder parameters
    private int  currBegTU;
    private int  currLenTU;
    private byte currVelocity;


    //
    //  ~  INIT  ~  //
    //

    public static PartBuilder of() {
        return of(InfiniteTimeunitToBarConverter.of());
    }
    
    public static PartBuilder of(TimeSignature timeSign) {
        return of(InfiniteTimeunitToBarConverter.of(timeSign));
    }
    
    public static PartBuilder of(int pieceLengthTU) {
        return of(TimeunitToBarConverter.of(pieceLengthTU));
    }
    
    public static PartBuilder of(TimeunitToBarConverter tuToBar) {
        return new PartBuilder(tuToBar, Optional.empty());
    }
    
    public static PartBuilder of(TimeunitToBarConverter tuToBar, TaskLogger taskLogger) {
        return new PartBuilder(tuToBar, Optional.of(taskLogger));
    }
    
    private PartBuilder(TimeunitToBarConverter tuToBar, Optional<TaskLogger> taskLogger) {
        super(taskLogger);

        // Lifetime
        this.tuToBar = tuToBar;

        // Data for build
        this.entries = ArrayListMultimap.create();

        // Temporary builder parameters
        reset();
    }


    //
    //  ~  ADD  ~  //
    //

    public PartBuilder add(Pitch pitch, byte velocity, int begTU, int lengthTU) {
        entries.put(begTU, new NoteEntry(pitch, velocity, lengthTU));
        return this;
    }
    
    public PartBuilder add(Pitch pitch, int begTU, int lengthTU) {
        return add(pitch, currVelocity, begTU, lengthTU);
    }

    public PartBuilder add(Pitch pitch, int begTU) {
        return add(pitch, currVelocity, begTU, currLenTU);
    }

    public PartBuilder add(Pitch pitch) {
        return add(pitch, currVelocity, currBegTU, currLenTU);
    }

    public PartBuilder pos(int timeunit) {
        this.currBegTU = timeunit;
        return this;
    }

    public PartBuilder length(int noteLenTU) {
        this.currLenTU = noteLenTU;
        return this;
    }

    public PartBuilder velocity(byte velocity) {
        this.currVelocity = velocity;
        return this;
    }

    
    //
    //  ~  BUILD  ~  //
    //

    @Implement
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected PolyphonicPart buildImpl() {
        ComplexityHierarchyRank minComplex = ComplexityHierarchyRank.MONOPHONIC;
        
        List<PolyphonicBar> bars = new LinkedList<>();
        for (BarBuilder builder : builders) {
            PolyphonicBar bar = builder.build();
            
            ComplexityHierarchyRank rank = bar.getComplexityHierarchyRank();
            if (rank.ordinal() < minComplex.ordinal()) {
                minComplex = rank;
            }
            
            bars.add(bar);
        }
        
        switch (minComplex) {
        case POLYPHONIC: return new PolyphonicPart(bars);
        case HOMOPHONIC: return new HomophonicPart((List<HomophonicBar>) (List) bars);
        case MONOPHONIC: return new MonophonicPart((List<MonophonicBar>) (List) bars);
        default: throw new RuntimeException(); 
        }
    }
    

    public PartBuilder add2(Pitch pitch, byte velocity, int begTU, int lengthTU) {
        // Number of timeunits in the bar this note starts in
        int tuInBar = tuToBar.getBarLengthTU(begTU);
        
        // Timeunit position of note start in bar
        int barPosTU = begTU - tuToBar.convertBarToTu(tuToBar.convertTuToBar(begTU));
        
        // Timeunit length of note in bar (a note can span more than one bar)
        int actualLen = barPosTU + lengthTU > tuInBar ? tuInBar - barPosTU : lengthTU;

        // Add notes in first covered bar
        addNoteInBar(pitch, velocity, barPosTU, actualLen);
        
        // Add note in additional bars
        lengthTU -= actualLen;
        while (lengthTU != 0) {
            begTU += actualLen;
            tuInBar = tuToBar.getBarLengthTU(begTU);
            actualLen = lengthTU > tuInBar ? tuInBar : lengthTU;
            addNoteInBar(pitch, velocity, 0, actualLen);
            lengthTU -= actualLen;
        }

        return this;
    }
    
    private void addNoteInBar(Pitch pitch, byte velocity, int begTU, int lengthTU) {
        boolean isTied = false;
        
        Dynamic dynamic = Dynamic.of(velocity);
        
        // For each rythmic value
        for (RythmnValue value : splitIntoRythmicValues(begTU, lengthTU)) {
            // Create note
            Note note = Note.of(pitch, value, dynamic);
            
            // Add note entry
            entries.put(begTU, new TieableNote(note, isTied));
            
            // Progress to next note beginning
            begTU += value.toTimeunit();
            
            // All other notes are tied to the first one
            isTied = true;
        }
    }
    

    private void log(int begTU, int lengthTU) {
        taskLogger.log(1, "%37s | Bar: %3d | Beg: %4d | End: %4d |", "", 
                tuToBar.convertTuToBar(begTU), begTU, begTU + lengthTU);
    }

    @Implement
    public void reset() {
        entries.clear();
        
        currBegTU    = 0;
        currLenTU    = RythmnValue.QUARTER.toTimeunit();
        currVelocity = Dynamic.Marker.FORTE.getMinimumVelocity();
    }
}
