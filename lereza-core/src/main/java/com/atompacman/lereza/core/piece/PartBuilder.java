package com.atompacman.lereza.core.piece;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeSet;

import com.atompacman.lereza.core.piece.MonophonicNoteNode.Neighbourhood;
import com.atompacman.lereza.core.piece.PolyphonicNoteNode.TiedNoteStatus;
import com.atompacman.lereza.core.piece.timeline.InfiniteTimeunitToBarConverter;
import com.atompacman.lereza.core.piece.timeline.TimeunitToBarConverter;
import com.atompacman.lereza.core.theory.Dynamic;
import com.atompacman.lereza.core.theory.Note;
import com.atompacman.lereza.core.theory.Pitch;
import com.atompacman.lereza.core.theory.RhythmValue;
import com.atompacman.lereza.core.theory.TimeSignature;
import com.atompacman.toolkat.Builder;
import com.atompacman.toolkat.annotations.Implement;
import com.atompacman.toolkat.task.Anomaly.Description;
import com.atompacman.toolkat.task.Anomaly.Severity;
import com.atompacman.toolkat.task.Task.NameFormat;
import com.atompacman.toolkat.task.TaskLogger;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public final class PartBuilder extends Builder<PolyphonicPart> {

    //
    //  ~  INNER TYPES  ~  //
    //

    private enum NoteSection {
        BEGINNING,
        MIDDLE,
        END
    }
    
    static final class NoteEntry {
        
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
    
    private static final class LayoutEntry {
        
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
        
        
        //
        //  ~  SERIALIZATION  ~  //
        //
        
        // @TODO remove me
        @Override
        public String toString() {
            return section.name() + "[" + noteEntry.pitch.toString() + "]";
        }
    }

    private enum Task {
        
        @NameFormat("Build part")
        BUILD_PART,
        
        @NameFormat("Determine layout")
        DETERMINE_LAYOUT,
        
        @NameFormat("Find complexity rank")
        FIND_COMPLEXITY_RANK,
        
        @NameFormat("Build bar slices")
        BUILD_BAR_SLICES,
        
        @NameFormat("Assemble bar slices")
        ASSEMBLE_BAR_SLICES
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
        SIMULTANEOUS_NOTES_WITH_SAME_PITCH
    }
    
    
    //
    //  ~  FIELDS  ~  //
    //

    // Lifetime
    private final TimeunitToBarConverter tuToBar;

    // Data for build
    private Multimap<Integer, NoteEntry> entries;

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
    
    PartBuilder(TimeunitToBarConverter tuToBar, Optional<TaskLogger> taskLogger) {
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
    
    public PartBuilder add(byte hexNote, byte velocity, int begTU, int lengthTU) {
        return add(Pitch.thatIsMoreCommonForHexValue(hexNote), velocity, begTU, lengthTU);
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
    protected PolyphonicPart buildImpl() {
        taskLogger.startTask(Task.BUILD_PART);
        
        taskLogger.startSubtaskOf(Task.BUILD_PART, Task.DETERMINE_LAYOUT);
        List<List<LayoutEntry>> layout = determineLayout();
        
        taskLogger.startSubtaskOf(Task.BUILD_PART, Task.FIND_COMPLEXITY_RANK);
        ComplexityHierarchyRank rank = findComplexityRank(layout);
        
        taskLogger.startSubtaskOf(Task.BUILD_PART, Task.BUILD_BAR_SLICES);
        List<PolyphonicBarSlice> slices;
        switch (rank) {
        case MONOPHONIC : slices = buildMonophonicBarSlices();       break;
        case HOMOPHONIC : slices = buildHomophonicBarSlices(layout); break;
        case POLYPHONIC : slices = buildPolyphonicBarSlices(layout); break;
        default:          throw new RuntimeException();
        }
        
        taskLogger.startSubtaskOf(Task.BUILD_PART, Task.ASSEMBLE_BAR_SLICES);
        return assembleBarSlices(slices);
    }

    private List<List<LayoutEntry>> determineLayout() {
        // Create empty layout
        List<List<LayoutEntry>> layout = new ArrayList<>(tuToBar.pieceLengthTU());
        for (int i = 0; i < tuToBar.pieceLengthTU(); ++i) {
            layout.add(new ArrayList<>());
        }
        
        Multimap<Integer, NoteEntry> cleaned = LinkedListMultimap.create();
        
        for (Entry<Integer, NoteEntry> e : entries.entries()) {
            // Current note range
            int beg = e.getKey();
            int len = e.getValue().lengthTU;
            int end = beg + len;
            
            if (len < 1) {
                taskLogger.signal(Anomaly.NOTE_LENGTH_NOT_POSITIVE, len);
                continue;
            }
            
            // Check if note fits in the part
            if (beg < 0 || end > tuToBar.pieceLengthTU()) {
                taskLogger.signal(Anomaly.NOTE_OUT_OF_SCOPE, beg, end);
                continue;
            }
            
            // Save entry with correct range
            cleaned.put(e.getKey(), e.getValue());
            
            // Save entry layout sections
            layout.get(beg).add(new LayoutEntry(NoteSection.BEGINNING, e.getValue()));
            LayoutEntry middle = new LayoutEntry(NoteSection.MIDDLE, e.getValue());
            for (int i = beg + 1; i < end - 1; ++i) {
                layout.get(i).add(middle);
            }
            layout.get(end - 1).add(new LayoutEntry(NoteSection.END, e.getValue()));
        }
        
        entries = cleaned;
        
        return layout;
    }
    
    private ComplexityHierarchyRank findComplexityRank(List<List<LayoutEntry>> layout) {
        boolean isMonophonic = true;
        
        for (List<LayoutEntry> slice : layout) {
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

    private List<PolyphonicBarSlice> buildMonophonicBarSlices() {
        List<PolyphonicBarSlice> slices = new LinkedList<>();
        
        // Sort note beginning timeunits
        Iterator<Integer> begTUs = new TreeSet<>(entries.keySet()).iterator();
                
        // Last node to be added
        List<MonophonicNoteNode> prevNodes = null;
        int prevEndTU = 0;
        int begTU = 0;
        
        while (begTUs.hasNext()) {
            begTU = begTUs.next();
            
            // Add rest between notes
            if (begTU > prevEndTU) {
                prevNodes = addMonophonicEntry(prevEndTU, 
                                               begTU, 
                                               Optional.empty(), 
                                               Optional.ofNullable(prevNodes), 
                                               slices);
            }
            
            // Add note
            NoteEntry entry = entries.get(begTU).iterator().next();
            int endTU = begTU + entry.lengthTU;
            prevNodes = addMonophonicEntry(begTU, 
                                           endTU, 
                                           Optional.of(entry), 
                                           Optional.ofNullable(prevNodes), 
                                           slices);
            prevEndTU = endTU;
        }
        
        // Add last rest if needed
        if (prevEndTU < tuToBar.pieceLengthTU()) {
            addMonophonicEntry(prevEndTU, 
                               tuToBar.pieceLengthTU(), 
                               Optional.empty(), 
                               Optional.ofNullable(prevNodes), 
                               slices);
        }
        
        return slices;
    }
    
    private List<MonophonicNoteNode> addMonophonicEntry(int                               begTU, 
                                                        int                               endTU,
                                                        Optional<NoteEntry>               entry,
                                                        Optional<List<MonophonicNoteNode>>prevNodes,
                                                        List<PolyphonicBarSlice>          slices) {

        List<MonophonicNoteNode> nodes = new LinkedList<>();
        MonophonicNoteNode firstPrev = null;
        MonophonicNoteNode prev = null; 
        if (prevNodes.isPresent()) {
            firstPrev = prevNodes.get().get(0);
            prev = prevNodes.get().get(prevNodes.get().size() - 1);
        }
        MonophonicNoteNode curr = null;
        
        boolean isFirstValue = true;
        
        // Split timeunit interval into rhythm values
        for (RhythmValue value : splitIntoRhythmValues(begTU, endTU)) {
            if (entry.isPresent()) {
                // Crate note node
                Note note = Note.of(entry.get().pitch, value, Dynamic.of(entry.get().velocity));
                curr = new MonophonicNoteNode(note);
            } else {
                // Create rest node
                curr = new MonophonicNoteNode(value);
            }

            // Connect if possible
            if (firstPrev != null) {
                if (isFirstValue) {
                    for (MonophonicNoteNode node : prevNodes.get()) {
                        connectAtoB(node, curr, TiedNoteStatus.MERGE_TOGETHER);
                    }
                }
                connectBtoA(firstPrev, curr, TiedNoteStatus.MERGE_TOGETHER);
            }

            if (prev != null) {
                connectAtoB(prev, curr, TiedNoteStatus.AS_SEPARATE_NOTES);
                connectBtoA(prev, curr, TiedNoteStatus.AS_SEPARATE_NOTES);
                if (!isFirstValue) {
                    prev.tieToNextNote();
                }
            }

            // Create slices
            for (int i = 0; i < value.toTimeunit(); ++i) {
                slices.add(new MonophonicBarSlice.Impl(curr, i == 0));
            }
            
            nodes.add(curr);
            prev = curr;
            isFirstValue = false;
        }
        
        return nodes;
    }
    
    private static void connectAtoB(MonophonicNoteNode a, 
                                    MonophonicNoteNode b,
                                    TiedNoteStatus     status) {
        
        Neighbourhood.Impl n = (Neighbourhood.Impl) a.getNoteNeighbourhood(status);
        checkArgument(!n.nextNode.isPresent());
        n.nextNode = Optional.of(b);
    }
    
    private static void connectBtoA(MonophonicNoteNode a, 
                                    MonophonicNoteNode b,
                                    TiedNoteStatus     status) {

        Neighbourhood.Impl n = (Neighbourhood.Impl) b.getNoteNeighbourhood(status);
        checkArgument(!n.prevNode.isPresent());
        n.prevNode = Optional.of(a);
    }
    
    private List<PolyphonicBarSlice> buildHomophonicBarSlices(List<List<LayoutEntry>> layout) {
        throw new NotImplementedException();
    }
    
    private List<PolyphonicBarSlice> buildPolyphonicBarSlices(List<List<LayoutEntry>> layout) {
        throw new NotImplementedException();
    }

    public static List<RhythmValue> splitIntoRhythmValues(int begTU, int lenTU) {
        List<RhythmValue> values = new LinkedList<>();

        for (int i = RhythmValue.values().length - 1; i >= 0; --i) {
            RhythmValue val = RhythmValue.values()[i];
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
                values.addAll(splitIntoRhythmValues(begTU, valBeg));
            }
            values.add(val);

            if (valEnd < lenTU) {
                values.addAll(splitIntoRhythmValues(valEnd, lenTU));
            }
            break;
        }

        return values;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private PolyphonicPart assembleBarSlices(List<PolyphonicBarSlice> slices) {
        ComplexityHierarchyRank rank = slices.get(0).getComplexityHierarchyRank();
        
        List<PolyphonicBar> bars = new LinkedList<>();
        for (int i = 0; i < tuToBar.getNumBars(); ++i) {
            int barBeg = tuToBar.convertBarToTu(i);
            int barEnd = barBeg + tuToBar.getBarLengthTUFromBar(i);
            List<PolyphonicBarSlice> barSlices = slices.subList(barBeg, barBeg + barEnd);
            
            switch (rank) {
            case MONOPHONIC: bars.add(new MonophonicBar((List) barSlices)); break;
            case HOMOPHONIC: bars.add(new HomophonicBar((List) barSlices)); break;
            case POLYPHONIC: bars.add(new PolyphonicBar((List) barSlices)); break;
            }
        }
        
        switch (rank) {
        case MONOPHONIC: return new MonophonicPart((List) bars);
        case HOMOPHONIC: return new HomophonicPart((List) bars);
        case POLYPHONIC: return new PolyphonicPart((List) bars);
        default:         throw  new RuntimeException();
        }
    }
    
    @Implement
    public void reset() {
        entries.clear();
        
        currBegTU    = 0;
        currLenTU    = RhythmValue.QUARTER.toTimeunit();
        currVelocity = Dynamic.Marker.FORTE.getMinimumVelocity();
    }
}
