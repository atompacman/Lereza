package com.atompacman.lereza.kpf;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.math3.util.FastMath;

import com.atompacman.lereza.core.midi.device.MIDIDeviceException;
import com.atompacman.lereza.core.midi.device.MIDIDeviceInfo;
import com.atompacman.lereza.core.midi.device.MIDIDeviceManager;
import com.atompacman.lereza.core.midi.realtime.RealTimeMIDIProcessor;
import com.atompacman.lereza.core.solfege.RythmicSignature;
import com.atompacman.toolkat.misc.Log;

public class RealTimeTempoDetector extends RealTimeMIDIProcessor {

    //====================================== CONSTANTS ===========================================\\

    // Single hit processing
    private static final double BPM_CHANGE_RATE            = 0.3;

    // Window processing
    private static final double CHORD_ADVANTAGE_ALPHA      = 1.75;

    // Common
    private static final double MINIMUM_INTERVAL_LENGTH_MS = 15.0;
    private static final double BPM_CHANGE_SENSIBILITY     = 0.3;
    private static final double INVALID_INTERVAL           = -1.0;


    //======================================= FIELDS =============================================\\

    // Modules
    private Metronome      metronome;

    // Parameters
    private final double   micros2TU;
    private final int      maxIntervalGap;

    // Temporaries
    private long           lastTimestamp;
    private final double[] hitBuf;
    private final double[] intervalHistog;



    //======================================= METHODS ============================================\\

    //---------------------------------------- MAIN ----------------------------------------------\\

    public static void main(String[] args) throws MIDIDeviceException, LineUnavailableException, 
                                                  IOException, UnsupportedAudioFileException {

        MIDIDeviceManager manager = MIDIDeviceManager.getInstance();
        MIDIDeviceInfo device = manager.getDeviceList().stream().filter(
                d -> d.getAPIInfo().getName().contains("SAMSON")).findFirst().get();
        RealTimeMIDIProcessor rtmp = new RealTimeTempoDetector(180, 
                2000, 500, RythmicSignature.valueOf(3, 4), true);
        rtmp.start();
        manager.connectDeviceToReceiver(device, rtmp, "TempoDetector");
    }


    //--------------------------------- PUBLIC CONSTRUCTORS --------------------------------------\\

