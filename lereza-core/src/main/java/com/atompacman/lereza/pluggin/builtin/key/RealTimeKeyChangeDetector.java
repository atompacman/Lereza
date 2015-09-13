package com.atompacman.lereza.pluggin.builtin.key;

import java.io.File;
import java.util.List;

import javax.sound.midi.MidiEvent;

import com.atompacman.lereza.core.midi.tool.MIDINoteEventProcessor;

public class RealTimeKeyChangeDetector extends AbstractKeyChangeDetector<List<MidiEvent>> {

    //======================================= FIELDS =============================================\\

    private final int noteBufferTUlen;



    //======================================= METHODS ============================================\\

    //--------------------------------- PUBLIC CONSTRUCTORS --------------------------------------\\

    public RealTimeKeyChangeDetector(File  keyConsonanceWindow, 
                                    int    minKeyChangeGap, 
                                    double keyChangeSensibility, 
                                    int    noteBufferTUlen) {
        
        super(keyConsonanceWindow, minKeyChangeGap, keyChangeSensibility);
        this.noteBufferTUlen = noteBufferTUlen;
    }


    //------------------------------------ TU LENGTH OF ------------------------------------------\\

    protected int timeunitLengthOf(List<MidiEvent> part) {
        return noteBufferTUlen;
    }


    //-------------------------------------- ADD NOTE --------------------------------------------\\

    protected void addNotes(List<MidiEvent> events) {
        // Store in this temporary array the tick when the note begins
        int[] notesBeg = new int[256];

        // Process MIDI events
        MIDINoteEventProcessor.process(events,
                // Note begins
                (hexNote, tick) ->   notesBeg[hexNote] = (int) tick,
                // Note ends
                (hexNote, tick) -> { addNote(notesBeg[hexNote], (int) tick, hexNote);
                                     notesBeg[hexNote] = 0; });

        // Add notes that are not ended (we force them to end at the maximum timeunit)
        for (int i = 0; i < notesBeg.length; ++i) {
            if (notesBeg[i] != 0) {
                addNote(notesBeg[i], noteBufferTUlen, i);
            }
        }
    }
}