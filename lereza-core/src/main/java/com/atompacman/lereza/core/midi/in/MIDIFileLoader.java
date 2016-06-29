package com.atompacman.lereza.core.midi.in;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javax.annotation.Nullable;
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
import com.atompacman.toolkat.Log;
import com.atompacman.toolkat.annotations.Temporary;
import com.atompacman.toolkat.task.Anomaly;
import com.atompacman.toolkat.task.Anomaly.Severity;
import com.atompacman.toolkat.task.Task;
import com.atompacman.toolkat.task.TaskLogger;
import static com.atompacman.lereza.core.midi.in.MIDIFileLoader.TaskType.*;

import static com.atompacman.lereza.core.midi.in.MIDIFileLoader.AnomalyType.*;

public final class MIDIFileLoader {

    //
    //  ~  INNER TYPES  ~  //
    //

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

        public static Optional<MetaMessageType> of(MetaMessage msg) {
            int metaType = msg.getType();

            for (MetaMessageType type : MetaMessageType.values()) {
                if (metaType == type.typeByte) {
                    return Optional.of(type);
                }
            }
            Log.error("Unknown meta message type \"%s\"", metaType);
            return Optional.empty();
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

        public static Optional<ChannelMessageCmd> of(ShortMessage msg) {
            int msgCmd = msg.getCommand();

            for (ChannelMessageCmd cmd : ChannelMessageCmd.values()) {
                if (msgCmd == cmd.signifByte) {
                    return Optional.of(cmd);
                }
            }
            Log.error("Unknown channel message command \"%s\"", msgCmd);
            return Optional.empty();
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

        public static Optional<SystemCommonMessageType> of(ShortMessage msg) {
            int status = msg.getStatus();

            for (SystemCommonMessageType scmt : SystemCommonMessageType.values()) {
                if (status == scmt.statusByte) {
                    return Optional.of(scmt);
                }
            }
            Log.error("Unknown system common message \"%s\"", status);
            return Optional.empty();
        }
    }

    enum AnomalyType {
        
        @Anomaly.Description (
                name          = "Invalid MIDI file",
                detailsFormat = "Could not load MIDI sequence at \"%s\"",
                description   = "MIDI file is either inexistant or of invalid format", 
                consequences  = "Cannot continue MIDI file reading",
                severity      = Severity.FATAL)
        INVALID_MIDI_FILE,
        
        @Anomaly.Description (
                name          = "Ignored MIDI event",
                description   = "Processing for some MIDI event is not implemented", 
                consequences  = "May contain useful information", 
                severity      = Severity.MINIMAL)
        IGNORED_MIDI_EVENT,

        @Anomaly.Description(
                name          = "Consecutive noteOn/noteOff event",
                detailsFormat = "Note %s event at %d",
                description   = "Track contained two consecutive noteOn/noteOff events",
                consequences  = "Ignoring additional events",
                severity      = Severity.MODERATE)
        CONSECUTIVE_NOTE_ON_OR_OFF_EVENT;
    }

    enum TaskType {

        @Task.Description (nameFormat = "Loading MIDI file from disk")
        LOAD_MIDI_FILE_FROM_DISK,

        @Task.Description (nameFormat = "Processing MIDI events")
        PROCESS_MIDI_EVENTS,

        @Task.Description (nameFormat = "Processing track %d")
        PROCESS_TRACK_MIDI_EVENTS;
    }


    //
    //  ~  FIELDS  ~  //
    //

    private @Temporary MIDISequenceContent content;
    private @Temporary MIDITrack	       track;
    private @Temporary MIDINote[]          noteBuffer;
    
    private @Nullable TaskLogger taskLog;
    

    //
    //  ~  INIT  ~  //
    //

    public MIDIFileLoader() {
        this.content    = null;
        this.track      = null;
        this.noteBuffer = null;
        
        this.taskLog    = null;
    }


    //
    //  ~  LOAD  ~  //
    //

    public MIDISequenceContent load(File midiFile) throws MIDIFileLoaderException {
        content = new MIDISequenceContent(midiFile);
        
        taskLog = new TaskLogger(LOAD_MIDI_FILE_FROM_DISK);
        Sequence apiSeq = loadSequenceFromDisk();

        taskLog.startTask(PROCESS_MIDI_EVENTS);
        for (int i = 0; i < apiSeq.getTracks().length; ++i) {
            taskLog.startSubtask(PROCESS_TRACK_MIDI_EVENTS,  i + 1);
            processMIDIEvents(apiSeq.getTracks()[i]);
        }

        return content;
    }

    private Sequence loadSequenceFromDisk() throws MIDIFileLoaderException {
        File file = content.getSourceFile();
        String path = file.getAbsolutePath();
        
        taskLog.log(Level.INFO, "URL: \"%s\"", path);

        Sequence seq = null;

        try {
            seq = MidiSystem.getSequence(file);
        } catch (InvalidMidiDataException | IOException e) {
            taskLog.signalException(INVALID_MIDI_FILE, MIDIFileLoaderException.class, e, path);
        }

        float divisionType = seq.getDivisionType();
        if (divisionType != Sequence.PPQ) {
            taskLog.signalException(INVALID_MIDI_FILE, MIDIFileLoaderException.class, 
                    "SMPTE-based division type is unimplemented");
        }
        
        int resolution = seq.getResolution();
        double ticksPer64thNote = resolution / (2 * MIDISequenceContent.NUM_32TH_NOTES_PER_BEAT);
        if (ticksPer64thNote - (int) ticksPer64thNote != 0) {
            taskLog.signalException(INVALID_MIDI_FILE, MIDIFileLoaderException.class, 
                    "Sequence's num of ticks per 64th note is not integral");
        }
        content.setNumTicksPer64thNote((int) ticksPer64thNote);
        content.setLengthTU((int) ((double) seq.getTickLength() / ticksPer64thNote));
        
        double lengthSec = (double) seq.getMicrosecondLength() / 1000000.0;
        
        taskLog.log("Basic MIDI sequence info");
        taskLog.log("%4s%-24s : %d", "", "Num of tracks", seq.getTracks().length);
        taskLog.log("%4s%-24s : %.2f sec (%d ticks)","","Duration", lengthSec, seq.getTickLength());
        taskLog.log("%4s%-24s : %d ticks per quarter note", "", "Timing resolution", resolution);
        taskLog.log("%4s%-24s : %d", "", "Num ticks per 64th note", (int) ticksPer64thNote);

        return seq;
    }

    private void processMIDIEvents(Track apiTrack) throws MIDIFileLoaderException {
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
            taskLog.log("Ignoring empty track");
            return;
        }
        
        // Add MIDI track to sequence wrapper
        content.addTrack(track);

        // Log stats
        taskLog.log(Level.INFO, "Track total number of notes: %d (Tick: %d-%d)", 
                track.getNotes().size(), track.getStartTick(), track.getEndTick());
    }

