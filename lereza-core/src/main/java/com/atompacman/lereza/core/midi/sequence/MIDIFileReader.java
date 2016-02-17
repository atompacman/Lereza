package com.atompacman.lereza.core.midi.sequence;

import java.io.File;
import java.io.IOException;

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

import com.atompacman.lereza.core.theory.CircleOfFifths;
import com.atompacman.lereza.core.theory.Key;
import com.atompacman.lereza.core.theory.Quality;
import com.atompacman.lereza.core.theory.TimeSignature;
import com.atompacman.lereza.core.theory.Tone;
import com.atompacman.toolkat.misc.Log;
import com.atompacman.toolkat.module.AnomalyDescription;
import com.atompacman.toolkat.module.AnomalyDescription.Severity;
import com.atompacman.toolkat.module.BaseModule;
import com.atompacman.toolkat.module.LogEntry;
import com.atompacman.toolkat.module.Module;
import com.atompacman.toolkat.module.ProcedureDescription;

public class MIDIFileReader extends Module {

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

        private final int typeByte;

        private MetaMessageType(int typeByte) {
            this.typeByte = typeByte;
        }

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

        private final int signifByte;

        private ChannelMessageCmd(int signifByte) {
            this.signifByte = signifByte;
        }

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

        private final int statusByte;

        private SystemCommonMessageType(int statusByte) {
            this.statusByte = statusByte;
        }

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

