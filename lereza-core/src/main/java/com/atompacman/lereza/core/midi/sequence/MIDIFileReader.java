package com.atompacman.lereza.core.midi.sequence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;
import javax.sound.midi.Track;

import org.apache.logging.log4j.Level;

import com.atompacman.lereza.core.piece.Note;
import com.atompacman.lereza.core.piece.Piece;
import com.atompacman.lereza.core.piece.PieceBuilder;
import com.atompacman.lereza.core.piece.Stack;
import com.atompacman.lereza.core.solfege.CircleOfFifths;
import com.atompacman.lereza.core.solfege.Key;
import com.atompacman.lereza.core.solfege.Quality;
import com.atompacman.lereza.core.solfege.Tone;
import com.atompacman.toolkat.IO;
import com.atompacman.toolkat.misc.Log;
import com.atompacman.toolkat.module.AnomalyDescription;
import com.atompacman.toolkat.module.AnomalyDescription.Severity;
import com.atompacman.toolkat.module.LogEntry;
import com.atompacman.toolkat.module.Module;
import com.atompacman.toolkat.module.ProcedureDescription;

public class MIDIFileReader extends Module {

    //====================================== SINGLETON ===========================================\\

    private static class InstanceHolder {
        private static final MIDIFileReader instance = new MIDIFileReader();
    }

    public static MIDIFileReader getInstance() {
        return InstanceHolder.instance;
    }



    //====================================== CONSTANTS ===========================================\\

    private static final int TIMESTAMP_OFFSET_CORREC_RADIUS = 5;
    private static final int MAXIMUM_ACCEPTABLE_TS_DELTA    = 5;



    //===================================== INNER TYPES ==========================================\\

    private enum MetaMessageType {

        SEQUENCE_NUMBER     (0x00),
        TEXT                (0x01),
        COPYRIGHT_NOTICE    (0x02),
        TRACK_NAME          (0x03),
        INSTRUMENT_NAME     (0x04),
        LYRICS              (0x05),
        MARKER              (0x06),
        CUE_POINT           (0x07),
        CHANNEL_NUMBER      (0x20),
        MIDI_PORT           (0x21),
        END_OF_TRACK        (0x2F),
        SET_TEMPO           (0x51),
        SMPTE_OFFSET        (0x54),
        TIME_SIGNATURE      (0x58),
        KEY_SIGNATURE       (0x59),
        SEQUENCER_SPECIFIC  (0x7F);


        //===================================== FIELDS ===========================================\\

        private final int typeByte;



        //===================================== METHODS ==========================================\\

        //-------------------------------- PRIVATE CONSTRUCTOR -----------------------------------\\

        private MetaMessageType(int typeByte) {
            this.typeByte = typeByte;
        }



        //================================== STATIC METHODS ======================================\\

        public static MetaMessageType of(MetaMessage msg) {
            int metaType = msg.getType();

            for (MetaMessageType type : MetaMessageType.values()) {
                if (metaType == type.typeByte) {
                    return type;
                }
            }
            Log.error("Unknown meta message type \"%s\"", metaType);
            return null;
        }
    }

    private enum ChannelMessageCmd {

        NOTE_OFF            (ShortMessage.NOTE_OFF),
        NOTE_ON             (ShortMessage.NOTE_ON),
        POLY_PRESSURE       (ShortMessage.POLY_PRESSURE),
        CONTROL_CHANGE      (ShortMessage.CONTROL_CHANGE),
        PROGRAM_CHANGE      (ShortMessage.PROGRAM_CHANGE),
        CHANNEL_PRESSURE    (ShortMessage.CHANNEL_PRESSURE),
        PITCH_BEND          (ShortMessage.PITCH_BEND),
        SYSTEM_MESSAGE      (0xF0);


        //===================================== FIELDS ===========================================\\

        private final int signifByte;



        //===================================== METHODS ==========================================\\

        //-------------------------------- PRIVATE CONSTRUCTOR -----------------------------------\\

        private ChannelMessageCmd(int signifByte) {
            this.signifByte = signifByte;
        }



        //================================== STATIC METHODS ======================================\\

