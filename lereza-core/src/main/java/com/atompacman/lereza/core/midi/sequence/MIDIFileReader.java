package com.atompacman.lereza.core.midi.sequence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.atompacman.lereza.core.piece.Note;
import com.atompacman.lereza.core.piece.Piece;
import com.atompacman.lereza.core.piece.PieceBuilder;
import com.atompacman.lereza.core.piece.PieceBuilderSupervisor;
import com.atompacman.lereza.core.piece.Stack;
import com.atompacman.lereza.core.solfege.CircleOfFifths;
import com.atompacman.lereza.core.solfege.Key;
import com.atompacman.lereza.core.solfege.Quality;
import com.atompacman.lereza.core.solfege.Tone;
import com.atompacman.toolkat.IO;
import com.atompacman.toolkat.exception.Throw;
import com.atompacman.toolkat.module.AnomalyDescription;
import com.atompacman.toolkat.module.Module;
import com.atompacman.toolkat.module.ProcedureDescription;
import com.atompacman.toolkat.module.AnomalyDescription.Impact;
import com.atompacman.toolkat.module.AnomalyDescription.Recoverability;

public class MIDIFileReader extends Module {

    //====================================== SINGLETON ===========================================\\

    private static class InstanceHolder {
        private static final MIDIFileReader instance = new MIDIFileReader();
    }

    public static MIDIFileReader getInstance() {
        return InstanceHolder.instance;
    }


    
    //====================================== CONSTANTS ===========================================\\

    private static final Logger logger = LogManager.getLogger(MIDIFileReader.class);

    private static final int TIMESTAMP_OFFSET_CORREC_RADIUS = 5;
    private static final int MAXIMUM_ACCEPTABLE_TS_DELTA = 5;



    //===================================== INNER TYPES ==========================================\\

    private enum MetaMessageType {