        @AnomalyDescription(
                name            = "Consecutive noteOn/noteOff event",
                detailsFormat   = "Note %s event at %d",
                description     = "Track contained two consecutive noteOn/noteOff events",
                consequences    = "Ignoring additional events",
                severity        = Severity.MODERATE)
        CONSECUTIVE_NOTE_ON_OR_OFF_EVENT;
    }

    private enum Procedure {

        @ProcedureDescription (nameFormat = "Loading MIDI file from disk")
        LOAD_MIDI_FILE_FROM_DISK,

        @ProcedureDescription (nameFormat = "Processing MIDI events")
        PROCESS_MIDI_EVENTS,

        @ProcedureDescription (nameFormat = "Processing track %d")
        PROCESS_TRACK_MIDI_EVENTS;
    }



    //======================================= FIELDS =============================================\\

    // Temporaries
    private MIDISequenceContent content;
    private MIDITrack	        track;
    private MIDINote[]          noteBuffer;

    

    //======================================= METHODS ============================================\\

    //------------------------------------- CONSTRUCTORS -----------------------------------------\\

    public MIDIFileReader(BaseModule parentModule) {
        super(Level.DEBUG, parentModule);
        
        // Temporaries
        this.content    = null;
        this.track      = null;
        this.noteBuffer = null;
    }


    //---------------------------------------- READ ----------------------------------------------\\

    public MIDISequenceContent read(File midiFile) throws MIDIFileReaderException {
        content = new MIDISequenceContent(midiFile);
        
        procedure(Procedure.LOAD_MIDI_FILE_FROM_DISK);
        Sequence apiSeq = loadSequenceFromDisk();

        procedure(Procedure.PROCESS_MIDI_EVENTS);
        for (int i = 0; i < apiSeq.getTracks().length; ++i) {
            subprocedure(Procedure.PROCESS_TRACK_MIDI_EVENTS, i + 1);
            processMIDIEvents(apiSeq.getTracks()[i]);
        }

        return content;
    }

    private Sequence loadSequenceFromDisk() throws MIDIFileReaderException {
        File file = content.getSourceFile();
        String path = file.getAbsolutePath();
        
        log(Level.INFO, "URL: \"%s\"", path);

        Sequence seq = null;

        try {
            seq = MidiSystem.getSequence(file);
        } catch (InvalidMidiDataException | IOException e) {
            signalException(Anomaly.INVALID_MIDI_FILE, MIDIFileReaderException.class, e, path);
        }

        float divisionType = seq.getDivisionType();
        if (divisionType != Sequence.PPQ) {
            signalException(Anomaly.INVALID_MIDI_FILE, MIDIFileReaderException.class, 
                    "SMPTE-based division type is unimplemented");
        }
        
        int resolution = seq.getResolution();
        double ticksPer64thNote = resolution / (2 * MIDISequenceContent.NUM_32TH_NOTES_PER_BEAT);
        if (ticksPer64thNote - (int) ticksPer64thNote != 0) {
            signalException(Anomaly.INVALID_MIDI_FILE, MIDIFileReaderException.class, 
                    "Sequence's num of ticks per 64th note is not integral");
        }
        content.setNumTicksPer64thNote((int) ticksPer64thNote);
        content.setLengthTU((int) ((double) seq.getTickLength() / ticksPer64thNote));
        
        double lengthSec = (double) seq.getMicrosecondLength() / 1000000.0;
        
        log("Basic MIDI sequence info");
        log("%4s%-24s : %d", "", "Num of tracks", seq.getTracks().length);
        log("%4s%-24s : %.2f sec (%d ticks)", "", "Duration", lengthSec, seq.getTickLength());
        log("%4s%-24s : %d ticks per quarter note", "", "Timing resolution", resolution);
        log("%4s%-24s : %d", "", "Num ticks per 64th note", (int) ticksPer64thNote);

        return seq;
    }

    private void processMIDIEvents(Track apiTrack) throws MIDIFileReaderException {
        // Create a MIDI track wrapper
        track = new MIDITrack();
        noteBuffer = new MIDINote[128];

        // For each MIDI event in the track
        for (int j = 0; j < apiTrack.size(); ++j) {
            MidiEvent event = apiTrack.get(j);
            MidiMessage msg = event.getMessage();
            long tick = event.getTick();

            // Handle message type
            if (msg instanceof ShortMessage) {
                processMessage((ShortMessage) msg, tick);
            } else if (msg instanceof MetaMessage) {
                processMessage((MetaMessage)  msg, tick);
            } else {
                processMessage((SysexMessage) msg, tick);
            }
        }

        // Skip empty tracks
        if (track.getNotes().isEmpty()) {
            log("Ignoring empty track");
            return;
        }
        
        // Add MIDI track to sequence wrapper
        content.addTrack(track);

        // Log stats
        log(Level.INFO, "Track total number of notes: %d (Tick: %d-%d)", 
                track.getNotes().size(), track.getStartTick(), track.getEndTick());
    }

    private void processMessage(MetaMessage msg, long tick) throws MIDIFileReaderException {
        byte[] data = msg.getData();
        String strData = new String(data);
        MetaMessageType type = MetaMessageType.of(msg);
        boolean ignored = false;

        switch(type) {
        case CHANNEL_NUMBER:   track.setChannelNumber(data[0]);     break;
        case COPYRIGHT_NOTICE: content.setCopyrightNotice(strData); break;
        case CUE_POINT:        track.addCuePoint(strData);          break;
        case INSTRUMENT_NAME:  track.setInstrumentName(strData);    break;
        case LYRICS:           track.addLyrics(strData);            break;
        case MARKER:           track.addMarker(strData);            break;
        case MIDI_PORT:        track.setMidiPort(data[0]);          break;
        case TEXT:             track.addText(strData);              break;
        case TRACK_NAME:       track.setName(strData);              break;
        case END_OF_TRACK:                                          break;
        
        case TIME_SIGNATURE:
            if (data[3] != MIDISequenceContent.NUM_32TH_NOTES_PER_BEAT) {
                signalException(Anomaly.INVALID_MIDI_FILE, MIDIFileReaderException.class, 
                        "Unimplemented tick adjustment for sequence with num of 32th notes "
                        + "per beat different than " + MIDISequenceContent.NUM_32TH_NOTES_PER_BEAT);
            }
            TimeSignature sign = TimeSignature.valueOf((int) data[0], (int) Math.pow(2, data[1]));
            content.addTimeSignatureChange(sign, tick);
            break;
            
        case SET_TEMPO:
            double microSec = (double) ((data[0] << 16) + (data[1] << 8) + data[2]);
            double bmp = 60000000.0 / microSec;
            strData = String.format("%.2f", bmp);
            content.addTempoChange(bmp, tick); 
            break;
            
        case KEY_SIGNATURE:
            Tone tone = CircleOfFifths.toneAtPosition(data[0]);
            Quality quality = (data[1] == 0x00) ? Quality.MAJOR : Quality.MINOR;
            content.addKeyChange(Key.of(tone, quality), tick);
            break;

        default:
            ignored = true;
            break;
        }

        logMIDIMessage("MetaMessage", type.name(), ignored, tick, strData, data, false);
    }
    
    private void processMessage(SysexMessage msg, long tick) {
        byte[] data = msg.getData();
        String msgType = msg.getStatus() == SysexMessage.SPECIAL_SYSTEM_EXCLUSIVE ? 
                "SPECIAL SYST. EXCL." : "SYSTEM EXCLUSIVE";
        logMIDIMessage("SysexMessage", msgType, true, tick, new String(data), data, false);
    }

    private void processMessage(ShortMessage msg, long tick) {
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

        String msg = String.format("%s | %-14s | %-17s | %6d | %s %s", ignored ? " Ignored " : 
                     "Processed", msgCategory, msgType, tick, strData, msgBytesToString(bytes));

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

    private static String msgBytesToString(byte[] bytes) {
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
}