        public static ChannelMessageCmd of(ShortMessage msg) {
            int msgCmd = msg.getCommand();

            for (ChannelMessageCmd cmd : ChannelMessageCmd.values()) {
                if (msgCmd == cmd.signifByte) {
                    return cmd;
                }
            }
            Log.error("Unknown channel message command \"%s\"", msgCmd);
            return null;
        }
    }

    private enum SystemCommonMessageType {

        MIDI_TIME_CODE          (ShortMessage.MIDI_TIME_CODE),
        SONG_POSITION_POINTER   (ShortMessage.SONG_POSITION_POINTER),
        SONG_SELECT             (ShortMessage.SONG_SELECT),
        TUNE_REQUEST            (ShortMessage.TUNE_REQUEST),
        END_OF_EXCLUSIVE        (ShortMessage.END_OF_EXCLUSIVE),
        TIMING_CLOCK            (ShortMessage.TIMING_CLOCK),
        START                   (ShortMessage.START),
        CONTINUE                (ShortMessage.CONTINUE),
        STOP                    (ShortMessage.STOP),
        ACTIVE_SENSING          (ShortMessage.ACTIVE_SENSING),
        SYSTEM_RESET            (ShortMessage.SYSTEM_RESET);


        //===================================== FIELDS ===========================================\\

        private final int statusByte;



        //===================================== METHODS ==========================================\\

        //-------------------------------- PRIVATE CONSTRUCTOR -----------------------------------\\

        private SystemCommonMessageType(int statusByte) {
            this.statusByte = statusByte;
        }



        //================================== STATIC METHODS ======================================\\

        public static SystemCommonMessageType of(ShortMessage msg) {
            int status = msg.getStatus();

            for (SystemCommonMessageType scmt : SystemCommonMessageType.values()) {
                if (status == scmt.statusByte) {
                    return scmt;
                }
            }
            Log.error("Unknown system common message \"%s\"", status);
            return null;
        }
    }

    private enum Anomaly {
        
        @AnomalyDescription (
                name            = "Invalid MIDI file",
                detailsFormat   = "Could not load MIDI sequence at \"%s\"",
                description     = "MIDI file is either inexistant or of invalid format", 
                consequences    = "Cannot continue MIDI file reading", 
                severity        = Severity.FATAL)
        INVALID_MIDI_FILE,
        
        @AnomalyDescription (
                name            = "Ignored MIDI event",
                description     = "Processing for some MIDI event is not implemented", 
                consequences    = "May contain useful information", 
                severity        = Severity.MINIMAL)
        IGNORED_MIDI_EVENT,

        @AnomalyDescription (
                name            = "Timestamp offset change", 
                description     = "A change in timestamp rounding delta", 
                consequences    = "Possible rhythm deviation", 
                severity        = Severity.MINIMAL)
        TIMESTAMP_OFFSET_CHANGE,

        @AnomalyDescription (
                name            = "Minor timestamp rounding", 
                detailsFormat   = "Timestamp:%6d >>%6d | Delta: %s >> %s (%s) | Timeunit: %d",
                description     = "A timestamp had to be slightly rounded", 
                consequences    = "Possible rhythm deviation", 
                severity        = Severity.MINIMAL)
        MINOR_TIMESTAMP_ROUNDING,

        @AnomalyDescription (
                name            = "Major timestamp rounding", 
                detailsFormat   = "Timestamp:%6d >>%6d | Delta: %s >> %s (%s) | Timeunit: %d",
                description     = "A timestamp had to be heavily rounded", 
                consequences    = "Expected rhythm deviation", 
                severity        = Severity.MODERATE)
        MAJOR_TIMESTAMP_ROUNDING,

        @AnomalyDescription (
                name            = "Chaotic timestamp subsequence",
                detailsFormat   = "Chaotic timestamps at %d, with the following deltas: %s",
                description     = "A series of timestamps had variable offsets with main rhythm", 
                consequences    = "Allmost certain rhythm deviation", 
                severity        = Severity.CRITIC)
        CHAOTIC_TIMESTAMP_SUBSEQUENCE,

