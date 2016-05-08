package com.atompacman.lereza.core.piece;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeSet;

import com.atompacman.lereza.core.piece.PolyphonicNoteNode.TiedNoteStatus;
import com.atompacman.lereza.core.theory.Dynamic;
import com.atompacman.lereza.core.theory.Note;
import com.atompacman.lereza.core.theory.Pitch;
import com.atompacman.lereza.core.theory.RythmnValue;
import com.atompacman.lereza.core.theory.TimeSignature;
import com.atompacman.toolkat.Builder;
import com.atompacman.toolkat.annotations.Implement;
import com.atompacman.toolkat.task.Anomaly.Description;
import com.atompacman.toolkat.task.Anomaly.Severity;
import com.atompacman.toolkat.task.TaskLogger;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Range;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public final class BarBuilder extends Builder<PolyphonicBar> {

    //
    //  ~  INNER TYPES  ~  //
    //

    private enum NoteSection {
        BEGINNING,
        MIDDLE,
        END;
    }
    
    private static final class NoteEntry {
        
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
    
    private static class LayoutEntry {
        
        //
        //  ~  FIELDS  ~  //
        //
        
        public final NoteSection section;
        public final NoteEntry   noteEntry;
        
        
        //
        //  ~  INIT  ~  //
        //
        
        public LayoutEntry(NoteSection section, NoteEntry noteEntry) {
            this.section   = section;
            this.noteEntry = noteEntry;
        }
    }

    private enum Anomaly {
        
        @Description (name          = "Note is out of bar scope", 
                      detailsFormat = "Beg: %d, End: %d ",
                      consequences  = "Ignoring note",
                      severity      = Severity.MODERATE)
        NOTE_OUT_OF_SCOPE,
        
        @Description (name          = "Note timeunit length is not positive", 
                      detailsFormat = "Length: %d",
                      consequences  = "Ignoring note",
                      severity      = Severity.MODERATE)
        NOTE_LENGTH_NOT_POSITIVE,

        @Description (name = "Simultaneous notes with the same pitch")
        SIMULTANEOUS_NOTES_WITH_SAME_PITCH;
    }
    
    
    //
    //  ~  FIELDS  ~  //
    //

    // Lifetime
    private final TimeSignature timeSig;
    
    // Data for build
    private Multimap<Integer, NoteEntry> entries;

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
        this.entries = LinkedListMultimap.create();

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
    protected PolyphonicBar buildImpl() {
        // Determine timeunit range of added notes (they can begin / end outside the bar)
        Range<Integer> tuRange = determineTimeunitRange();
        
        // Determine note sections layout
        List<List<LayoutEntry>> layout = determineLayout(tuRange);
        
        // Find if bar is mono/homo/polyphonic (if timeunit range is unbounded, then bar is empty)
        ComplexityHierarchyRank rank = tuRange.hasLowerBound() 
                ? findComplexityRank(layout, tuRange.lowerEndpoint()) 
                : ComplexityHierarchyRank.MONOPHONIC;
                
        // Build with appropriate complexity
        switch (rank) {
        case MONOPHONIC : return buildMonophonicBar();
        case HOMOPHONIC : return buildHomophonicBar(layout);
        case POLYPHONIC : return buildPolyphonicBar(layout);
        default:          return null;
        }
    }

    private Range<Integer> determineTimeunitRange() {
        // Timeunit range in bar
        Range<Integer> barRange = Range.closed(0, timeSig.timeunitsInABar());
        
        // Total timeunit range 
        Range<Integer> tuRange = Range.all();
        
        // Cleaned entries
        Multimap<Integer, NoteEntry> cleaned = LinkedListMultimap.create();
        
        // Extend total range with each note
        for (Entry<Integer, NoteEntry> e : entries.entries()) {
            // Current note range
            int beg = e.getKey();
            int len = e.getValue().lengthTU;
            int end = beg + len;
            Range<Integer> range = Range.closed(beg, end);
            
            // Signal anomalies
            if (!barRange.isConnected(range)) {
                taskLogger.signal(Anomaly.NOTE_OUT_OF_SCOPE, beg, end);
                continue;
            }
            if (len < 1) {
                taskLogger.signal(Anomaly.NOTE_LENGTH_NOT_POSITIVE, len);
                continue;
            }
            
            // Extend total range
            tuRange = tuRange.span(range);
            
            // Save entry with correct range
            cleaned.put(e.getKey(), e.getValue());
        }
        
        entries = cleaned;
        
        return tuRange;
    }
    
    private List<List<LayoutEntry>> determineLayout(Range<Integer> tuRange) {
        // Create empty layout
        List<List<LayoutEntry>> layout = new ArrayList<>();
        int min = tuRange.hasLowerBound() ? tuRange.lowerEndpoint() : 0;
        int max = tuRange.hasUpperBound() ? tuRange.upperEndpoint() : timeSig.timeunitsInABar();
        for (int i = min; i < max; ++i) {
            layout.add(new ArrayList<>());
        }
        
        // Fill layout
        for (Entry<Integer, NoteEntry> e : entries.entries()) {
            int beg = e.getKey();
            int len = e.getValue().lengthTU;
            int end = beg + len;
            
            layout.get(beg - min).add(new LayoutEntry(NoteSection.BEGINNING, e.getValue()));
            
            LayoutEntry middle = new LayoutEntry(NoteSection.MIDDLE, e.getValue());
            for (int i = beg + 1; i < end - 1; ++i) {
                layout.get(beg - min).add(middle);
            }
            
            layout.get(end - 1 - min).add(new LayoutEntry(NoteSection.END, e.getValue()));
        }
        
        return layout;
    }
    
    private ComplexityHierarchyRank findComplexityRank(List<List<LayoutEntry>> layout,
                                                       int                     tuRangeBeg) {
        boolean isMonophonic = true;
        
        for (int i = 0; i < timeSig.timeunitsInABar(); ++i) {
            List<LayoutEntry> slice = layout.get(i - tuRangeBeg);
            if (slice.size() < 2) {
                isMonophonic &= true;
                continue;
            }
            isMonophonic = false;
            
            NoteSection section = slice.get(0).section;
            for (int j = 1; j < slice.size(); ++j) {
                if (slice.get(j).section != section) {
                    return ComplexityHierarchyRank.POLYPHONIC;
                }
            }
        }
        
        return isMonophonic ? ComplexityHierarchyRank.MONOPHONIC
                            : ComplexityHierarchyRank.HOMOPHONIC;
    }
    
    private MonophonicBar buildMonophonicBar() {
        // A bar is just a list of bar slice of the corresponding complexity rank
        List<MonophonicBarSlice> slices = new LinkedList<>();
        
        // Sort note beginning timeunits
        Iterator<Integer> begTUs = new TreeSet<>(entries.keySet()).iterator();
                
        // Last node to be added
        MonophonicNoteNode node = null;
        int prevEndTU = 0;
        int begTU = begTUs.hasNext() ? begTUs.next() : 0;
        
        while (begTUs.hasNext()) {
            // Add rest to fill bar between notes
            if (prevEndTU != begTU) {
                node = addMonophonicEntry(prevEndTU, 
                                          begTU, 
                                          Optional.empty(), 
                                          Optional.ofNullable(node), 
                                          slices);
            }
            
            // Add note
            NoteEntry entry = entries.get(begTU).iterator().next();
            int endTU = begTU + entry.lengthTU;
            node = addMonophonicEntry(begTU, 
                                      endTU, 
                                      Optional.of(entry), 
                                      Optional.ofNullable(node), 
                                      slices);
            // Go to next note
            prevEndTU = endTU;
            begTU = begTUs.next();
        }
        
        // Add last rest if needed
        if (prevEndTU < timeSig.timeunitsInABar()) {
            addMonophonicEntry(prevEndTU, 
                               timeSig.timeunitsInABar(), 
                               Optional.empty(), 
                               Optional.ofNullable(node), 
                               slices);
        }
        
        return new MonophonicBar(slices);
    }

    private MonophonicNoteNode addMonophonicEntry(int                          begTU, 
                                                  int                          endTU,
                                                  Optional<NoteEntry>          entry,
                                                  Optional<MonophonicNoteNode> prevNode,
                                                  List<MonophonicBarSlice>     slices) {
        
        // Clamp timeunits to fit them in bars
        begTU = Math.max(0, begTU);
        endTU = Math.min(timeSig.timeunitsInABar(), endTU);
        
        MonophonicNoteNode prev = prevNode.orElse(null); 
        MonophonicNoteNode curr = null;
        
        // Split timeunit interval into rythmic values
        for (RythmnValue value : splitIntoRythmicValues(begTU, endTU - begTU)) {
            if (entry.isPresent()) {
                // Crate note node
                Note note = Note.of(entry.get().pitch, value, Dynamic.of(entry.get().velocity));
                curr = new MonophonicNoteNode(note);
            } else {
                // Create rest node
                curr = new MonophonicNoteNode(value);
            }
            
            // Create slices
            for (int i = 0; i < value.toTimeunit(); ++i) {
                slices.add(new MonophonicBarSlice.Impl(curr, i == 0));
            }
            
            // Connect if possible
            if (prev != null) {
                MonophonicNoteNode.Neighbourhood.Impl.connect(prev, curr, 
                        TiedNoteStatus.AS_SEPARATE_NOTES, true);
            }
            prev = curr;
        }
        
        return curr;
    }
    
    private HomophonicBar buildHomophonicBar(List<List<LayoutEntry>> layout) {
        throw new NotImplementedException();
    }
    
    private PolyphonicBar buildPolyphonicBar(List<List<LayoutEntry>> layout) {
        throw new NotImplementedException();
    }

    public static List<RythmnValue> splitIntoRythmicValues(int begTU, int lenTU) {
        List<RythmnValue> values = new ArrayList<>();

        for (int i = RythmnValue.values().length - 1; i >= 0; --i) {
            RythmnValue val = RythmnValue.values()[i];
            int valLen = val.toTimeunit();

            if (valLen > lenTU) {
                continue;
            }
            int valBeg = 0;
            while (valBeg < begTU) {
                valBeg += valLen;
            }
            int valEnd = valBeg + valLen;

            if (valEnd > lenTU) {
                continue;
            }
            if (begTU < valBeg) {
                values.addAll(splitIntoRythmicValues(begTU, valBeg));
            }
            values.add(val);

            if (valEnd < lenTU) {
                values.addAll(splitIntoRythmicValues(valEnd, lenTU));
            }
            break;
        }

        return values;
    }
    
    @Implement
    public void reset() {
        entries.clear();
        
        currBegTU    = 0;
        currLenTU    = PolyphonicNoteNode.DEFAULT_VALUE.toTimeunit();
        currVelocity = PolyphonicNoteNode.DEFAULT_DYNAMIC_MARKER.getMinimumVelocity();
    }
}
