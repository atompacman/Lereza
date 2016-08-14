package com.atompacman.lereza.core.midi.in;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import com.atompacman.lereza.core.piece.Piece;
import com.atompacman.lereza.core.piece.PieceBuilder;
import com.atompacman.lereza.core.piece.timeline.TimeunitToBarConverter;
import com.atompacman.lereza.core.theory.TimeSignature;
import com.atompacman.toolkat.annotations.Temporary;
import com.atompacman.toolkat.task.AnomalyDescription;
import com.atompacman.toolkat.task.AnomalyDescription.Severity;
import com.atompacman.toolkat.task.TaskMonitor;
import com.google.common.collect.SetMultimap;

public final class MIDISequenceContentAssembler {

    //
    //  ~  CONSTANTS  ~  //
    //
    
    private static final int TICK_OFFSET_CORREC_RADIUS     = 5;
    private static final int MAXIMUM_ACCEPTABLE_TICK_DELTA = 5;

    
    //
    //  ~  INNER TYPES  ~  //
    //
    
    private enum Anomaly {
        
        @AnomalyDescription (
                name            = "Missing initial MIDI event",
                detailsFormat   = "Missing %s change event",
                description     = "Expecting a certain MIDI event at tick 0", 
                consequences    = "An arbitrary value will be chosen", 
                severity        = Severity.MODERATE)
        MISSING_INITIAL_MIDI_EVENT,
        
        @AnomalyDescription (
                name            = "Tick offset change", 
                description     = "A change in tick rounding delta", 
                consequences    = "Possible rhythm deviation", 
                severity        = Severity.MINIMAL)
        TICK_OFFSET_CHANGE,

        @AnomalyDescription (
                name            = "Minor tick rounding", 
                detailsFormat   = "Tick:%6d >>%6d | Delta tick: %s >> %s (%s) | Timeunit: %d",
                description     = "A tick had to be slightly rounded", 
                consequences    = "Possible rhythm deviation", 
                severity        = Severity.MINIMAL)
        MINOR_TICK_ROUNDING,

        @AnomalyDescription (
                name            = "Major tick rounding", 
                detailsFormat   = "Tick:%6d >>%6d | Delta tick: %s >> %s (%s) | Timeunit: %d",
                description     = "A tick had to be heavily rounded", 
                consequences    = "Expected rhythm deviation", 
                severity        = Severity.MODERATE)
        MAJOR_TICK_ROUNDING,

        @AnomalyDescription (
                name            = "Chaotic tick subsequence",
                detailsFormat   = "Chaotic ticks at %d, with the following deltas: %s",
                description     = "A series of ticks had variable offsets with main rhythm", 
                consequences    = "Allmost certain rhythm deviation", 
                severity        = Severity.CRITIC)
        CHAOTIC_TICK_SUBSEQUENCE;
    }

    
    //
    //  ~  FIELDS  ~  //
    //
    
    private @Temporary TaskMonitor monitor;
    
    
    //
    //  ~  INIT  ~  //
    //

    public MIDISequenceContentAssembler() {
        this.monitor = null;
    }
    
    
    //
    //  ~  ASSEMBLE  ~  //
    //