        @AnomalyDescription(
                name            = "Consecutive noteOn/noteOff event",
                detailsFormat   = "Note %s event at %d",
                description     = "Track contained two consecutive noteOn/noteOff events",
                consequences    = "Ignoring additional events",
                severity        = Severity.MODERATE)
        CONSECUTIVE_NOTE_ON_OR_OFF_EVENT;
    }

    private enum Procedure {

        @ProcedureDescription (name = "Loading MIDI file from disk")
        LOAD_MIDI_FILE_FROM_DISK,

        @ProcedureDescription (name = "Processing MIDI events")
        PROCESS_MIDI_EVENTS,

        @ProcedureDescription (name = "Converting MIDI ticks to timeunits")
        CONVERT_TICKS_TO_TIMEUNITS,

        @ProcedureDescription (name = "Build piece")
        BUILD_PIECE;
    }



    //======================================= FIELDS =============================================\\

    // Temporary fields
    private MIDISequence seq;
    private MIDITrack	 track;
    private MIDINote[]   noteBuffer;



    //======================================= METHODS ============================================\\

    //---------------------------------- PRIVATE CONSTRUCTOR -------------------------------------\\

    private MIDIFileReader() {
        super(Level.DEBUG);
    }


    //---------------------------------------- READ ----------------------------------------------\\

    public Piece<Stack<Note>> read(File midiFile) throws MIDIFileReaderException {
        // Initialize temporary fields
        seq = new MIDISequence(midiFile);
        track = null;
        noteBuffer = null;

        // Main procedures
        procedure(Procedure.LOAD_MIDI_FILE_FROM_DISK);
        Sequence apiSeq = loadSequenceFromDisk();

        procedure(Procedure.PROCESS_MIDI_EVENTS);
        processMIDIEvents(apiSeq);

        procedure(Procedure.CONVERT_TICKS_TO_TIMEUNITS);
        float divType = apiSeq.getDivisionType();
        int res       = apiSeq.getResolution();
        List<Map<Long, Integer>> conversionMaps = createTStoTUConvMaps(divType, res);

        // TODO fusionOddNotesWithTinyRests ?

        procedure(Procedure.BUILD_PIECE);
        return buildPiece(conversionMaps);
    }

    // - - - - - - - - - - - - - - - - - LOAD FILE FROM DISK - - - - - - - - - - - - - - - - - - -\\

    private Sequence loadSequenceFromDisk() throws MIDIFileReaderException {
        log(Level.INFO, "URL: \"" + seq.getFile().getAbsolutePath() + "\"");

        Sequence seq = null;

        try {
            seq = MidiSystem.getSequence(IO.getResourceAsStream(this.seq.getFile().getPath()));
        } catch (InvalidMidiDataException | IOException e) {
            signalException(Anomaly.INVALID_MIDI_FILE, MIDIFileReaderException.class, 
                    e, this.seq.getFile().getAbsolutePath());
        }

        log("MIDI sequence infos");
        log("%4s%-18s : %d", "", "Num of tracks", seq.getTracks().length);
        log("%4s%-18s : %.2f sec (%d ticks)", "", "Duration", 
                (double) seq.getMicrosecondLength() / 1000000.0, seq.getTickLength());
        log("%4s%-18s : %d ticks per %s", "", "Timing resolution", seq.getResolution(),
                (seq.getDivisionType() != Sequence.PPQ ? " " + "frame (" + seq.getDivisionType() + 
                        " frames per second) (SMPTE-based)" : "quarter note (tempo-based)"));
        return seq;
    }

    // - - - - - - - - - - - - - - - - - PROCESS MIDI EVENTS - - - - - - - - - - - - - - - - - - -\\