    private void processMessage(MetaMessage msg, long tick) throws MIDIFileLoaderException {
        byte[] data = msg.getData();
        String strData = new String(data);
        MetaMessageType type = MetaMessageType.of(msg).orElse(null);
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
                taskLog.signalException(INVALID_MIDI_FILE, MIDIFileLoaderException.class, 
                        "Unimplemented tick adjustment for sequence with num of 32th notes "
                        + "per beat different than " + MIDISequenceContent.NUM_32TH_NOTES_PER_BEAT);
            }
            TimeSignature sign = TimeSignature.of((int) data[0], (int) Math.pow(2, data[1]));
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
        ChannelMessageCmd cmd = ChannelMessageCmd.of(msg).orElse(null);
        String strData = msg.getData1() + " " + msg.getData2();
        String msgCategory = "ChannelMessage";
        boolean ignored = false;
        boolean traceLvl = false;
        
        switch(cmd) {
        
        case NOTE_ON:
            byte hexNote  = (byte) msg.getData1();
            byte velocity = (byte) msg.getData2();

            if (noteBuffer[hexNote] != null) {
                taskLog.signal(CONSECUTIVE_NOTE_ON_OR_OFF_EVENT, "on", tick);
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
                taskLog.signal(CONSECUTIVE_NOTE_ON_OR_OFF_EVENT, "off", tick);
                ignored = true;
            } else {
                note.setEndTick(tick);
                track.addNote(note); 
                noteBuffer[hexNote] = null;
                traceLvl = true;
            }
            break;

        case PROGRAM_CHANGE:
            track.addInstrumentChange(msg.getData1(), tick);
            break;

        case SYSTEM_MESSAGE:
            strData = SystemCommonMessageType.of(msg).get().name();
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
            taskLog.signal(IGNORED_MIDI_EVENT, msg);
        } else {
            if (traceLvl) {
                taskLog.log(Level.TRACE, msg);
            } else {
                taskLog.log(1, msg);
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