    public Piece assemble(MIDISequenceContent content, TaskMonitor monitor) 
                                                                    throws MIDIFileLoaderException {
        
        Map<Long, Integer> conversionMap = 
        monitor.executeSubtaskExcep("Convert ticks to timeunits", mon -> {
            this.monitor = mon;
            Set<Long> sortedTicks = createSortedPieceTickSet(content);
            int anacrusisCorrection = calculateAnacrusisCorrectionTick(content);
            return createTickToTUConversionMap(new ArrayList<>(sortedTicks), 
                                               content.getNumTicksPer64thNote(),
                                               anacrusisCorrection);
        });

        TreeMap<Integer, TimeSignature> timeSignTU = 
        monitor.executeSubtask("Convert piece properties to timeunits", mon -> {
            this.monitor = mon;
            return convertTicksToTU(content.getTimeSignChanges(), conversionMap, 
                                    TimeSignature.STANDARD_4_4, "time signature");
            // TODO BUILD THESE
//          TreeMap<Integer, Double>        tempoTU    = convertTicksToTU(content.getTempoChanges(), 
//          conversionMap, 
//          120.0, 
//          "tempo");
//          TreeMap<Integer, Key>           keyTU      = convertTicksToTU(content.getKeyChanges(), 
//          conversionMap, 
//          Key.valueOf("C"), 
//          "key");
        });

        // Create timeline structures
        TimeunitToBarConverter tuToBar = 
        monitor.executeSubtask("Create timeline structures", mon -> {
            int len = content.getSequenceLengthTU();
            int pieceLengthTU = calculatePieceLengthTU(len, timeSignTU.lastEntry(), mon);
            return TimeunitToBarConverter.of(timeSignTU, pieceLengthTU);
            // TODO use these
            //new TimeSignatureTimeline(timeSignTU, tuToBar);
            //new TempoTimeline(tempoTU, pieceLengthTU);
            //new MIDIKeyTimeline(keyTU, pieceLengthTU);
        });
        
        // TODO fusionOddNotesWithTinyRests ?
        
        return monitor.executeSubtask("Build piece", mon -> {
            PieceBuilder builder = PieceBuilder.of(tuToBar);
            List<MIDITrack> tracks = content.getTracks();

            for (int i = 0; i < tracks.size(); ++i) {
                mon.executeSubtask("Build track " + (i + 1), i, (submon, j) -> {
                    buildTrack(j, tracks.get(j).getNotes(), conversionMap, builder);
                });
            }

            return builder.build(mon);
        });
    }

    private static TreeSet<Long> createSortedPieceTickSet(MIDISequenceContent content) {
        TreeSet<Long> tickSet = new TreeSet<>();

        for (MIDITrack track : content.getTracks()) {
            tickSet.addAll(track.getNotes().keySet());
            tickSet.addAll(track.getEndingNotes().keySet());
        }
        
        tickSet.addAll(content.getTempoChanges().keySet());
        tickSet.addAll(content.getTimeSignChanges().keySet());
        tickSet.addAll(content.getKeyChanges().keySet());
        
        return tickSet;
    }

    private int calculateAnacrusisCorrectionTick(MIDISequenceContent content) {
        SortedMap<Long, TimeSignature> timeSignChangesTick = content.getTimeSignChanges();
        Iterator<Entry<Long, TimeSignature>> iter = timeSignChangesTick.entrySet().iterator();
        
        TimeSignature firstSign = iter.next().getValue();
        
        if (!iter.hasNext()) {
            // TODO
            //throw new RuntimeException("Cannot calculate anacrusis "
              //      + "correction without at least two key changes");
            return 0;
        }
        
        Long secondTick = iter.next().getKey();
        long numTicksPerBar = firstSign.timeunitsInABar() * content.getNumTicksPer64thNote();
        int anacrusis = (int)(numTicksPerBar - (secondTick % numTicksPerBar));
        if (anacrusis == numTicksPerBar) {
            anacrusis = 0;
        }
        monitor.log("Anacrusis correction length: %d ticks", anacrusis);
        
        return anacrusis;
    }
    