    private void processMIDIEvents(Sequence apiSeq) throws MIDIFileReaderException {
        // For all tracks
        for (int i = 0; i < apiSeq.getTracks().length; ++i) {
            // Log track number
            log(Level.INFO, 2, "Reading track %d", i + 1);

            // Create a MIDI track wrapper
            track = new MIDITrack();
            noteBuffer = new MIDINote[128];

            // Get API MIDI track
            Track apiTrack = apiSeq.getTracks()[i];

            // For each MIDI event in the track
            for (int j = 0; j < apiTrack.size(); ++j) {
                MidiEvent event = apiTrack.get(j);
                MidiMessage msg = event.getMessage();

                // Handle message type
                if (msg instanceof ShortMessage) {
                    processMessage((ShortMessage) msg, event.getTick());
                } else if (msg instanceof MetaMessage) {
                    processMessage((MetaMessage)  msg, event.getTick());
                } else {
                    processMessage((SysexMessage) msg, event.getTick());
                }
            }

            // Add MIDI track to sequence wrapper
            seq.addTrack(track);

            // Log stats
            log(Level.INFO, "Track total number of notes: %d (Tick: %d-%d)", 
                    track.getNotes().size(), track.getStartTick(), track.getEndTick());
        }
    }

    private void processMessage(MetaMessage msg, Long tick) throws MIDIFileReaderException {
        byte[] data = msg.getData();
        String strData = new String(data);
        MetaMessageType type = MetaMessageType.of(msg);
        boolean ignored = false;

        switch(type) {
        case CHANNEL_NUMBER:        track.setChannelNumber(data[0]);  break;
        case COPYRIGHT_NOTICE:      seq.setCopyrightNotice(strData);  break;
        case CUE_POINT:             track.addCuePoint(strData);       break;
        case INSTRUMENT_NAME:       track.setInstrumentName(strData); break;
        case LYRICS:                track.addLyrics(strData);         break;
        case MARKER:                track.addMarker(strData);         break;
        case MIDI_PORT:             track.setMidiPort(data[0]);       break;
        case TEXT:                  track.addText(strData);           break;
        case TRACK_NAME:            track.setName(strData);           break;
        case END_OF_TRACK:                                            break;
        case TIME_SIGNATURE:
            seq.setSignature(data[0], (int) Math.pow(2, data[1]));
            if (data[3] != MIDISequence.NUM_32TH_NOTES_PER_BEAT) {
                signalException(Anomaly.INVALID_MIDI_FILE, MIDIFileReaderException.class, 
                        "Unimplemented timestamp adjustment for sequence with num of 32th notes "
                        + "per beat different than " + MIDISequence.NUM_32TH_NOTES_PER_BEAT);
            }
            strData = seq.getRythmicSignature().toString();           break;
        case SET_TEMPO:
            double microSec = (double) ((data[0] << 16) + (data[1] << 8) + data[2]);
            double bmp = 60000000.0 / microSec;
            strData = String.format("%.2f", bmp);
            seq.addTempoChange(tick, bmp);                            break;
        case KEY_SIGNATURE:
            Tone tone = CircleOfFifths.toneAtPosition(data[0]);
            Quality quality = (data[1] == 0x00) ? Quality.MAJOR : Quality.MINOR;
            seq.setKey(Key.valueOf(tone, quality));                   break;
        default:
            ignored = true;
            break;
        }

        logMIDIMessage("MetaMessage", type.name(), ignored, tick, strData, data, false);
    }

    private void processMessage(SysexMessage msg, Long tick) {
        byte[] data = msg.getData();
        String msgType = msg.getStatus() == SysexMessage.SPECIAL_SYSTEM_EXCLUSIVE ? 
                "SPECIAL SYST. EXCL." : "SYSTEM EXCLUSIVE";
        logMIDIMessage("SysexMessage", msgType, true, tick, new String(data), data, false);
    }