    public RealTimeTempoDetector(int timeIntervalMillis, int noteHitBufLen) 
            throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        
        this(Metronome.DEFAULT_INITIAL_BPM, timeIntervalMillis, 
                noteHitBufLen, RythmicSignature.valueOf(3,4), false);
    }
    
    public RealTimeTempoDetector(double           initBPM, 
                                 int              timeIntervalMillis, 
                                 int              noteHitBufLen,
                                 RythmicSignature timeSign,
                                 boolean          startMetronome)
            throws LineUnavailableException, IOException, UnsupportedAudioFileException {

        super(timeIntervalMillis);

        // Parameters
        double millis2TU    = (double) noteHitBufLen / (double) timeIntervalMillis;
        this.micros2TU      = 0.001 * millis2TU;
        this.maxIntervalGap = (int) Math.rint(MINIMUM_INTERVAL_LENGTH_MS * millis2TU);

        // Modules
        this.metronome      = new Metronome(initBPM, timeSign);

        // Temporary
        this.lastTimestamp  = -1;
        this.hitBuf         = new double[noteHitBufLen];
        this.intervalHistog = new double[noteHitBufLen];
        
        if (startMetronome) {
            metronome.start();
        }
    }


    //--------------------------------------- PROCESS --------------------------------------------\\

    public void send(MidiMessage msg, long timestamp) {
        // Only consider NoteOn messages
        if (msg.getStatus() != ShortMessage.NOTE_ON) {
            return;
        }
        
        // Adapt tempo according to previous timestamp
        processSingleHit(msg, timestamp);
        
        // Add MIDI message to a buffer (hit window) that 
        // will be processed after a certain time interval
        super.send(msg, timestamp);
    }

    public double inferBPMFromInterval(double intervLenMS) {
        // Ignore tiny intervals
        if (intervLenMS < MINIMUM_INTERVAL_LENGTH_MS) {
            return INVALID_INTERVAL;
        }

        // Get beat length (milliseconds)
        double beatLengthMS = 60000.0 / metronome.getBPM();

        // Find closest power of ratio between beat length and interval length
        double pow2 = FastMath.log(2, beatLengthMS / intervLenMS);
        int rPow2 = (int) Math.rint(pow2);

        // Ignore intervals whose relationship with BPM is too different
        if (Math.abs(pow2 - rPow2) > BPM_CHANGE_SENSIBILITY && metronome.isPlaying()) {
            return INVALID_INTERVAL;
        }

        return 60000.0 / (intervLenMS * FastMath.pow(2, rPow2));
    }

    // - - - - - - - - - - - - - - - - - - - SINGLE HIT - - - - - - - - - - - - - - - - - - - - - \\

    public void processSingleHit(MidiMessage message, long timestamp) {
        // Cannot tell tempo for the first note
        if (lastTimestamp == -1) {
            lastTimestamp = timestamp;
            return;
        }

        // Infer current BPM
        double inferedBPM = inferBPMFromInterval((timestamp - lastTimestamp) * 0.001);
        if (inferedBPM == INVALID_INTERVAL) {
            return;
        }
        
        // Update last timestamp
        lastTimestamp = timestamp;

        // Update BPM according to a BPM change rate
        metronome.setBPM((1 - BPM_CHANGE_RATE) * metronome.getBPM() + BPM_CHANGE_RATE * inferedBPM);

        // Start metronome if not already playing
        if (!metronome.isPlaying()) {
            metronome.start();
        }
    }

    // - - - - - - - - - - - - - - - - - - - HIT WINDOW - - - - - - - - - - - - - - - - - - - - - \\

    public void process(List<MidiEvent> events) {
        // Reset hit buffer and interval histogram
        Arrays.fill(hitBuf, 0);
        Arrays.fill(intervalHistog, 0);

        // Fill note hit buffer with MIDI NoteOn events (do nothing if there are one hit or less)
        if (!fillHitBuffer(events, hitBuf, micros2TU)) {
            // Do nothing if there is only one hit
            return;
        }

        // Merge tight groups of hits into one average hit
        if (!mergeCloseData(hitBuf, maxIntervalGap)) {
            // Do nothing if there is only one hit
            return;
        }

        // Find first data point
        int lastHit = -1;
        while (lastHit < hitBuf.length - 1 && hitBuf[++lastHit] == 0);

        // Build histogram
        for (int i = lastHit + 1; i < hitBuf.length; ++i) {
            if (hitBuf[i] != 0) {
                intervalHistog[i - lastHit] += 0.5 * (hitBuf[lastHit] + hitBuf[i]);
                lastHit = i;
            }
        }

        // Merge close intervals
        mergeCloseData(intervalHistog, maxIntervalGap);

        double totalWeight = 0;
        double actualBPM = 0;

        for (int i = 0; i < intervalHistog.length; ++i) {
            // Skip interval that weren't played
            if (intervalHistog[i] == 0) {
                continue;
            }

            // Infer BPM from interval
            double inferedBPM = inferBPMFromInterval(0.001 * (i / micros2TU));
            if (inferedBPM == INVALID_INTERVAL) {
                return;
            }
            
            // Add weighted BPM to total
            actualBPM += inferedBPM * intervalHistog[i];
            totalWeight += intervalHistog[i];
        }

        // If there were no valid intervals
        if (actualBPM == 0) {
            return;
        }

        // Change BPM according to time window
        metronome.setBPM(actualBPM / totalWeight);
    }

    private static boolean fillHitBuffer(List<MidiEvent> events, double[] hitBuf, double micros2TU){
        int numHits = 0;

        for (MidiEvent event : events) {
            // Convert MIDI tick (time since beginning of window in microseconds) 
            // into processing timeunits
            int tu = (int) Math.rint(event.getTick() * micros2TU);
            if (tu >= hitBuf.length) {
                Log.warn("NoteOn event timeunit (%d) is greater than "
                        + "interval length (%d)", tu, hitBuf.length);
                tu = hitBuf.length - 1;
            }
            // Increment number of hits when a note is played at a specific timeunit
            hitBuf[tu] += 1;
            ++numHits;
        }

        return numHits > 1;
    }

    private static boolean mergeCloseData(double[] data, int maxIntervalGap) {
        int numDataPnts = 0;

        for (int i = 0; i < data.length; ++i) {
            if (data[i] == 0) {
                continue;
            }
            int beg = i++;
            int lastHit = beg;
            while (i - lastHit - 1 <= maxIntervalGap && i < data.length) {
                if (data[i] != 0) {
                    lastHit = i;
                }
                ++i;
            }
            --i;
            double sum = 0;
            for (int j = beg; j <= lastHit; ++j) {
                sum += data[j];
                data[j] = 0;
            }
            double width = (double) (lastHit - beg + 1);
            data[beg + (int)(width * 0.5)] = Math.pow(sum, CHORD_ADVANTAGE_ALPHA) / width;
            ++numDataPnts;
        }

        return numDataPnts > 1;
    }
}