        SEQUENCE_NUMBER 	(0x00),
        TEXT				(0x01),
        COPYRIGHT_NOTICE	(0x02),
        TRACK_NAME			(0x03),
        INSTRUMENT_NAME		(0x04),
        LYRICS				(0x05),
        MARKER				(0x06),
        CUE_POINT			(0x07),
        CHANNEL_NUMBER		(0x20),
        MIDI_PORT			(0x21),
        END_OF_TRACK		(0x2F),
        SET_TEMPO			(0x51),
        SMPTE_OFFSET		(0x54),
        TIME_SIGNATURE		(0x58),
        KEY_SIGNATURE		(0x59),
        SEQUENCER_SPECIFIC	(0x7F);


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
            logger.error("Unknown meta message type \"" + metaType + "\"");
            return null;
        }
    }

    private enum ChannelMessageCmd {

        NOTE_OFF 			(ShortMessage.NOTE_OFF),
        NOTE_ON				(ShortMessage.NOTE_ON),
        POLY_PRESSURE		(ShortMessage.POLY_PRESSURE),
        CONTROL_CHANGE		(ShortMessage.CONTROL_CHANGE),
        PROGRAM_CHANGE		(ShortMessage.PROGRAM_CHANGE),
        CHANNEL_PRESSURE	(ShortMessage.CHANNEL_PRESSURE),
        PITCH_BEND			(ShortMessage.PITCH_BEND),
        SYSTEM_MESSAGE		(0xF0);


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
            logger.error("Unknown channel message command \"" + msgCmd + "\"");
            return null;
        }
    }

    private enum SystemCommonMessageType {

        MIDI_TIME_CODE 			(ShortMessage.MIDI_TIME_CODE),
        SONG_POSITION_POINTER 	(ShortMessage.SONG_POSITION_POINTER),
        SONG_SELECT 			(ShortMessage.SONG_SELECT),
        TUNE_REQUEST 			(ShortMessage.TUNE_REQUEST),
        END_OF_EXCLUSIVE 		(ShortMessage.END_OF_EXCLUSIVE),
        TIMING_CLOCK 			(ShortMessage.TIMING_CLOCK),
        START 					(ShortMessage.START),
        CONTINUE 				(ShortMessage.CONTINUE),
        STOP 					(ShortMessage.STOP),
        ACTIVE_SENSING 			(ShortMessage.ACTIVE_SENSING),
        SYSTEM_RESET 			(ShortMessage.SYSTEM_RESET);


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
            logger.error("Unknown system common message \"" + status + "\"");
            return null;
        }
    }

    private enum AN {
        
        @AnomalyDescription (
                name            = "Ignored MIDI event", 
                description     = "Processing for some MIDI event is not implemented", 
                consequences    = "May contain useful information", 
                impact          = Impact.MINIMAL, 
                recoverability  = Recoverability.NORMAL,
                assocModule     = MIDIFileReader.class)
        IGNORED_MIDI_EVENT,

        @AnomalyDescription (
                name            = "Timestamp offset change", 
                description     = "A change in timestamp rounding delta", 
                consequences    = "Possible rhythm deviation", 
                impact          = Impact.MINIMAL, 
                recoverability  = Recoverability.UNKNOWN,
                assocModule     = MIDIFileReader.class)
        TIMESTAMP_OFFSET_CHANGE,

        @AnomalyDescription (
                name            = "Minor timestamp rounding", 
                description     = "A timestamp had to be slightly rounded", 
                consequences    = "Possible rhythm deviation", 
                impact          = Impact.MINIMAL, 
                recoverability  = Recoverability.UNKNOWN,
                assocModule     = MIDIFileReader.class)
        MINOR_TIMESTAMP_ROUNDING,

        @AnomalyDescription (
                name            = "Major timestamp rounding", 
                description     = "A timestamp had to be heavily rounded", 
                consequences    = "Expected rhythm deviation", 
                impact          = Impact.MODERATE, 
                recoverability  = Recoverability.UNKNOWN,
                assocModule     = MIDIFileReader.class)
        MAJOR_TIMESTAMP_ROUNDING,

        @AnomalyDescription (
                name            = "Chaotic timestamp subsequence", 
                description     = "A series of timestamps had variable offsets with main rhythm", 
                consequences    = "Allmost certain rhythm deviation", 
                impact          = Impact.CRITIC, 
                recoverability  = Recoverability.UNKNOWN,
                assocModule     = MIDIFileReader.class)
        CHAOTIC_TIMESTAMP_SUBSEQUENCE;
    }

    private enum CP {

        @ProcedureDescription (
                name = "Loading MIDI file from disk", 
                isMandatory = true)
        LOAD_MIDI_FILE_FROM_DISK,

        @ProcedureDescription (
                name = "Processing MIDI events", 
                isMandatory = true)
        PROCESS_MIDI_EVENTS,

        @ProcedureDescription (
                name = "Converting MIDI ticks to timeunits", 
                isMandatory = true)
        CONVERT_TICKS_TO_TIMEUNITS,

        @ProcedureDescription (
                name = "Build piece", 
                isMandatory = true)
        BUILD_PIECE;
    }



    //======================================= FIELDS =============================================\\

    // Temporary fields
    private MIDISequence seq;
    private MIDITrack	 track;



    //======================================= METHODS ============================================\\

    //---------------------------------- PRIVATE CONSTRUCTOR -------------------------------------\\

    private MIDIFileReader() {
        
    }
    
    
    //---------------------------------------- READ ----------------------------------------------\\

    public Piece<Stack<Note>> read(File midiFile) throws MIDIFileReaderException {
        // Initialize temporary fields
        seq = new MIDISequence(midiFile);
        track = null;
        report.setVerbose(Level.DEBUG);
        
        // Main steps
        report.checkpoint(CP.LOAD_MIDI_FILE_FROM_DISK);		
        Sequence midiSeq = loadSequenceFromDisk();

        report.checkpoint(CP.PROCESS_MIDI_EVENTS);
        processMIDIEvents(midiSeq);

        report.checkpoint(CP.CONVERT_TICKS_TO_TIMEUNITS);
        convertTicksToTimunits(midiSeq.getDivisionType(), midiSeq.getResolution());

        report.checkpoint(CP.BUILD_PIECE);
        return buildPiece();
    }

    // - - - - - - - - - - - - - - - - - LOAD FILE FROM DISK - - - - - - - - - - - - - - - - - - -\\

    private Sequence loadSequenceFromDisk() throws MIDIFileReaderException {
        String file = seq.getFile().getPath();
        report.log(Level.INFO, "URL: \"" + file + "\"");

        Sequence seq = null;

        try {
            seq = MidiSystem.getSequence(IO.getResourceasStream(file));
        } catch (InvalidMidiDataException | IOException e) {
            Throw.a(MIDIFileReaderException.class, "Could not load MIDI sequence "
                    + "at \"" + this.seq.getFile().getAbsolutePath() + "\"", e);
        }

        report.log("MIDI sequence infos");
        report.log("%4s%-18s : %d", "", "Num of tracks", seq.getTracks().length);
        report.log("%4s%-18s : %.2f%s", "", "Duration", (double) seq.getMicrosecondLength() 
                / 1000000.0, " sec (" + seq.getTickLength() + " ticks)");
        report.log("%4s%-18s : %s", "", "Timing resolution", seq.getResolution() + " ticks per " +
                (seq.getDivisionType() != Sequence.PPQ ? " " + "frame (" + seq.getDivisionType() + 
                        " frames per second) (SMPTE-based)" : "quarter note (tempo-based)"));
        return seq;
    }

    // - - - - - - - - - - - - - - - - - PROCESS MIDI EVENTS - - - - - - - - - - - - - - - - - - -\\

    private void processMIDIEvents(Sequence midiSeq) {
        for (int i = 0; i < midiSeq.getTracks().length; ++i) {
            report.log(Level.INFO, 2, "Reading track " + (i + 1));
            track = new MIDITrack();
            Track midiTrack = midiSeq.getTracks()[i];

            for (int j = 0; j < midiTrack.size(); ++j) {
                processEvent(midiTrack.get(j));
            }
            seq.addTrack(track);
        }
    }

    private void processEvent(MidiEvent event) {
        MidiMessage msg = event.getMessage();
        if (msg instanceof ShortMessage) {
            processMessage((ShortMessage) msg, event.getTick());
        } else if (msg instanceof MetaMessage) {
            processMessage((MetaMessage) msg, event.getTick());
        } else {
            processMessage((SysexMessage) msg);
        }
    }

    private void processMessage(MetaMessage msg, Long tick) {
        byte[] data = msg.getData();
        String strData = new String(data);
        MetaMessageType type = MetaMessageType.of(msg);
        boolean ignored = false;

        switch(type) {
        case CHANNEL_NUMBER:		track.setChannelNumber(data[0]);		break;
        case COPYRIGHT_NOTICE:		seq.setCopyrightNotice(strData);		break;
        case CUE_POINT:				track.addCuePoint(strData); 			break;
        case END_OF_TRACK:													break;
        case INSTRUMENT_NAME:		track.setInstrumentName(strData);		break;
        case KEY_SIGNATURE:													
            Tone tone = CircleOfFifths.toneAtPosition(data[0]);
            Quality quality = (data[1] == 0x00) ? Quality.MAJOR : Quality.MINOR;
            seq.setKey(Key.valueOf(tone, quality));							break;
        case LYRICS:				track.addLyrics(strData);				break;
        case MARKER:				track.addMarker(strData);				break;
        case MIDI_PORT:				track.setMidiPort(data[0]);				break;
        case SET_TEMPO:
            double microSec = (double) ((data[0] << 16) + (data[1] << 8) + data[2]);
            seq.addTempoChange(tick, 60000000.0 / microSec);				break;
        case TEXT:					track.addText(strData);					break;
        case TIME_SIGNATURE:
            seq.setSignature(data[0], (int) Math.pow(2, data[1]));
            seq.setNum32thNotesPerBeat(data[3]);							break;
        case TRACK_NAME:			track.setName(strData);					break;
        default:		
            ignored = true;
            break;
        }

        String toLog = String.format("%s %-17s |  MetaMessage   | %s %s",
                ignored ? "[ Ignored ]" : "[Processed]", type.name(), strData, toString(data));

        if (ignored) {
            report.signal(AN.IGNORED_MIDI_EVENT, toLog);
        } else {
            report.log(toLog);
        }
    }

    private void processMessage(SysexMessage msg) {
        byte[] data = msg.getData();
        String strData = new String(data);

        report.signal(AN.IGNORED_MIDI_EVENT, "[ Ignored ] %-17s |  SysexMessage  | "
                + "%s %s", msg.getStatus() == SysexMessage.SPECIAL_SYSTEM_EXCLUSIVE ? 
                        "SPECIAL SYST. EXCL." : "SYSTEM EXCLUSIVE", strData, toString(data));
    }

    private void processMessage(ShortMessage msg, Long tick) {
        ChannelMessageCmd cmd = ChannelMessageCmd.of(msg);
        boolean ignored = false;

        switch(cmd) {
        case NOTE_OFF: case NOTE_ON: 	
            track.addNote((byte) msg.getData1(), (byte) msg.getData2(), tick); 
            break;

        case PROGRAM_CHANGE:			
            track.addInstrumentChange(msg.getData1(), tick);					
            break;

        case SYSTEM_MESSAGE:			
            processSystemMessage(msg); 						
            break;

        default: 
            ignored = true;
        }

        String toLog = String.format("%s %-17s | ChannelMessage | %3d %3d",	ignored ? 
                "[ Ignored ]" : "[Processed]", cmd.name(), msg.getData1(), msg.getData2());

        if (ignored) {
            report.signal(AN.IGNORED_MIDI_EVENT, toLog);
        } else {
            report.log(Level.DEBUG, toLog);
        }
    }

    private void processSystemMessage(ShortMessage msg) {
        String msgName = SystemCommonMessageType.of(msg).name();
        report.signal(AN.IGNORED_MIDI_EVENT, "[ Ignored ] %-17s | System Message |", msgName);
    }

    private static String toString(byte[] bytes) {
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

    private void convertTicksToTimunits(float divisionType, int ticksPerQuarterNote) 
            throws MIDIFileReaderException {

        if (divisionType != Sequence.PPQ) {
            Throw.a(MIDIFileReaderException.class, "SMPTE-based division type is unimplemented");
        }
        if (seq.getNum32thNotesPerBeat() != MIDISequence.DEFAULT_NUM_32TH_NOTES_PER_BEAT) {
            Throw.a(MIDIFileReaderException.class, "Unimplemented timestamp adjustment "
                    + "for sequence with num of 32th notes per beat different than "
                    + MIDISequence.DEFAULT_NUM_32TH_NOTES_PER_BEAT);
        }
        if (ticksPerQuarterNote % 8 != 0) {
            Throw.a(MIDIFileReaderException.class, "Sequence's num of "
                    + "ticks per 32th note is not integral");
        }

        for (int i = 0; i < seq.getNumTracks(); ++i) {
            report.log(Level.INFO, 2, "Processing track " + (i + 1));

            Map<Long, Set<MIDINote>> notes = seq.getTrack(i).getNotes();

            if (notes.isEmpty()) {
                report.log("Ignoring empty track");
                continue;
            }
            convertTicksToTimeunits(notes, ticksPerQuarterNote / 8);
        }
    }

    private void convertTicksToTimeunits(Map<Long, Set<MIDINote>> notes, int ticksPer32thNote) {
        int currDelta = 0;
        double tuPerTick = 1.0 / (double) ticksPer32thNote;
        Map<Long, Set<MIDINote>> converted = new HashMap<>();

        List<Long> timestamps = new ArrayList<>(notes.keySet());
        Collections.sort(timestamps);

        for (int i = 0; i < timestamps.size(); ++i) {
            long timestamp = timestamps.get(i);
            Long roundedTU = Math.round((double)(timestamp + currDelta) * tuPerTick);
            long roundedTS = roundedTU * ticksPer32thNote;
            int deltaTS = (int) (roundedTS - timestamp);

            if (deltaTS != currDelta) {
                Map<Integer,Integer> deltaOcc = new HashMap<>();
                deltaOcc.put(deltaTS, 0);

                for (int j=1; j <= TIMESTAMP_OFFSET_CORREC_RADIUS && i+j < timestamps.size(); ++j) {
                    long futureTS = timestamps.get(i+j);
                    Long futureRndTU = Math.round((double)(futureTS + currDelta) * tuPerTick);
                    int futureDeltaTS = (int) (futureRndTU * ticksPer32thNote - futureTS);

                    Integer count = deltaOcc.get(futureDeltaTS);
                    deltaOcc.put(futureDeltaTS, count == null ? 1 : count + 1);
                }

                int deltaDelta = deltaTS - currDelta;
                AN anomaly = Math.abs(deltaDelta) > MAXIMUM_ACCEPTABLE_TS_DELTA ? 
                        AN.MAJOR_TIMESTAMP_ROUNDING : AN.MINOR_TIMESTAMP_ROUNDING;

                report.signal(anomaly, "Timestamp:%6d >>%6d | Delta: %s >> %s (%s) | Timeunit: %d", 
                        timestamp, roundedTS, formatDelta(currDelta), formatDelta(deltaTS), 
                        formatDelta(deltaDelta), roundedTU);

                if (deltaOcc.get(deltaTS) > (TIMESTAMP_OFFSET_CORREC_RADIUS - 1) / 2) {
                    report.signal(AN.TIMESTAMP_OFFSET_CHANGE);
                    currDelta = deltaTS;
                } else if (deltaOcc.size() >= TIMESTAMP_OFFSET_CORREC_RADIUS) {
                    report.signal(AN.CHAOTIC_TIMESTAMP_SUBSEQUENCE, "Chaotic timestamps at " + 
                            timestamp +	", with the following deltas: " + deltaOcc.keySet()); 
                }
            } else {
                report.log("Timestamp:%6d >>%6d | Delta: %s >> %s       | Timeunit: %d", timestamp, 
                        roundedTS, formatDelta(currDelta), formatDelta(deltaTS), roundedTU);
            }

            Set<MIDINote> noteSet = notes.get(timestamp);
            converted.put(roundedTU, noteSet);
        }

        notes.clear();
        notes.putAll(converted);
    }

    private static String formatDelta(int delta) {
        return String.format("%s%-2d", delta < 0 ? "-" : "+", Math.abs(delta));
    }

    private int convertNotesLenToTU(Map<Long, Set<MIDINote>> notes, int ticksPer32thNote) {
        int finalTU = 0;
        return finalTU;
    }

    //- - - - - - - - - - - - - - - - - - - BUILD PIECE - - - - - - - - - - - - - - - - - - - - - \\

    private Piece<Stack<Note>> buildPiece() {
        PieceBuilderSupervisor supervisor = new PieceBuilderSupervisor();
        PieceBuilder builder = PieceBuilder.create(seq, supervisor);

        int partNum = 0;

        for (int i = 0; i < seq.getNumTracks(); ++i) {
            report.log(Level.INFO, 2, "Building track " + (i + 1));

            seq.getTrack(i);

            if (track.getNotes().isEmpty()) {
                report.log("Ignoring empty track");
                continue;
            }

            for (Entry<Long, Set<MIDINote>> entry : track.getNotes().entrySet()) {
                // TODO fusionOddNotesWithTinyRests ?
                for (MIDINote note : entry.getValue()) {
                    builder.add(note.getHexNote(), note.getVelocity(), partNum, 
                            entry.getKey().intValue(), note.getLength());
                }
            }
            ++partNum;
        }

        return builder.buildComponent();
    }


    //--------------------------------------- FINALIZE -------------------------------------------\\

    public void finalize() {

    }
}