    private void processMessage(ShortMessage msg, Long tick) {
        ChannelMessageCmd cmd = ChannelMessageCmd.of(msg);
        String strData = msg.getData1() + " " + msg.getData2();
        String msgCategory = "ChannelMessage";
        boolean ignored = false;
        boolean traceLvl = false;
        
        switch(cmd) {
        case NOTE_ON:
            byte hexNote  = (byte) msg.getData1();
            byte velocity = (byte) msg.getData2();

            if (noteBuffer[hexNote] != null) {
                signal(Anomaly.CONSECUTIVE_NOTE_ON_OR_OFF_EVENT, "on", tick);
                ignored = true;
            }
            noteBuffer[hexNote] = new MIDINote(hexNote, velocity, tick);
            traceLvl = true;
            break;

        case NOTE_OFF:
            hexNote  = (byte) msg.getData1();
            velocity = (byte) msg.getData2();

            MIDINote note = noteBuffer[hexNote];
            if (note == null) {
                signal(Anomaly.CONSECUTIVE_NOTE_ON_OR_OFF_EVENT, "off", tick);
                ignored = true;
            }
            note.setEndTick(tick);
            track.addNote(note); 
            noteBuffer[hexNote] = null;
            traceLvl = true;
            break;

        case PROGRAM_CHANGE:
            track.addInstrumentChange(msg.getData1(), tick);
            break;

        case SYSTEM_MESSAGE:
            strData = SystemCommonMessageType.of(msg).name();
            msgCategory = "System Message";
            ignored = true;
            break;

        default: 
            ignored = true;
        }

        logMIDIMessage(msgCategory, cmd.name(), ignored, tick, strData, null, traceLvl);
    }

    private void logMIDIMessage(String msgCategory, String msgType, boolean ignored, 
            Long tick, String strData, byte[] bytes, boolean traceLvl) {

        String msg = String.format("%s | %-14s | %-17s | %6d | %s %s", 
                ignored ? " Ignored " : "Processed", msgCategory,
                        msgType, tick, strData, toString(bytes));

        if (ignored) {
            signal(Anomaly.IGNORED_MIDI_EVENT, msg);
        } else {
            if (traceLvl) {
                log(Level.TRACE, LogEntry.NO_TITLE, 1, msg);
            } else {
                log(1, msg);
            }
        }
    }

