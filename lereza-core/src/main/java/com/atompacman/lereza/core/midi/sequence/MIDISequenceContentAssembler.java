package com.atompacman.lereza.core.midi.sequence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.logging.log4j.Level;

import com.atompacman.lereza.core.piece.Piece;
import com.atompacman.lereza.core.piece.PieceBuilder;
import com.atompacman.lereza.core.piece.timeline.MIDIKeyTimeline;
import com.atompacman.lereza.core.piece.timeline.TempoTimeline;
import com.atompacman.lereza.core.piece.timeline.TimeSignatureTimeline;
import com.atompacman.lereza.core.piece.timeline.TimeunitToBarConverter;
import com.atompacman.lereza.core.solfege.Key;
import com.atompacman.lereza.core.solfege.TimeSignature;
import com.atompacman.toolkat.exception.Throw;
import com.atompacman.toolkat.exception.UnimplementedException;
import com.atompacman.toolkat.module.AnomalyDescription;
import com.atompacman.toolkat.module.BaseModule;
import com.atompacman.toolkat.module.AnomalyDescription.Severity;
import com.atompacman.toolkat.module.Module;
import com.atompacman.toolkat.module.ProcedureDescription;

public class MIDISequenceContentAssembler extends Module {

    //====================================== CONSTANTS ===========================================\\

    private static final int TICK_OFFSET_CORREC_RADIUS     = 5;
    private static final int MAXIMUM_ACCEPTABLE_TICK_DELTA = 5;


    
    //===================================== INNER TYPES ==========================================\\

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

    private enum Procedure {

        @ProcedureDescription (nameFormat = "Converting MIDI ticks to timeunits")
        CONVERT_TICKS_TO_TIMEUNITS,

        @ProcedureDescription (nameFormat = "Building piece")
        BUILD_PIECE,
        
        @ProcedureDescription (nameFormat = "Building track %s")
        BUILD_TRACK;
    }
    
    
    
    //======================================= METHODS ============================================\\

    //------------------------------------- CONSTRUCTORS -----------------------------------------\\

    public MIDISequenceContentAssembler(BaseModule parentModule) {
        super(Level.DEBUG, parentModule);
    }
    

    //--------------------------------------- ASSEMBLE -------------------------------------------\\

    public Piece assemble(MIDISequenceContent content) throws MIDIFileReaderException {
        procedure(Procedure.CONVERT_TICKS_TO_TIMEUNITS);

        // Create a map that links ticks to timeunits
        Map<Long, Integer> conversionMap = createTickToTUConversionMap(
                                               new ArrayList<>(createSortedPieceTickSet(content)), 
                                               content.getNumTicksPer64thNote(), 
                                               calculateAnacrusisCorrectionTick(content));
        
        // Convert piece properties to timeunits
        TreeMap<Integer, TimeSignature> timeSignTU = convertTicksToTU(content.getTimeSignChanges(), 
                                                                      conversionMap, 
                                                                      TimeSignature.STANDARD_4_4, 
                                                                      "time signature");
        TreeMap<Integer, Double>        tempoTU    = convertTicksToTU(content.getTempoChanges(), 
                                                                      conversionMap, 
                                                                      120.0, 
                                                                      "tempo");
        TreeMap<Integer, Key>           keyTU      = convertTicksToTU(content.getKeyChanges(), 
                                                                      conversionMap, 
                                                                      Key.valueOf("C"), 
                                                                      "key");

        // Create timeline structures
        int pieceLengthTU = calculatePieceLengthTU(content.getSequenceLengthTU(),
                                                   timeSignTU.lastEntry());
        TimeunitToBarConverter tuToBar = new TimeunitToBarConverter(timeSignTU, pieceLengthTU);

        // TODO use these
        new TimeSignatureTimeline(timeSignTU, tuToBar);
        new TempoTimeline(tempoTU, pieceLengthTU);
        new MIDIKeyTimeline(keyTU, pieceLengthTU);
        
        // TODO fusionOddNotesWithTinyRests ?
        
        procedure(Procedure.BUILD_PIECE);
        PieceBuilder builder = new PieceBuilder(tuToBar, this);
        List<MIDITrack> tracks = content.getTracks();

        for (int i = 0; i < tracks.size(); ++i) {
            subprocedure(Procedure.BUILD_TRACK, i + 1);
            buildTrack(i, tracks.get(i).getNotes(), conversionMap, builder);
        }

        return builder.buildComponent();
    }
    
