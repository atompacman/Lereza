package com.atompacman.lereza.rte;

import java.util.Map.Entry;

import com.atompacman.lereza.core.profile.drum.DrumBeat;
import com.atompacman.lereza.core.profile.drum.PercussionElement;
import com.atompacman.lereza.core.profile.drum.PercussionPattern;

public class MIDIPercussionBeatPlayer extends MIDIChannelPlayer {

    //====================================== CONSTANTS ===========================================\\

    private static final int GENERAL_MIDI_PERCU_CHANNEL = 9;



    //======================================= FIELDS =============================================\\

    private DrumBeat beat;
    private int      tu;



    //======================================= METHODS ============================================\\

    //---------------------------------- PACKAGE CONSTRUCTOR -------------------------------------\\

    MIDIPercussionBeatPlayer(DrumBeat beat, double initBPM) {
        super("Percussions", GENERAL_MIDI_PERCU_CHANNEL, beat.getTimeSignature(), initBPM);
        this.beat = beat;
        this.tu   = 0;
    }


    //---------------------------------------- PLAY ----------------------------------------------\\

    protected void playNextTU() {
        for (Entry<PercussionElement, PercussionPattern> entry : beat.getPatterns().entrySet()) {
            PercussionPattern pattern = entry.getValue();
            if (pattern.hasAHit(tu % pattern.getLengthTU())) {
                playNote(entry.getKey().getHexNote());
            }
        }
        tu = (tu + 1) % beat.getLengthTU();
    }
}