    private static String toString(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        if (bytes.length == 0) {
            return "";
        }
        if (bytes.length > 8) {
            return "(...)";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('(').append(bytes[0]);
        for (int i = 1; i < bytes.length; ++i) {
            sb.append(' ');
            sb.append(Byte.toString(bytes[i]));
        }
        return sb.append(')').toString();
    }

    //- - - - - - - - - - - - - - - - CONVERT TICKS TO TIMUNITS - - - - - - - - - - - - - - - - - \\

    private List<Map<Long, Integer>> createTStoTUConvMaps(float divisionType, 
            int ticksPerQuarterNote) throws MIDIFileReaderException {

        if (divisionType != Sequence.PPQ) {
            signalException(Anomaly.INVALID_MIDI_FILE, MIDIFileReaderException.class, 
                    "SMPTE-based division type is unimplemented");
        }
        double ticksPer64thNote = ticksPerQuarterNote / (2 * MIDISequence.NUM_32TH_NOTES_PER_BEAT);

        if (ticksPer64thNote - (int) ticksPer64thNote != 0) {
            signalException(Anomaly.INVALID_MIDI_FILE, MIDIFileReaderException.class, 
                    "Sequence's num of ticks per 64th note is not integral");
        }
        List<Map<Long, Integer>> conversionMaps = new ArrayList<>(seq.getNumTracks());
        
        for (int i = 0; i < seq.getNumTracks(); ++i) {
            log(Level.INFO, 2, "Processing track " + (i + 1));

            // Create a sorted list of all timestamps
            Set<Long> allTimestamps = new HashSet<>();
            allTimestamps.addAll(seq.getTrack(i).getNotes().keySet());
            allTimestamps.addAll(seq.getTrack(i).getEndingNotes().keySet());
            List<Long> timestamps = new ArrayList<>(allTimestamps);
            Collections.sort(timestamps);
            
            if (timestamps.isEmpty()) {
                log("Ignoring empty track");
                conversionMaps.add(null);
            } else {
                conversionMaps.add(createTStoTUConversMap(timestamps, (int) ticksPer64thNote));
            }
        }
        
        return conversionMaps;
    }

    private Map<Long, Integer> createTStoTUConversMap(List<Long> timestamps, int ticksPer64thNote) {
        int currDelta = 0;
        double tuPerTick = 1.0 / (double) ticksPer64thNote;

        // Initialize conversion map
        Map<Long, Integer> timestampToTU = new LinkedHashMap<>();

        // For all timestamp (note start/end)
        for (int i = 0; i < timestamps.size(); ++i) {
            // Get rounded timeunit value
            long timestamp = timestamps.get(i);
            Long roundedTU = Math.round((double)(timestamp + currDelta) * tuPerTick);

            // Get timestamp delta
            long roundedTS = roundedTU * ticksPer64thNote;
            int deltaTS = (int) (roundedTS - timestamp);

            // Add rounded timeunit to conversion map
            timestampToTU.put(timestamp, roundedTU.intValue());
            
            // If timestamp delta did not change
            if (deltaTS == currDelta) {
                log("Timestamp:%6d >>%6d | Delta: %s >> %s| Timeunit: %d", timestamp, 
                        roundedTS, formatDelta(currDelta), formatDelta(deltaTS), roundedTU);
                continue;
            }

            // Count occurrences of future timestamp deltas
            Map<Integer,Integer> deltaOcc = new HashMap<>();
            for (int j = 1; j <= TIMESTAMP_OFFSET_CORREC_RADIUS && i + j < timestamps.size(); ++j) {
                long futureTS = timestamps.get(i+j);
                long futureRoundedTU = Math.round((double)(futureTS + currDelta) * tuPerTick);
                int futureDeltaTS = (int) (futureRoundedTU * ticksPer64thNote - futureTS);
                Integer count = deltaOcc.get(futureDeltaTS);
                deltaOcc.put(futureDeltaTS, count == null ? 1 : count + 1);
            }

            // Compute delta delta
            int deltaDelta = deltaTS - currDelta;
            Anomaly anomaly = Math.abs(deltaDelta) > MAXIMUM_ACCEPTABLE_TS_DELTA ? 
                    Anomaly.MAJOR_TIMESTAMP_ROUNDING : Anomaly.MINOR_TIMESTAMP_ROUNDING;

            signal(anomaly, timestamp, roundedTS, formatDelta(currDelta), 
                    formatDelta(deltaTS), formatDelta(deltaDelta), roundedTU);

            // If majority of future deltas are the same than current delta 
            if (deltaOcc.get(deltaTS) > (TIMESTAMP_OFFSET_CORREC_RADIUS - 1) / 2) {
                signal(Anomaly.TIMESTAMP_OFFSET_CHANGE);
                currDelta = deltaTS;
            } else if (deltaOcc.size() >= TIMESTAMP_OFFSET_CORREC_RADIUS) {
                signal(Anomaly.CHAOTIC_TIMESTAMP_SUBSEQUENCE, timestamp, deltaOcc.keySet()); 
            }
        }
        return timestampToTU;
    }

    private static String formatDelta(int delta) {
        return String.format("%s%-2d", delta < 0 ? "-" : "+", Math.abs(delta));
    }

    //- - - - - - - - - - - - - - - - - - - BUILD PIECE - - - - - - - - - - - - - - - - - - - - - \\

    private Piece<Stack<Note>> buildPiece(List<Map<Long, Integer>> conversionMaps) {
        PieceBuilder builder = new PieceBuilder(seq);
        registerSubmodule(builder);

        int partNum = 0;

        for (int i = 0; i < seq.getNumTracks(); ++i) {
            log(Level.INFO, 2, "Building track %d", i + 1);

            Map<Long, Set<MIDINote>> notes = seq.getTrack(i).getNotes();

            if (track.getNotes().isEmpty()) {
                log("Ignoring empty track");
                continue;
            }

            Map<Long, Integer> conversionMap = conversionMaps.get(i);
            
            for (Long timestamp : conversionMap.keySet()) {
                Set<MIDINote> timestampNotes = notes.get(timestamp);
                if (timestampNotes == null) {
                    continue;
                }
                for (MIDINote note : timestampNotes) {
                    int startTU  = conversionMap.get(note.startTick());
                    int lengthTU = conversionMap.get(note.endTick()) - startTU;
                    builder.add(note.getHexNote(), note.getVelocity(), partNum, startTU, lengthTU);
                }
            }
            ++partNum;
        }

        return builder.buildComponent();
    }
}