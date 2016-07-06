package com.atompacman.lereza.core.midi.in;

import static com.atompacman.lereza.core.midi.in.MIDIFileLoader.Anomaly.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiFileFormat;
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
import com.atompacman.toolkat.annotations.HelperMethod;
import com.atompacman.toolkat.annotations.SubMethodOf;
import com.atompacman.toolkat.annotations.Temporary;
import com.atompacman.toolkat.task.AnomalyDescription;
import com.atompacman.toolkat.task.AnomalyDescription.Severity;
import com.atompacman.toolkat.task.TaskMonitor;

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
        SEQUENCER_SPECIFIC  (0x7F),
        UNKNOWN             (0xFF);

        
        //
        //  ~  INIT  ~  //
        //

        private MetaMessageType(int typeByte) {
            META_MSG_TYPES[typeByte] = this;
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
        SYSTEM_MESSAGE      (0xF0),
        UNKNOWN             (0xFF);

        
        //
        //  ~  INIT  ~  //
        //

        private ChannelMessageCmd(int typeByte) {
            CHANNEL_MSG_CMDS[typeByte] = this;
        }
    }

    private enum SystemCommonMessageType {

        MIDI_TIME_CODE        (ShortMessage.MIDI_TIME_CODE),
        SONG_POSITION_POINTER (ShortMessage.SONG_POSITION_POINTER),
        SONG_SELECT           (ShortMessage.SONG_SELECT),
        TUNE_REQUEST          (ShortMessage.TUNE_REQUEST),
        END_OF_EXCLUSIVE      (ShortMessage.END_OF_EXCLUSIVE),
        TIMING_CLOCK          (ShortMessage.TIMING_CLOCK),
        START                 (ShortMessage.START),
        CONTINUE              (ShortMessage.CONTINUE),
        STOP                  (ShortMessage.STOP),
        ACTIVE_SENSING        (ShortMessage.ACTIVE_SENSING),
        SYSTEM_RESET          (ShortMessage.SYSTEM_RESET),
        UNKNOWN               (0xFF);

        
        //
        //  ~  INIT  ~  //
        //

        private SystemCommonMessageType(int typeByte) {
            SYS_COMMON_MSG_TYPES[typeByte] = this;
        }
    }

    // Not private to allow static import
    enum Anomaly {
        
        @AnomalyDescription (
                name          = "MIDI file not found",
                detailsFormat = "Could not find file \"%s\"",
                consequences  = "Cannot continue MIDI file reading",
                severity      = Severity.FATAL)
        FILE_NOT_FOUND,
        
        @AnomalyDescription (
                name          = "Invalid MIDI data",
                detailsFormat = "Could not parse MIDI data in file \"%s\": %s",
                consequences  = "Cannot continue MIDI file reading",
                severity      = Severity.FATAL)
        INVALID_MIDI_DATA,
        
        @AnomalyDescription (
                name          = "Ignored MIDI event",
                detailsFormat = "%s",
                description   = "Processing for some MIDI event is not implemented", 
                consequences  = "May contain useful information", 
                severity      = Severity.MINIMAL)
        IGNORED_MIDI_EVENT,

        @AnomalyDescription(
                name          = "Consecutive noteOn/noteOff event",
                detailsFormat = "Note %s event at %d",
                description   = "Track contained two consecutive noteOn/noteOff events",
                consequences  = "Ignoring additional events",
                severity      = Severity.MODERATE)
        CONSECUTIVE_NOTE_ON_OR_OFF_EVENT;
    }


    //
    //  ~  CONSTANTS  ~  //
    //

    private static final MetaMessageType[]         META_MSG_TYPES;
    private static final ChannelMessageCmd[]       CHANNEL_MSG_CMDS;
    private static final SystemCommonMessageType[] SYS_COMMON_MSG_TYPES;

    
    //
    //  ~  FIELDS  ~  //
    //

    private @Temporary MIDISequenceContent content;
    private @Temporary MIDITrack	       track;
    private @Temporary MIDINote[]          noteBuffer;
    
    private @Temporary TaskMonitor monitor;
    

    //
    //  ~  INIT  ~  //
    //

    static {
        META_MSG_TYPES       = new MetaMessageType        [256];
        CHANNEL_MSG_CMDS     = new ChannelMessageCmd      [256];
        SYS_COMMON_MSG_TYPES = new SystemCommonMessageType[256];
        replaceEmptyByEnum(META_MSG_TYPES, MetaMessageType.UNKNOWN);
        replaceEmptyByEnum(CHANNEL_MSG_CMDS, ChannelMessageCmd.UNKNOWN);
        replaceEmptyByEnum(SYS_COMMON_MSG_TYPES, SystemCommonMessageType.UNKNOWN);
    }
    
    private static <T extends Enum<?>> void replaceEmptyByEnum(T[] table, T value) {
        for (int i = 0; i < table.length; ++i) {
            if (table[i] == null) {
                table[i] = value;
            }
        }
    }
    
    public static MIDIFileLoader of() {
        return new MIDIFileLoader();
    }
    
    private MIDIFileLoader() {
        this.content    = null;
        this.track      = null;
        this.noteBuffer = null;
        
        this.monitor    = null;
    }
    
    
    //
    //  ~  LOAD  ~  //
    //

    public MIDISequenceContent load(File        midiFile, 
                                    TaskMonitor monitor) throws MIDIFileLoaderException {
        
        content = new MIDISequenceContent(midiFile);
        
        Sequence apiSeq = monitor.executeSubtaskExcep("Load MIDI file from disk", mon -> {
            this.monitor = mon;
            return loadSequenceFromDisk();
        });
        
        monitor.executeSubtaskExcep("Process MIDI events", mon -> {
            for (int i = 0; i < apiSeq.getTracks().length; ++i) {
                mon.executeSubtaskExcep("Track" + (i + 1), i, (submon, j) -> {
                    this.monitor = submon;
                    processMIDIEvents(apiSeq.getTracks()[j]);
                });
            }
        });

        return content;
    }

    @SubMethodOf("load")
    private Sequence loadSequenceFromDisk() throws MIDIFileLoaderException {
        // Get file path
        final File   file = content.getSourceFile();
        final String path = file.getAbsolutePath();
        monitor.log(Level.INFO, "URL: \"%s\"", path);

        // Read file format
        MidiFileFormat format = null;
        try {
            format = MidiSystem.getMidiFileFormat(file);
        } catch (IOException e) {
            return monitor.signalException(FILE_NOT_FOUND, MIDIFileLoaderException.class, e, path);
        } catch (InvalidMidiDataException e) {
            return monitor.signalException(INVALID_MIDI_DATA, MIDIFileLoaderException.class,e, path,
                    "Could not determine MIDI file format");
        }
        
        // Perform format checks
        if (!MidiSystem.isFileTypeSupported(format.getType())) {
            monitor.signalException(INVALID_MIDI_DATA, MIDIFileLoaderException.class, path, 
                    "MIDI file type is not supported by installed reader");
        }
        
        final float divisionType = format.getDivisionType();
        if (divisionType != Sequence.PPQ) {
            monitor.signalException(INVALID_MIDI_DATA, MIDIFileLoaderException.class, path, 
                    "SMPTE-based division type is not implemented");
        }
        
        final int resolution = format.getResolution();
        double ticksPer64thNote = resolution / (2 * MIDISequenceContent.NUM_32TH_NOTES_PER_BEAT);
        if (ticksPer64thNote - (int) ticksPer64thNote != 0) {
            monitor.signalException(INVALID_MIDI_DATA, MIDIFileLoaderException.class, path, 
                    "Sequence's num of ticks per 64th note is not integral");
        }
        content.setNumTicksPer64thNote((int) ticksPer64thNote);
        
        // Load sequence
        Sequence seq = null;
        try {            
            seq = MidiSystem.getSequence(file);
        } catch (IOException e) {
            return monitor.signalException(FILE_NOT_FOUND, MIDIFileLoaderException.class, e, path);
        } catch (InvalidMidiDataException e) {
            return monitor.signalException(INVALID_MIDI_DATA, MIDIFileLoaderException.class,e, path, 
                    "Data does not follow the MIDI specification");
        }

        // Set sequence timeunit length
        content.setLengthTU((int)((double) seq.getTickLength() / content.getNumTicksPer64thNote()));

        // Print basic information
        final double lengthSec = (double) seq.getMicrosecondLength() / 1000000.0;        
        final String BASE = "%4s%-24s : ";
        monitor.setDefaultVerbose(Level.DEBUG);
        monitor.log("Basic MIDI sequence info");
        monitor.log(BASE + "%d", "", "Num of tracks", seq.getTracks().length);
        monitor.log(BASE + "%d", "", "Size (bytes)", (int)(format.getByteLength()));
        monitor.log(BASE + "%.2f sec (%d ticks)","","Duration", lengthSec, seq.getTickLength());
        monitor.log(BASE + "%d ticks per quarter note","","Timing resolution", seq.getResolution());
        monitor.log(BASE + "%d", "", "Num ticks per 64th note", content.getNumTicksPer64thNote());

        return seq;
    }

    @SubMethodOf("load")
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
            monitor.log(Level.INFO, "Ignoring empty track");
            return;
        }
        
        // Add MIDI track to sequence wrapper
        content.addTrack(track);

        // Log stats
        monitor.log(Level.INFO, "Track total number of notes: %d (Tick: %d-%d)", 
                track.getNotes().size(), track.getStartTick(), track.getEndTick());
    }

    @SubMethodOf("processMIDIEvents")
    private void processMessage(MetaMessage msg, long tick) throws MIDIFileLoaderException {
        byte[] data = msg.getData();
        String strData = new String(data);
        MetaMessageType type = META_MSG_TYPES[msg.getType()];
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
                monitor.signalException(INVALID_MIDI_DATA, MIDIFileLoaderException.class, 
                        content.getSourceFile().getPath(), "Unimplemented tick adjustment for "
                        + "sequence with num of 32th notes per beat different than " 
                        + MIDISequenceContent.NUM_32TH_NOTES_PER_BEAT);
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

        logMIDIMessage("MetaMessage", type.name(), ignored, tick, strData, data, Level.DEBUG);
    }
    
    @SubMethodOf("processMIDIEvents")
    private void processMessage(SysexMessage msg, long tick) {
        byte[] data = msg.getData();
        String msgType = msg.getStatus() == SysexMessage.SPECIAL_SYSTEM_EXCLUSIVE ? 
                "SPECIAL SYST. EXCL." : "SYSTEM EXCLUSIVE";
        logMIDIMessage("SysexMessage", msgType, true, tick, new String(data), data, Level.DEBUG);
    }
    
    @SubMethodOf("processMIDIEvents")
    private void processMessage(ShortMessage msg, long tick) {
        ChannelMessageCmd cmd = CHANNEL_MSG_CMDS[msg.getCommand()];
        String strData = msg.getData1() + " " + msg.getData2();
        String msgCategory = "ChannelMessage";
        boolean ignored = false;
        Level verbLvl = Level.DEBUG;
        
        switch(cmd) {
        
        case NOTE_ON:
            byte hexNote  = (byte) msg.getData1();
            byte velocity = (byte) msg.getData2();

            if (noteBuffer[hexNote] != null) {
                monitor.signal(CONSECUTIVE_NOTE_ON_OR_OFF_EVENT, "on", tick);
                ignored = true;
            }
            noteBuffer[hexNote] = new MIDINote(hexNote, velocity, tick);
            verbLvl = Level.TRACE;
            break;

        case NOTE_OFF:
            hexNote  = (byte) msg.getData1();
            velocity = (byte) msg.getData2();

            MIDINote note = noteBuffer[hexNote];
            if (note == null) {
                monitor.signal(CONSECUTIVE_NOTE_ON_OR_OFF_EVENT, "off", tick);
                ignored = true;
            } else {
                note.setEndTick(tick);
                track.addNote(note); 
                noteBuffer[hexNote] = null;
                verbLvl = Level.TRACE;
            }
            break;

        case PROGRAM_CHANGE:
            track.addInstrumentChange(msg.getData1(), tick);
            break;

        case SYSTEM_MESSAGE:
            strData = SYS_COMMON_MSG_TYPES[msg.getStatus()].name();
            msgCategory = "System Message";
            ignored = true;
            break;

        default: 
            ignored = true;
        }

        logMIDIMessage(msgCategory, cmd.name(), ignored, tick, strData, null, verbLvl);
    }

    @HelperMethod
    private void logMIDIMessage(String msgCategory, String msgType, boolean ignored, 
                                Long tick, String strData, byte[] bytes, Level verbLvl) {

        String msg = String.format("%s | %-14s | %-17s | %6d | %s %s", ignored ? " Ignored " : 
                     "Processed", msgCategory, msgType, tick, strData, msgBytesToString(bytes));

        if (ignored) {
            monitor.signal(IGNORED_MIDI_EVENT, msg);
        } else {
            monitor.log(verbLvl, 2, msg);
        }
    }
    
    @SubMethodOf("logMIDIMessage")
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