    private int calculateAnacrusisCorrectionTick(MIDISequenceContent content) {
        TreeMap<Long, TimeSignature> timeSignChangesTick = content.getTimeSignChanges();
        Iterator<Entry<Long, TimeSignature>> iter = timeSignChangesTick.entrySet().iterator();
        
        TimeSignature firstSign = iter.next().getValue();
        
        if (!iter.hasNext()) {
            Throw.aRuntime(UnimplementedException.class, "Cannot calculate "
                    + "anacrusis correction without at least two key changes");
        }
        
        Long secondTick = iter.next().getKey();
        long numTicksPerBar = firstSign.timeunitsInABar() * content.getNumTicksPer64thNote();
        int anacrusis = (int)(numTicksPerBar - (secondTick % numTicksPerBar));
        if (anacrusis == numTicksPerBar) {
            anacrusis = 0;
        }
        log("Anacrusis correction length: %d ticks", anacrusis);
        
        return anacrusis;
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
    
    private Map<Long, Integer> createTickToTUConversionMap(List<Long> ticks, 
                                                           int        ticksPer64thNote, 
                                                           int        anacrusisCorrTick)
                                                               throws MIDIFileReaderException {
        
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
                log("Tick:%6d >>%6d | Delta tick: %s >> %s| Timeunit: %d", tick, roundedTick, 
                        formatDelta(currDeltaTick), formatDelta(deltaTick), roundedTU);
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

            signal(anomaly, tick, roundedTick, formatDelta(currDeltaTick), 
                    formatDelta(deltaTick), formatDelta(deltaDelta), roundedTU);

            // If majority of future deltas are the same than current delta 
            if (deltaOcc.get(deltaTick) > (TICK_OFFSET_CORREC_RADIUS - 1) / 2) {
                signal(Anomaly.TICK_OFFSET_CHANGE);
                currDeltaTick = deltaTick;
            } else if (deltaOcc.size() >= TICK_OFFSET_CORREC_RADIUS) {
                signal(Anomaly.CHAOTIC_TICK_SUBSEQUENCE, tick, deltaOcc.keySet()); 
            }
        }
        return tickToTU;
    }
    
    private static String formatDelta(int delta) {
        return String.format("%s%-2d", delta < 0 ? "-" : "+", Math.abs(delta));
    }
    
    private <T> TreeMap<Integer, T> convertTicksToTU(TreeMap<Long, T>   tickChanges,
                                                     Map<Long, Integer> conversionMap,
                                                     T                  defaultInitValue,
                                                     String             propertyName) {
        
        Iterator<Entry<Long, T>> it = tickChanges.entrySet().iterator();
        Entry<Long, T> entry = it.hasNext() ? it.next() : null;
        
        if (entry == null || entry.getKey() != 0L) {
            signal(Anomaly.MISSING_INITIAL_MIDI_EVENT, propertyName);
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
                                       Entry<Integer, TimeSignature> lastTimeSignEntry) {
        
        if (lastTimeSignEntry.getKey() >= sequenceLengthTU) {
            throw new IllegalArgumentException("Last time signature "
                    + "change must have happened before piece ends");
        }
        
        int endTU = lastTimeSignEntry.getKey();
        int tuInLastBar = lastTimeSignEntry.getValue().timeunitsInABar();
        
        while (endTU < sequenceLengthTU) {
            endTU += tuInLastBar;
        }
        
        log("Piece length: %d timeunits", endTU);

        return endTU;
    }
    
    private static void buildTrack(int                      partNum,
                                   Map<Long, Set<MIDINote>> notes, 
                                   Map<Long, Integer>       conversionMap, 
                                   PieceBuilder             builder) {

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