    private Map<Long, Integer> createTickToTUConversionMap(List<Long>  ticks, 
                                                           int         ticksPer64thNote, 
                                                           int         anacrusisCorrTick)
                                                               throws MIDIFileLoaderException {
        
        int currDeltaTick = 0;
        double tuPerTick = 1.0 / ticksPer64thNote;

        // Initialize conversion map
        Map<Long, Integer> tickToTU = new LinkedHashMap<>();

        // For all ticks
        for (int i = 0; i < ticks.size(); ++i) {
            // Get rounded timeunit value
            long tick = ticks.get(i);
            long corrTick = tick + anacrusisCorrTick;
            Long roundedTU = Math.round((double)(corrTick + currDeltaTick) * tuPerTick);

            // Get tick delta
            long roundedTick = roundedTU * (int) ticksPer64thNote;
            int deltaTick = (int) (roundedTick - corrTick);

            // Add rounded timeunit to conversion map
            tickToTU.put(tick, roundedTU.intValue());
            
            // If tick delta did not change
            if (deltaTick == currDeltaTick) {
                monitor.log("Tick:%6d >>%6d | Delta tick: %s >> %s| Timeunit: %d", tick, 
                        roundedTick, formatDelta(currDeltaTick), formatDelta(deltaTick), roundedTU);
                continue;
            }

            // Count occurrences of future tick deltas
            Map<Integer,Integer> deltaOcc = new HashMap<>();
            for (int j = 1; j <= TICK_OFFSET_CORREC_RADIUS && i + j < ticks.size(); ++j) {
                long futureTick = ticks.get(i+j);
                long futureRoundedTU = Math.round((double)(futureTick + currDeltaTick) * tuPerTick);
                int futureDeltaTick = (int) (futureRoundedTU * (int) ticksPer64thNote - futureTick);
                Integer count = deltaOcc.get(futureDeltaTick);
                deltaOcc.put(futureDeltaTick, count == null ? 1 : count + 1);
            }

            // Compute delta delta
            int deltaDelta = deltaTick - currDeltaTick;
            Anomaly anomaly = Math.abs(deltaDelta) > MAXIMUM_ACCEPTABLE_TICK_DELTA ? 
                    Anomaly.MAJOR_TICK_ROUNDING : Anomaly.MINOR_TICK_ROUNDING;

            monitor.signal(anomaly, tick, roundedTick, formatDelta(currDeltaTick), 
                    formatDelta(deltaTick), formatDelta(deltaDelta), roundedTU);

            // If majority of future deltas are the same than current delta 
            if (deltaOcc.get(deltaTick) > (TICK_OFFSET_CORREC_RADIUS - 1) / 2) {
                monitor.signal(Anomaly.TICK_OFFSET_CHANGE);
                currDeltaTick = deltaTick;
            } else if (deltaOcc.size() >= TICK_OFFSET_CORREC_RADIUS) {
                monitor.signal(Anomaly.CHAOTIC_TICK_SUBSEQUENCE, tick, deltaOcc.keySet()); 
            }
        }
        return tickToTU;
    }
    
    private static String formatDelta(int delta) {
        return String.format("%s%-2d", delta < 0 ? "-" : "+", Math.abs(delta));
    }
    
    private <T> TreeMap<Integer, T> convertTicksToTU(SortedMap<Long, T> tickChanges,
                                                     Map<Long, Integer> conversionMap,
                                                     T                  defaultInitValue,
                                                     String             propertyName) {
        
        Iterator<Entry<Long, T>> it = tickChanges.entrySet().iterator();
        Entry<Long, T> entry = it.hasNext() ? it.next() : null;
        
        if (entry == null || entry.getKey() != 0L) {
            monitor.signal(Anomaly.MISSING_INITIAL_MIDI_EVENT, propertyName);
            entry.setValue(defaultInitValue);
        }
        
        TreeMap<Integer, T> tuChanges = new TreeMap<>();
        tuChanges.put(0, entry.getValue());
        
        while (it.hasNext()) {
            entry = it.next();
            tuChanges.put(conversionMap.get(entry.getKey()), entry.getValue());
        }
        
        return tuChanges;
    }
    
    private int calculatePieceLengthTU(int                           sequenceLengthTU,
                                       Entry<Integer, TimeSignature> lastTimeSignEntry,
                                       TaskMonitor                   monitor) {
        
        if (lastTimeSignEntry.getKey() >= sequenceLengthTU) {
            throw new IllegalArgumentException("Last time signature "
                    + "change must have happened before piece ends");
        }
        
        int endTU = lastTimeSignEntry.getKey();
        int tuInLastBar = lastTimeSignEntry.getValue().timeunitsInABar();
        
        while (endTU < sequenceLengthTU) {
            endTU += tuInLastBar;
        }
        
        monitor.log("Piece length: %d timeunits", endTU);

        return endTU;
    }
    
    private static void buildTrack(int                         partNum,
                                   SetMultimap<Long, MIDINote> notes, 
                                   Map<Long, Integer>          conversionMap, 
                                   PieceBuilder                builder) {

        for (long tick : conversionMap.keySet()) {
            Set<MIDINote> noteSet = notes.get(tick);
            if (noteSet == null) {
                continue;
            }
            for (MIDINote note : noteSet) {
                int startTU  = conversionMap.get(note.startTick());
                int lengthTU = conversionMap.get(note.endTick()) - startTU;
                builder.add(note.getHexNote(), note.getVelocity(), partNum, startTU, lengthTU);
            }
        }
    }
}
